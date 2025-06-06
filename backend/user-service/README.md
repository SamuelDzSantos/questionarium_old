# User Service

O **User Service** faz parte do ecossistema de microsserviços do projeto **Questionarium**. É responsável por gerenciar dados de usuários, fornecendo endpoints REST para operações de CRUD, gerando tokens de verificação de e-mail, ativando usuários após confirmação e publicando eventos para outros serviços (Auth e Email).

## Tecnologias utilizadas

* Java 17
* Spring Boot 3.3.x
* Spring Web (REST)
* Spring Data JPA (PostgreSQL)
* Spring Cloud Stream (RabbitMQ Binder)
* Spring AMQP (RabbitMQ)
* Jackson (para serialização JSON)
* Lombok
* Maven

## Pré-requisitos

* Java 17 configurado no PATH
* Maven 3.8+
* Docker & Docker Compose (para subir dependências)

Execute na raiz do monorepo:

```
docker-compose up -d
```

Isso sobe:

* PostgreSQL em `localhost:10000`
* RabbitMQ em `localhost:5672` (UI em `localhost:15672`)

## Configuração

As configurações do serviço estão em `src/main/resources/application.properties`. Exemplos:

```properties
spring.application.name=user-service
server.port=14002
spring.datasource.url=jdbc:postgresql://localhost:10000/questionarium_user_db
spring.datasource.username=admin
spring.datasource.password=Admin_123
spring.jpa.hibernate.ddl-auto=create
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=admin
```

## Dependências principais

* **Spring Boot Starter Web**
* **Spring Boot Starter Data JPA**
* **PostgreSQL Driver**
* **Spring Cloud Stream RabbitMQ Binder**
* **Spring Boot Starter AMQP**
* **Shared Module** (`br.com.questionarium:shared:1.0-SNAPSHOT`)

## Estrutura de pacotes

```
user-service/
├─ src/main/java/br/com/questionarium/user_service/
│  ├─ config/
│  │  ├─ RabbitConfig.java (configuração do RabbitMQ)
│  │  └─ WebConfig.java (configuração de CORS)
│  ├─ controller/
│  │  └─ UserController.java (endpoints REST)
│  ├─ dto/
│  │  ├─ CreateUserRequest.java (criação de usuário)
│  │  ├─ UpdateUserRequest.java (atualização de usuário)
│  │  └─ UserResponse.java (resposta padrão)
│  ├─ exception/
│  │  ├─ EmailAlreadyExistsException.java
│  │  ├─ TokenInvalidException.java
│  │  ├─ UserNotFoundException.java
│  │  └─ GlobalExceptionHandler.java (tratamento global)
│  ├─ model/
│  │  ├─ User.java (entidade JPA do usuário)
│  │  └─ VerificationToken.java (token de verificação)
│  ├─ repository/
│  │  ├─ UserRepository.java
│  │  └─ VerificationTokenRepository.java
│  └─ service/
│     └─ UserService.java (lógica de negócio)
└─ src/main/resources/
   └─ application.properties (configuração do serviço)
```

## Executando localmente

1. Suba as dependências:

```
docker-compose up -d
```

2. Compile e execute o serviço:

```
mvn clean install
mvn spring-boot:run
```

Acesse em `localhost:14002`.

## Endpoints

* **Criar usuário**: `POST /users`
* **Confirmar usuário**: `GET /users/confirm?token={token}`
* **Listar usuários ativos**: `GET /users`
* **Buscar usuário por ID**: `GET /users/{id}`
* **Atualizar usuário**: `PUT /users/{id}`
* **Remover usuário (soft delete)**: `DELETE /users/{id}`

## Tratamento de Exceções

Tratado globalmente via `GlobalExceptionHandler`:

* `EmailAlreadyExistsException` (409)
* `TokenInvalidException` (400)
* `UserNotFoundException` (404)

## Logs

Utiliza SLF4J para registro de logs:

```java
logger.info("Mensagem informativa");
logger.warn("Aviso");
logger.error("Erro", exceção);
```

Os logs facilitam o acompanhamento da execução do serviço.
