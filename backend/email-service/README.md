# Email Service

O **Email Service** faz parte do ecossistema de microsserviços do projeto **Questionarium**. É responsável por receber eventos via RabbitMQ (quando um novo usuário é criado ou confirmado) e enviar e-mails de forma assíncrona usando JavaMail (Gmail SMTP por padrão).

## Tecnologias Utilizadas

* Java 17
* Spring Boot 3.3.x
* Spring Boot Starter AMQP (RabbitMQ)
* Spring Boot Starter Mail (JavaMail)
* Spring Boot Starter Web (para health checks)
* SLF4J / Logback (logs)
* Jackson (serialização JSON)
* Lombok
* Maven

## Pré-requisitos

* Java 17 configurado no PATH
* Maven 3.8+
* Docker & Docker Compose

Execute na raiz do monorepo:

```
docker-compose up -d
```

RabbitMQ estará disponível em:

* `localhost:5672` (UI em `localhost:15672`)

## Configuração

Arquivo `src/main/resources/application.properties`:

```properties
server.port=14003

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=admin

rabbitmq.email.queue=emailQueue
rabbitmq.user.exchange=user-exchange
rabbitmq.routing.email=user.email

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=questionariumdev@gmail.com
spring.mail.password=shyawhvwvbabxrzu
spring.mail.default-encoding=UTF-8

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=10000
spring.mail.properties.mail.debug=true
```

## Dependências Principais

* **spring-boot-starter-amqp**
* **spring-boot-starter-mail**
* **spring-boot-starter-web**
* **shared** (`br.com.questionarium:shared:1.0-SNAPSHOT`)
* **Lombok**
* **spring-boot-starter-test**

## Estrutura de Pacotes

```
email-service/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ br/com/questionarium/email_service/
│  │  │      ├─ config/
│  │  │      │   └─ RabbitMQConfig.java (configuração RabbitMQ)
│  │  │      ├─ consumers/
│  │  │      │   └─ EmailConsumer.java (listener RabbitMQ)
│  │  │      ├─ services/
│  │  │      │   └─ EmailService.java (envio JavaMail)
│  │  │      └─ EmailServiceApplication.java
│  │  └─ resources/
│  │      ├─ application.properties
│  │      └─ logback-spring.xml
└─ pom.xml
```

## Executando Localmente

1. Subir dependências:

```
docker-compose up -d
```

2. Rodar o serviço:

```
mvn clean install
mvn spring-boot:run
```

Acesse em `localhost:14003`.

## Funcionamento

* O **User Service** publica eventos via RabbitMQ.
* **EmailConsumer** escuta a fila e consome eventos.
* **EmailService** envia e-mails via JavaMail (Gmail SMTP).

## Logs e Erros

* Logs informativos no início e conclusão do envio.
* Logs de erro detalhados em caso de falhas.
