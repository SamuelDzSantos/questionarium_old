# Gateway Service

O Gateway Service faz parte do ecossistema de microsserviços do projeto **Questionarium**. Atua como ponto de entrada unificado, roteando requisições HTTP para os serviços internos (Auth, User e Question) e validando tokens JWT.

---

## Tecnologias utilizadas

- Java 17  
- Spring Boot 3.3.x  
- Spring Cloud Gateway  
- Spring Security (WebFlux + OAuth2 Resource Server)  
- Spring AMQP (caso seja necessário publicar eventos no RabbitMQ)  
- Lombok  
- Maven  

---

## Pré-requisitos

1. Java 17 instalado e configurado no PATH.  
2. Maven (versão 3.8.x ou superior).  
3. Docker & Docker Compose para subir dependências:
   - PostgreSQL  
   - MongoDB  
   - RabbitMQ  
4. O projeto pai (raiz) já inclui o arquivo `docker-compose.yml`, que inicializa:
   - PostgreSQL (porta mapeada 10000:5432)  
   - MongoDB (porta mapeada 10002:27017)  
   - RabbitMQ (porta mapeada 5672:5672 e UI em 15672)  

   Para subir todos os containers necessários, execute na raiz do monorepo:

       docker-compose up -d  

---

## Configuração

Todas as propriedades específicas do Gateway estão em `src/main/resources/application.properties`. Exemplo comentado:

    # Porta em que o Gateway irá rodar
    server.port=14000

    # Nome da aplicação (usado pelo Spring Cloud, se aplicável)
    spring.application.name=gateway

    # Chave secreta para validar tokens JWT (mesma usada pelo AuthService)
    key.secret=1231dwjaodij1023aowdijao10woiajdowiaj109diajwoijaodl10wodiajlijzlkscjlzkjclkajclawkjdldfwel

    # Configurações do RabbitMQ (caso o Gateway precise publicar eventos)
    spring.rabbitmq.host=localhost
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=admin
    spring.rabbitmq.password=admin

---

## Dependências no `pom.xml`

O `pom.xml` do módulo **gateway** deve incluir:

    <dependencies>
        <!-- Spring Cloud Gateway -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>

        <!-- WebFlux (necessário para o Gateway reativo) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>

        <!-- Segurança reativa e OAuth2 Resource Server -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!-- Spring AMQP (opcional, caso vá publicar eventos) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <!-- Importa BOM do Spring Cloud (versão definida no POM pai) -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring.cloud.dependencies.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

---

## Estrutura de pacotes

```
gateway/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/questionarium/gateway/
│   │   │       ├── config/
│   │   │       │   ├── GatewayConfig.java
│   │   │       │   └── SecurityConfig.java
│   │   │       └── Application.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/ (testes unitários/integracionais, se necessário)
└── pom.xml
```

---

## Como executar localmente

1. Subir dependências via Docker Compose:

       docker-compose up -d

   Isso levantará:
   - PostgreSQL em `localhost:10000`  
   - MongoDB em `localhost:10002`  
   - RabbitMQ em `localhost:5672` (UI em `localhost:15672`)  

2. Build e run do Gateway Service:  
   Navegue até a pasta `gateway` e execute:

       mvn clean install
       mvn spring-boot:run

   O serviço iniciará na porta **14000**.

3. Verificar logs e rotas configuradas:
   - Rotas `/auth/**` → redireciona para AuthService (http://localhost:14001).  
   - Rotas `/users/**` → redireciona para UserService (http://localhost:14002).  
   - Rotas `/questions/**` → redireciona para QuestionService (http://localhost:14004).  

---

## Endpoints disponíveis (configurados no Gateway)

1. Qualquer requisição para `/auth/**` será roteada ao Auth Service:
   - Ex.: `GET /auth/authenticated`  
   - Ex.: `POST /auth/login`  
2. Qualquer requisição para `/users/**` será roteada ao User Service:
   - Ex.: `POST /users`  
   - Ex.: `GET /users/{id}`  
3. Qualquer requisição para `/questions/**` será roteada ao Question Service:
   - Ex.: `GET /questions`  
   - Ex.: `POST /questions`  

---

## Segurança e JWT

1. O Gateway valida tokens JWT emitidos pelo Auth Service usando HS256 e a mesma chave secreta (`key.secret`).  
2. O bean `SecurityConfig` define:
   - CORS (origem permitida: `http://localhost:4200`, métodos e headers liberados).  
   - CSRF desabilitado.  
   - Sessões stateless.  
   - Rotas públicas:
     - `POST /auth/login` (permite obter token).  
   - Qualquer outra rota exige JWT válido.  

---
