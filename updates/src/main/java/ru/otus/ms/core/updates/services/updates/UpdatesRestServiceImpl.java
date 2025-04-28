package ru.otus.ms.core.updates.services.updates;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.ms.core.updates.dtos.updates.UpdatedHistoryDto;
import ru.otus.ms.core.updates.dtos.updates.UpdatesCreateRqDto;
import ru.otus.ms.core.updates.dtos.updates.UpdatesDto;
import ru.otus.ms.core.updates.entities.UpdateHistory;
import ru.otus.ms.core.updates.entities.Updates;
import ru.otus.ms.core.updates.exception_handlers.exceptions.NoResourceFoundException;
import ru.otus.ms.core.updates.mappers.UpdatesMapper;
import ru.otus.ms.core.updates.repositories.UpdatesRepository;
import ru.otus.ms.core.updates.repositories.UserUpdatesRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdatesRestServiceImpl implements UpdatesRestService {

    private final UpdatesMapper updatesMapper;
    private final UpdatesRepository updatesRepository;
    private final UserUpdatesRepository userUpdatesRepository;

    @Override
    public Page<Updates> getAllUpdates(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return updatesRepository.findAll(pageable);
    }

    @Override
    public Optional<UpdatesDto> getUpdateById(UUID uuid) {
        Updates updates = updatesRepository.findById(uuid);
        return updates != null ? Optional.of(updatesMapper.convertUpdatesToDto(updates)) : Optional.empty();
    }

    @Override
    public UpdatesCreateRqDto createNewUpdate(UpdatesCreateRqDto updatesCreateRqDto) {
        Updates updates = updatesMapper.convertUpdatesToEntity(updatesCreateRqDto);
        Updates save = updatesRepository.save(updates);
        return updatesMapper.convertNewUpdateToDto(save);
    }


    @Override
    public void deleteUpdateById(UUID uuid) {
        updatesRepository.deleteUpdateById(uuid);
    }


    @Override
    public boolean deleteUpdate(UUID uuid) {
        return getUpdateById(uuid)
                .map(update -> {
                    deleteUpdateExtraData(uuid);
                    deleteUpdateById(uuid);
                    return true;
                })
                .orElse(false);
    }


    @Override
    public void deleteUpdateExtraData(UUID uuid){
        userUpdatesRepository.deleteUpdateFromUserUpdates(uuid);
    }

    @Override
    public void renewUpdate(@NonNull UUID uuid, UpdatesCreateRqDto updatesCreateRqDto) {
        Updates update = updatesRepository.findById(uuid);
        if (update != null) {
            update.setPlace(updatesCreateRqDto.getPlace());
            update.setDescription(updatesCreateRqDto.getDescription());
            updatesRepository.save(update);
        } else throw new NoResourceFoundException("Update with UUID " + uuid + " not found");
    }

    @Override
    public Optional<UpdatedHistoryDto> findDeletedUpdate(UUID uuid) {
        UpdateHistory deletedUpdate = updatesRepository.findDeletedUpdate(uuid);
        return deletedUpdate != null ? Optional.of(updatesMapper.convertUpdatedHistoryToDto(deletedUpdate)) : Optional.empty();

    }

}
