# Auth Service

O Auth Service faz parte do ecossistema de microsserviços do projeto **Questionarium**. É responsável por autenticar usuários via JWT, validar permissões (roles) e processar eventos de registro enviados por outros microsserviços através do RabbitMQ.

---

## Tecnologias utilizadas

- Java 17  
- Spring Boot 3.3.x  
- Spring Security (JWT)  
- Spring Data MongoDB  
- Spring Data JPA (não utilizado diretamente neste módulo, mas presente na stack)  
- Spring AMQP (RabbitMQ)  
- Lombok  
- Maven  

---

## Pré-requisitos

1. **Java 17** instalado e configurado no PATH  
2. **Maven** (versão 3.8.x ou superior)  
3. **Docker & Docker Compose** (para subir dependências: MongoDB, PostgreSQL e RabbitMQ)  
4. O projeto pai (root) já inclui um arquivo `docker-compose.yml` que inicializa:  
   - PostgreSQL (porta mapeada 10000:5432)  
   - MongoDB (porta mapeada 10002:27017)  
   - RabbitMQ (porta mapeada 5672:5672 e UI em 15672)  

   Para rodar tudo localmente, basta executar na raiz do monorepo:

       docker-compose up -d

   Esse compose também já executa o script `init-db.sql` para criar os bancos necessários no PostgreSQL.

---

## Configuração

Todas as configurações específicas do Auth Service estão em `src/main/resources/application.properties`. Abaixo um exemplo comentado:

- **Porta em que o Auth Service irá rodar**  
    server.port=14001

- **Nome da aplicação (usado pelo Spring Cloud Discovery, se aplicável)**  
    spring.application.name=auth

- **Banco PostgreSQL**  
    (O serviço não usa JPA ativamente, mas a configuração fica aqui caso seja necessário)  
    spring.datasource.url=jdbc:postgresql://localhost:10000/auth  
    spring.datasource.username=admin  
    spring.datasource.password=Admin_123  
    spring.jpa.generate-ddl=true  
    spring.jpa.hibernate.ddl-auto=create-drop

- **MongoDB**  
    spring.data.mongodb.uri=mongodb://admin:Admin_123@localhost:10002  
    spring.data.mongodb.database=auth_db2

- **RabbitMQ**  
    (O Auth Service ouvirá eventos vindos da fila "auth-queue")  
    spring.rabbitmq.host=localhost  
    spring.rabbitmq.port=5672  
    spring.rabbitmq.username=admin  
    spring.rabbitmq.password=admin

- **Chaves para JWT (HS256)**  
    Em produção, recomenda-se externalizar em variáveis de ambiente  
    key.secret=1029831093810283dwiajhdkwadhkauhd10283091kdjwakdjawkd  
    # Opcional: chave pública (não utilizada se só HS256 for empregado)  
    key.public=0123810283oaiwkdjaklj19832wjikhdkajh198329aksjhdkahw9  
    # Expiração do token em segundos (ex.: 86400 = 24 horas)  
    jwt.expiration=86400

Para alterar qualquer propriedade (porta, credenciais, secret), basta editar este arquivo ou usar parâmetros/variáveis de ambiente.

---

## Dependências no pom.xml

O `pom.xml` do módulo auth (filho do projeto pai) deve incluir obrigatoriamente:

    <dependencies>
        <!-- Dependências do Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

        <!-- Dependência para validação de DTOs -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Driver MongoDB e PostgreSQL são trazidos pelos starters, mas garantir -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>

        <!-- Lombok já está no POM pai, mas listar aqui se necessário -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>

---

## Estrutura de pacotes

    auth/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── br/com/questionarium/auth/
    │   │   │       ├── config/
    │   │   │       │   ├── RabbitMQConfig.java
    │   │   │       │   └── SecurityConfig.java
    │   │   │       ├── controller/
    │   │   │       │   └── AuthController.java
    │   │   │       ├── interfaces/
    │   │   │       │   ├── CreatedUserAuthDTO.java
    │   │   │       │   └── LoginFormDTO.java
    │   │   │       ├── model/
    │   │   │       │   └── AuthUser.java
    │   │   │       ├── repository/
    │   │   │       │   └── AuthUserRepository.java
    │   │   │       ├── service/
    │   │   │       │   ├── AuthUserService.java
    │   │   │       │   ├── MongoDBUserDetailsService.java
    │   │   │       │   └── listeners/
    │   │   │       │       └── AuthListener.java
    │   │   │       └── service/other/
    │   │   │           └── JwtUtils.java
    │   │   └── resources/
    │   │       └── application.properties
    │   └── test/
    │       └── java/ (testes unitários/integracionais, caso necessário)
    └── pom.xml

