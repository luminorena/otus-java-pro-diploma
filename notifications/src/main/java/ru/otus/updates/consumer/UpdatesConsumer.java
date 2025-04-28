package ru.otus.updates.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.updates.consumer.dtos.ConsumerDto;
import ru.otus.updates.consumer.repositories.UsersRepository;
import ru.otus.updates.mailsender.service.MailSenderService;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdatesConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MailSenderService mailSenderService;
    private final UsersRepository usersRepository;
    private String jsonMsg;
    private String place;
    private String description;

    @KafkaListener(topics = "${updates.kafka.topic}",
            groupId = "${updates.kafka.consumerGroup}")
    public void consume(String message) throws JsonProcessingException {
        ConsumerDto consumerDto = objectMapper.readValue(message, ConsumerDto.class);
        ObjectWriter ow = new ObjectMapper().writer();
        jsonMsg = ow.writeValueAsString(consumerDto);
        log.info("#### -> Consumed a message {}", jsonMsg);
        sendMailMessage();
    }

    @SneakyThrows
    public void sendMailMessage() {
        JsonNode root = objectMapper.readTree(jsonMsg);
        String status = root.get("status").asText();

        List<String> userEmails = usersRepository.findUserEmails();

        if (!status.equals("DELETED")) {
            place = root.get("place").asText();
            description = root.get("description").asText();
        }

        for (String userEmail : userEmails) {
            if (status.equals("CREATED"))
                mailSenderService.send(userEmail,
                        "Поступило новое обновление"
                        , "Обновление на " + place + " для " + description + " готово к установке");
            else if (status.equals("UPDATED"))
                mailSenderService.send(userEmail,
                        "Необходимость переустановки обновления"
                        , "Обновление на " + place + " для " + description + " необходимо переустановить");


        }


    }
}
