<h2>Сервис уведомлений об обновлениях программного обеспечения в операционной системе </h2>

Посмотреть работу проекта можно в видео описании [вот здесь](https://cloud.mail.ru/public/SeSY/Zi7D5CWwv)

:white_check_mark:   Updates <br> REST сервис с CRUD операциями (пользователи и обновления в ОС), а также продьюсером в Kafka 
обновлений в статусах CREATED, UPDATED, DELETED <br><br>
:white_check_mark:   Notifications <br> Kafka сервис, консьюмер обновлений в статусах CREATED, UPDATED и 
броадкаст отправка уведомлений по почте всем пользователям <br><br>
:white_check_mark:   Мониторинг на Prometheus и JVM борда в Grafana <br><br>
:white_check_mark:   ELK stack <br><br>

<h4> Запуск </h4>

* перейти в дирректорию 
> /updates_service/updates/src/main/resources/environment
* запустить docker-compose up <br>
⚠️ Если updates-service-postgres или updates-service не поднимутся с первого раза, поднять их руками <br>
* выполнить init.sql
* запустить docker-compose -f docker-compose-kafka.yaml up -d  <br>
⚠️ kafka-ui можно и не поднимать, только если нужно делать манипуляции с топиком и данными
* в качестве SMTP-сервера используется [SMTP сервер](https://github.com/Nilhcem/FakeSMTP), необходимо запустить jar в консоли

<h4> Создание индексов ElasticSearch вручную и дебаг </h4>

* перейти в директорию 
> /updates_service/updates/src/main/resources/environment
* создать индекс вручную
> docker exec -it elasticsearch_springboot curl -XPUT "http://localhost:9200/springboot-updates-service"
* добавить маппинг timestamp

> docker exec -it elasticsearch_springboot curl -XPUT "http://localhost:9200/springboot-updates-service/_mapping" -H 'Content-Type: application
/json' -d'
{
  "properties": {
    "timestamp": {
      "type": "date",
      "format": "yyyy-MM-dd HH:mm:ss"
    }
  }
}'
* Тестовая отправка сообщения (с netcat в Windows)
> echo "{"message": "Test log", "log_timestamp": "2025-04-24 22:00:00"}" | ncat localhost 5000