---

## Como executar localmente

1. Subir dependências via Docker Compose  
   Na raiz do projeto (onde fica o `docker-compose.yml`), execute:

       docker-compose up -d

   Isso levantará:  
   - PostgreSQL em localhost:10000  
   - MongoDB em localhost:10002  
   - RabbitMQ em localhost:5672 (UI em localhost:15672)

2. Build e run do Auth Service  
   Navegue até a pasta `auth` e execute:

       mvn clean install  
       mvn spring-boot:run

   O serviço iniciará na porta 14001.

3. Verificar logs  
   - Em caso de cadastro via fila, você verá logs do `AuthListener`.  
   - Ao chamar `/auth/login`, será gerado um JWT e exibido no console (e retornado em JSON).

---

## Endpoints disponíveis

1. **Autenticação**  
   **POST** `/auth/login`

   - Envia credenciais para obter token JWT.  
   - Request Body (exemplo):  
     
         {
           "login": "usuario@exemplo.com",
           "password": "senha123"
         }
     
   - Resposta (HTTP 200):  
     
         {
           "token": "JWT-VALIDO"
         }
     
   - Em caso de erro (usuário não encontrado ou senha inválida), retorna `401 Unauthorized` ou `400 Bad Request` (se payload inválido).

2. **Testes de autorização**  
   **GET** `/auth/authenticated`

   - Verifica se o token enviado nos headers é válido (qualquer usuário autenticado).  
   - Requisição:  
     
         GET /auth/authenticated  
         Authorization: Bearer JWT-VALIDO
     
   - Resposta: HTTP 200 com body `true` se o token for válido; caso contrário, `401 Unauthorized`.

   **GET** `/auth/admin`

   - Endpoint protegido que requer `ROLE_ADMIN`.  
   - Requisição:  
     
         GET /auth/admin  
         Authorization: Bearer JWT-VALIDO

   - Resposta: HTTP 200 se o token pertencer a um usuário com `ROLE_ADMIN`; caso contrário, `403 Forbidden` ou `401 Unauthorized`.

---

## Registro de usuários (via RabbitMQ)

O Auth Service não expõe endpoint HTTP para cadastro direto. Em vez disso, outros microsserviços (por exemplo, `user-service`) publicam eventos no RabbitMQ assim que um usuário é criado:

- **Exchange**: `user-exchange` (tipo Topic)  
- **Queue**: `auth-queue` (vinculada à routing key `user.created`)

Fluxo simplificado:

1. Outro serviço (p. ex. `user-service`) cria usuário no banco relacional.  
2. Publica no RabbitMQ:

       UserCreatedEvent event = new UserCreatedEvent(userId, email, rawPassword, role);
       rabbitTemplate.convertAndSend("user-exchange", "user.created", event);

3. `AuthListener` (ouvinte de `auth-queue`) recebe o evento.  
4. Constrói `CreatedUserAuthDTO` e chama `AuthUserService.register(...)`.  
5. Um novo documento `AuthUser` é salvo no MongoDB com senha criptografada (BCrypt).

---

## JWT e Security

- Os tokens são gerados pelo `JwtUtils` usando HS256 com a chave secreta configurada em `application.properties` (`key.secret`).  
- Claims incluídas no token:  
  - `sub`: ID do usuário (ObjectId do Mongo).  
  - `email`: login.  
  - `role`: `USER` ou `ADMIN`.  
  - `iat` e `exp`: timestamps de emissão e expiração.  

- A configuração de segurança (`SecurityConfig`) define:  
  - CORS (origem permitida `http://localhost:4200`, todos os métodos e headers).  
  - CSRF desabilitado.  
  - Sessão stateless (não armazena sessão HTTP).  
  - Roteamento de autorização:  
    - `/auth/login` → aberto.  
    - `/auth/admin` → somente `ROLE_ADMIN`.  
    - Qualquer outro endpoint → requer token válido.  
  - Configuração do resource server usando JWT:

        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.decoder(jwtDecoder()))
        );

---

## Observações finais

- **ID em MongoDB**: cada documento `AuthUser` usa `@Id private String id;` (ObjectId), gerado automaticamente pelo Mongo.  
- **Validações nos DTOs**: `LoginFormDTO` e `CreatedUserAuthDTO` usam `@NotBlank` e `@Email` para garantir payload válido.  

### Boas práticas

- Em ambiente de produção, mova `key.secret` para variável de ambiente (por ex.: `export KEY_SECRET=...`).  
- Ajuste `spring.jpa.hibernate.ddl-auto` para `validate` ou `update` se precisar manter tabelas entre reinícios.  
- Monitore filas no RabbitMQ para evitar perda de mensagens.
