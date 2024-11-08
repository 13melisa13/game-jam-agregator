# game-jam-agregator
To incorporate the additional requirements into the architecture, let’s enhance the system with specific components and configurations to meet each point. Here’s how the system will be expanded to satisfy all the specified requirements:

### System Enhancements to Meet Requirements

1. **OAuth2 Authorization**
   - **Objective:** Ensure authorization is implemented using OAuth2 protocol.
   - **Implementation:** 
     - Use Spring Security OAuth2 for managing access tokens.
     - Store OAuth2 tokens in Redis for quick access and session management as mentioned previously.
     - Implement role-based access control if needed to manage permissions across different user levels.

2. **Configuration Service with Spring Cloud Config and Git Backend**
   - **Objective:** Create a centralized configuration service that pulls configuration data from a Git repository.
   - **Implementation:**
     - Set up a `ConfigService` using Spring Cloud Config, configured to read application settings from a Git repository.
     - Store configuration files for each microservice in Git, allowing dynamic configuration updates without redeployment.
     - Set up security for the Git repository and configure the `ConfigService` to refresh configurations on demand.

3. **API Gateway Pattern (using KrakenD)**
   - **Objective:** Implement an API Gateway pattern for centralized access to all microservices.
   - **Implementation:** 
     - Use KrakenD as the API Gateway to route requests to microservices, handle request logging, and apply rate-limiting or authentication policies.
     - Configure KrakenD routes for each microservice and manage API access through gateway rules.

4. **Service Discovery**
   - **Objective:** Implement service discovery to enable microservices to locate each other dynamically.
   - **Implementation:** 
     - Use Spring Cloud Netflix Eureka for service discovery, or Kubernetes' native service discovery if deploying in Minikube.
     - Register each microservice with the service discovery mechanism to allow for automatic scaling and discovery without hardcoded URLs.

5. **Load Balancer**
   - **Objective:** Implement a load balancer to distribute requests evenly across service instances.
   - **Implementation:** 
     - If using Kubernetes, configure a load balancer for each service using Kubernetes Services with LoadBalancer type (if Minikube or a cloud environment supports it).
     - Integrate client-side load balancing with Spring Cloud's Ribbon (or native Kubernetes service endpoints if Ribbon is deprecated) for internal calls.

6. **Three Domain-Related Microservices**
   - **Objective:** Create at least three microservices related to the business domain.
   - **Suggested Services:**
     - `EventService`: Manages event data (e.g., logs, user actions).
     - `NotificationService`: Sends notifications based on certain triggers.
     - `UploadFileService`: Handles file uploads and virus scanning.
   - **Implementation:** 
     - Each service interacts with its own PostgreSQL database instance.
     - Service functionality aligns with the architecture, and each has a separate repository for isolation.

7. **Inter-Service Communication with FeignClient**
   - **Objective:** Enable communication between microservices using FeignClient.
   - **Implementation:** 
     - Use FeignClient in each service to call other services by referencing their registered names in the service discovery.
     - For instance, `NotificationService` can call `EventService` to fetch event details before sending notifications.

8. **Separate Database for Each Microservice**
   - **Objective:** Ensure each microservice has its own PostgreSQL database.
   - **Implementation:** 
     - Deploy separate PostgreSQL instances for each service (`EventDB`, `NotificationDB`, `FileDataDB`) using Helm charts.
     - Configure persistence for each instance using Persistent Volumes in Minikube.
     - Configure database connection limits and set up a monitoring user for each database to expose metrics to Prometheus.

9. **Unit Testing for Services**
   - **Objective:** Ensure each microservice has comprehensive unit tests.
   - **Implementation:** 
     - Write unit tests for each service using JUnit and Mockito.
     - Include tests for FeignClient communications, business logic, and database interactions.
     - Implement continuous integration (CI) to run tests automatically on each commit.

10. **Kubernetes Deployment with Helm Charts**
    - **Objective:** Deploy the system on Kubernetes using Helm charts for Minikube or a cloud provider like Yandex Cloud.
    - **Implementation:** 
      - Write Helm charts for each service, PostgreSQL instances, Redis, KrakenD, Kafka, Prometheus, Grafana, Jaeger, Graylog, and Eureka.
      - Configure deployment files with resource limits, autoscaling, and health probes.
      - Test deployment in Minikube or a cloud environment like Yandex Cloud if accessible.

11. **Cloud Deployment**
    - **Objective:** Deploy the system on Yandex Cloud (or VK Cloud) if possible.
    - **Implementation:** 
      - Set up a Kubernetes cluster on Yandex Cloud and deploy the services using Helm charts.
      - Configure network policies, load balancing, and persistent storage according to Yandex Cloud requirements.
      - If cloud deployment is not feasible, demonstrate the system running in Minikube as a local alternative.

### Additional Components and Infrastructure

1. **Monitoring and Logging Infrastructure**
   - **Prometheus + Grafana**: Collect and visualize metrics from each microservice and PostgreSQL database.
   - **Graylog**: Centralized logging with logs detailing HTTP methods, URLs, and sender IP addresses for traceability.

2. **Tracing with Jaeger**
   - **Objective:** Implement distributed tracing with Jaeger to trace requests across services.
   - **Implementation:** 
     - Configure Spring Boot services to use OpenTracing with Jaeger, allowing tracing for each API request.
     - Visualize request paths and latency between microservices in Jaeger.

### Enhanced System Architecture Diagram

