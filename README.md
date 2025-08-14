# Order Service

## Описание
Сервис для управления заказами, построенный на Spring Boot с использованием PostgreSQL и RabbitMQ.

## Требования
- Docker и Docker Compose
- Maven
- JDK 17

## Установка и запуск

1. **Соберите и запустите приложение с помощью Docker Compose**:
   ```bash
   docker-compose up -d --build
   ```

2. **Проверьте работоспособность**:
    - Убедитесь, что все контейнеры запущены:
      ```bash
      docker ps
      ```
    - Проверьте health-эндпоинт приложения:
      ```bash
      curl http://localhost:8081/actuator/health
      ```

3. **Остановка**:
   ```bash
   docker-compose down
   ```