The diagram should now include:
- **KrakenD** as the API Gateway, connected to each microservice.
- **Eureka Service Discovery** or native Kubernetes discovery.
- **Spring Cloud Config** pointing to a Git repository for centralized configuration management.
- **Kafka** for message-based communication between services like `EventService` and `NotificationService`.
- **RedisDB** for token storage in the authentication flow.
- **Prometheus and Grafana** for monitoring with metrics endpoints in each service.
- **Graylog** for centralized logging.
- **Jaeger** for distributed tracing across microservices.
  
This architecture will meet all specified requirements, providing a scalable, observable, and secure system. Let me know if you need help with specific configuration files or additional examples for any of these services!

1. API Gateway
Технология: Spring Cloud Gateway (Java) или NGINX (в качестве альтернативы).
Задача: Маршрутизация и балансировка запросов к микросервисам, управление доступом и аутентификацией через Auth Service.
Дополнительно: Поддержка фильтрации запросов, лимитирования и логирования.
2. Auth Service
Технологии: Spring Security OAuth2 (для аутентификации и авторизации), JWT (JSON Web Token) для управления токенами.
Задача: Аутентификация пользователей и микросервисов, выдача токенов доступа.
База данных: PostgreSQL или MySQL для хранения учетных данных пользователей.
3. User Management Service
Технологии: Spring Boot + Spring Data JPA (для управления базой данных).
Задача: Управление данными пользователей, ролями и доступами.
База данных: PostgreSQL или MySQL для хранения информации о пользователях и их ролях.
4. File Upload Service
Технологии: Spring Boot + Spring Web (для обработки загрузки файлов), Spring Cloud OpenFeign для взаимодействия с другими микросервисами.
Задача: Прием загружаемых файлов, временное сохранение, отправка задачи в очередь для дальнейшей обработки.
Очередь задач: RabbitMQ или Apache Kafka для передачи задач на обработку.
5. Task Queue
Технологии: RabbitMQ (если нужны простота и удобное управление задачами) или Apache Kafka (если приоритет — высокая производительность и масштабируемость).
Задача: Асинхронная обработка задач по загрузке файлов и отправке уведомлений. Используется для передачи задач от File Upload Service к File Processor Worker и Notification Service.
6. File Processor Worker
Технологии: Spring Boot для обработки задач, Spring Cloud OpenFeign для взаимодействия с другими сервисами.
Задача: Обработка файлов из очереди, проверка на вирусы через ClamAV, сохранение чистых файлов в долговременное хранилище или генерация уведомления при заражении.
Антивирусное ПО: ClamAV Daemon (сканирование на вирусы), работающий через локальное TCP-соединение.
Сетевые протоколы: TCP для связи с ClamAV Daemon (например, через порт 3310).
7. ClamAV Daemon
Технология: ClamAV (в Docker-контейнере или установленный на сервере).
Задача: Проверка файлов на вирусы, работа в режиме демона с доступом через TCP.
Контейнеризация: Docker или Kubernetes для развёртывания и управления.
8. Storage Service
Технология: Облачное хранилище (например, Amazon S3, Yandex Object Storage или аналог).
Задача: Долговременное хранение файлов. File Processor Worker сохраняет сюда безопасные файлы, а база данных только хранит ссылки на них.
База данных: PostgreSQL или MySQL для хранения метаданных файлов и ссылок на них.
9. Notification Service
Технологии: Spring Boot для создания сервиса, JavaMail API (для отправки писем через SMTP), Spring Cloud OpenFeign для взаимодействия с очередью задач.
Задача: Получение задач из очереди и отправка уведомлений пользователям о статусе загрузки файлов (успех или ошибка).
Почтовый сервер: SMTP сервер (локальный или внешний, например, SendGrid, Mailgun или Yandex SMTP).
10. Event Management Service (опционально)
Технологии: Spring Boot + Spring Data JPA для работы с базой данных, Spring Cloud OpenFeign для взаимодействия с другими сервисами.
Задача: Управление событиями, регистрация участников, обработка заявок на участие и связь с User Management Service для проверки прав пользователей.
База данных: PostgreSQL или MySQL для хранения информации о мероприятиях и пользователях, участвующих в них.
Инфраструктурные компоненты
Kubernetes: Для оркестрации и управления контейнерами всех сервисов. Kubernetes позволяет автоматически масштабировать компоненты, управлять обновлениями и мониторить состояние сервисов.
Docker: Для контейнеризации всех сервисов, включая ClamAV, Notification Service и File Processor Worker.
Prometheus и Grafana: Для мониторинга метрик и состояния сервисов в кластере Kubernetes.
ELK Stack (Elasticsearch, Logstash, Kibana): Для логирования и анализа данных, удобного отслеживания ошибок и событий в системе.
Стек и технологии в кратком виде:
Компонент	Технологии	База данных / Хранилище
API Gateway	Spring Cloud Gateway, NGINX	—
Auth Service	Spring Security OAuth2, JWT	PostgreSQL / MySQL
User Management Service	Spring Boot, Spring Data JPA	PostgreSQL / MySQL
File Upload Service	Spring Boot, Spring Cloud OpenFeign	RabbitMQ / Kafka
Task Queue	RabbitMQ / Apache Kafka	—
File Processor Worker	Spring Boot, ClamAV Daemon (TCP)	RabbitMQ / Kafka
ClamAV Daemon	ClamAV (Docker)	—
Storage Service	Amazon S3, Yandex Object Storage	PostgreSQL / MySQL
Notification Service	Spring Boot, JavaMail API	RabbitMQ / Kafka
Event Management Service	Spring Boot, Spring Data JPA	PostgreSQL / MySQL
Инфраструктура	Kubernetes, Docker, Prometheus, Grafana	ELK Stack (для логов) 
