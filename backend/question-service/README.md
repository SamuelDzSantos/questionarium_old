# Question Service

O Question Service faz parte do ecossistema de microsserviços do projeto Questionarium. É responsável por gerenciar perguntas (questões) criadas por usuários autenticados, fornecendo endpoints REST para criar, listar, filtrar, atualizar e inativar questões, além de expor endpoints para gerenciamento de tags e consumir mensagens RabbitMQ para resposta de chaves (answer keys).

## Tecnologias utilizadas

    Java 17
    Spring Boot 3.3.x
    Spring Web (REST)
    Spring Data JPA (PostgreSQL)
    Spring Security + OAuth2 Resource Server (JWT HS256)
    MapStruct (mapeamento DTO ↔ Entidade)
    Jackson (serialização/JSON)
    Spring AMQP (RabbitMQ + AsyncRabbitTemplate)
    Lombok
    Maven

## Pré‐requisitos

    Java 17 instalado e configurado no PATH.
    Maven 3.8+ ou superior.
    Docker & Docker Compose para subir dependências:
        PostgreSQL (mapeado para localhost:10000)
        RabbitMQ (mapeado para localhost:5672, UI em localhost:15672)
    O projeto pai (raiz) inclui um docker-compose.yml que inicializa o PostgreSQL e o RabbitMQ. Para subir tudo, execute na raiz do monorepo:

        docker-compose up -d

    Isso garantirá que:
        PostgreSQL esteja disponível em localhost:10000.
        RabbitMQ esteja disponível em localhost:5672 (UI em localhost:15672).

## Configuração

Todas as configurações específicas do Question Service estão em src/main/resources/application.properties. Abaixo há um exemplo completo comentado:

    # Porta em que o Question Service irá rodar
    server.port=14004

    # Nome da aplicação (Spring Boot)
    spring.application.name=question-service

    # RabbitMQ (para consumir mensagens “question.answer-key”)
    spring.rabbitmq.host=localhost
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=admin
    spring.rabbitmq.password=admin

    # JPA / Hibernate
    spring.jpa.show-sql=true
    spring.jpa.hibernate.ddl-auto=create-drop
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

    # Banco PostgreSQL "questionarium" (porta 10000)
    spring.datasource.url=jdbc:postgresql://localhost:10000/questionarium
    spring.datasource.username=admin
    spring.datasource.password=Admin_123

    # JWT Resource Server (mesma chave usada pelo Auth Service, HS256)
    spring.security.oauth2.resourceserver.jwt.secret=1231dwjaodij1023aowdijao10woiajdowiaj109diajwoijaodl10wodiajlijzlkscjlzkjclawkjdldfwel

    # Logging (útil para depurar Security)
    logging.level.org.springframework.security=TRACE

Em produção, mova todas as credenciais sensíveis (senha do banco, senha do RabbitMQ e chave JWT) para variáveis de ambiente ou servidor de configuração seguro.

## Dependências no pom.xml

As principais dependências são:

    spring-boot-starter-data-jpa
        Habilita JPA com Hibernate para persistência no PostgreSQL.

    postgresql (scope runtime)
        Driver PostgreSQL para conexão em tempo de execução.

    spring-boot-starter-web
        Suporte a REST controllers e servidor embutido (Tomcat).

    spring-boot-starter-security + spring-boot-starter-oauth2-resource-server
        Configuração de Resource Server para validar JWT (HS256).

    mapstruct + mapstruct-processor
        Mapeamento automático entre DTOs e entidades.

    spring-boot-starter-amqp
        Para integração com RabbitMQ (AsyncRabbitTemplate, @RabbitListener).

    jackson-databind (injetado automaticamente)
        Serialização/deserialização de objetos JSON.

    lombok (scope provided)
        Geração de getters/setters, construtores, builders, logs, etc.

    spring-boot-starter-test (scope test)
        JUnit, Mockito e demais bibliotecas de teste.

    spring-cloud-stream (opcional, se precisar consumir/produzir eventos via Stream, mas não obrigatório para uso atual).

O build utiliza:

    spring-boot-maven-plugin para empacotar e executar com mvn spring-boot:run.
    maven-compiler-plugin para configurar annotation processors (MapStruct/Lombok) e Java 17.

## Estrutura de pacotes (exemplo)

    question-service/
    ├─ src/
    │  ├─ main/
    │  │  ├─ java/
    │  │  │  └─ br/com/questionarium/question_service/
    │  │  │      ├─ config/
    │  │  │      │   └─ SecurityConfig.java         # Configura Resource Server (JWT)
    │  │  │      ├─ controller/
    │  │  │      │   ├─ QuestionController.java     # Endpoints de questão
    │  │  │      │   └─ TagController.java          # Endpoints de tag
    │  │  │      ├─ dto/
    │  │  │      │   ├─ AlternativeDTO.java
    │  │  │      │   ├─ AnswerKeyDTO.java
    │  │  │      │   ├─ QuestionDTO.java
    │  │  │      │   ├─ QuestionResponse.java
    │  │  │      │   ├─ QuestionMapper.java         # MapStruct mapper
    │  │  │      │   └─ TagDTO.java
    │  │  │      ├─ model/
    │  │  │      │   ├─ Alternative.java
    │  │  │      │   ├─ Question.java
    │  │  │      │   └─ Tag.java
    │  │  │      ├─ repository/
    │  │  │      │   ├─ AlternativeRepository.java
    │  │  │      │   ├─ QuestionRepository.java
    │  │  │      │   └─ TagRepository.java
    │  │  │      ├─ service/
    │  │  │      │   ├─ QuestionService.java        # Lógica de negócio (CRUD/filtragem)
    │  │  │      │   ├─ TagService.java              # CRUD de tags
    │  │  │      │   └─ QuestionServiceHelper.java   # Auxiliar para assoc. tags
    │  │  │      ├─ rabbit/
    │  │  │      │   ├─ RabbitMQConfig.java         # Config RabbitMQ + AsyncRabbitTemplate
    │  │  │      │   └─ Consumer.java                # @RabbitListener para “question.answer-key”
    │  │  │      └─ types/
    │  │  │          ├─ QuestionAccessLevel.java
    │  │  │          └─ QuestionEducationLevel.java
    │  │  └─ resources/
    │  │      ├─ application.properties              # Configurações do serviço
    │  │      └─ logback-spring.xml (opcional para logs)
    │  └─ test/ (códigos de teste, se houver)
    └─ pom.xml

## Como executar localmente

    Subir dependências  
    Na raiz do monorepo, execute:

        docker-compose up -d

    Isso sobe:
        PostgreSQL em localhost:10000.
        RabbitMQ em localhost:5672 (UI: http://localhost:15672).

    Compilar e rodar o Question Service  
    Navegue até a pasta question-service e execute:

        mvn clean install
        mvn spring-boot:run

    O serviço iniciará em http://localhost:14004.

## Testar via Postman / Insomnia / Front-end

    Use um JWT válido (HS256) emitido pelo Auth Service no header:

        Authorization: Bearer <token-jwt>

    para acessar endpoints protegidos.

## Configuração de Segurança (JWT)

    O Question Service atua como Resource Server, validando o token JWT que chega em cada requisição.

    No application.properties, use a mesma chave secreta (HS256) configurada no Auth Service:

        spring.security.oauth2.resourceserver.jwt.secret=<mesma-chave-do-auth>

    A classe SecurityConfig.java habilita:

        @Configuration
        public class SecurityConfig {
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/questions/**").authenticated()
                        .anyRequest().permitAll()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                    .csrf(csrf -> csrf.disable());
                return http.build();
            }
        }

    Assim, qualquer acesso a /questions/** (POST, GET, PUT, DELETE) exige um JWT Bearer válido. Rotas de tag (/questions/tags/**) não estão protegidas por default, mas podem ser anotadas com @PreAuthorize se desejar.

## RabbitMQ

    1. Exchange e Fila

        Exchange: question-exchange (tipo Topic)
        Fila: question-queue (durável)
        Binding: question-queue ← question-exchange com routing key = "question.#"

        Qualquer mensagem enviada para question-exchange com routing key começando em "question." (por ex. "question.answer-key") será entregue na fila question-queue.

    2. Configuração em RabbitMQConfig.java

        @Bean
        public TopicExchange questionExchange() {
            return new TopicExchange("question-exchange");
        }

        @Bean
        public Queue questionQueue() {
            return new Queue("question-queue", true);
        }

        @Bean
        public Binding questionQueueBinding(Queue questionQueue, TopicExchange questionExchange) {
            return BindingBuilder
                    .bind(questionQueue)
                    .to(questionExchange)
                    .with("question.#");
        }

        @Bean
        public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
            return new Jackson2JsonMessageConverter();
        }

        @Bean
        public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate,
                                                       Jackson2JsonMessageConverter converter) {
            rabbitTemplate.setMessageConverter(converter);
            return new AsyncRabbitTemplate(rabbitTemplate);
        }

    3. Consumidor (Consumer.java)

        O método @RabbitListener(queues = "question-queue") escuta todas as mensagens com routing key "question.answer-key".

        Ao receber, converte o corpo JSON (List<Long>) e invoca questionService.getAnswerKeys(ids), retornando uma List<AnswerKeyDTO> como resposta automática (AsyncRabbitTemplate).

## Modelos e DTOs

    Question (entidade)

        id: Long (PK)
        multipleChoice: boolean
        numberLines: Integer
        educationLevel: QuestionEducationLevel (enum)
        personId: Long (ID do usuário logado, extraído do JWT)
        header: String
        headerImage: String (URL ou caminho da imagem de enunciado)
        answerId: Long (ID da alternativa correta)
        enable: boolean (se a questão está ativa)
        accessLevel: QuestionAccessLevel (enum – PRIVATE / PUBLIC)
        tags: Set<Tag> (relação ManyToMany – tabela question_tags)
        alternatives: Set<Alternative> (OneToMany cascata, tabela question_alternative)
        creationDateTime: LocalDateTime, updateDateTime: LocalDateTime (registrados com @PrePersist e @PreUpdate)

    Alternative (entidade)

        id: Long (PK)
        description: String (texto da alternativa)
        imagePath: String (URL ou caminho opcional de imagem)
        isCorrect: Boolean
        explanation: String (explicação para o aluno)
        question: Question (ManyToOne – FK question_id)
        alternativeOrder: Integer (ordem de exibição)

    Tag (entidade)

        id: Long (PK)
        name: String
        description: String

    DTOs

        AlternativeDTO:
            private Long id;
            private String description;
            private String imagePath;
            private Boolean isCorrect;
            private String explanation;
            private Long questionId;
            private Integer alternativeOrder;

        AnswerKeyDTO (para resposta via RabbitMQ):
            private Long questionId;
            private Long alternativeId;

        QuestionDTO (usado em criação/atualização/retorno):
            private Long id;
            private boolean multipleChoice;
            private Integer numberLines;
            private Long personId;       // preenchido pelo controller (JWT)
            private String header;
            private String headerImage;
            private Long answerId;       // calculado pelo service
            private boolean enable;
            private QuestionEducationLevel educationLevel;
            private QuestionAccessLevel accessLevel;
            private Set<Long> tagIds;    // IDs de tags associadas
            private Set<AlternativeDTO> alternatives;

        QuestionResponse (para exibir mais detalhes, se necessário; similar a QuestionDTO + fields de timestamp).

        TagDTO:
            private Long id;
            private String name;
            private String description;

## Endpoints (REST)

    1. Questões

        POST /questions
            Descrição: Cria uma nova questão associada ao usuário logado (extraído do JWT).
            Headers:
                Authorization: Bearer <token-jwt>
                Content-Type: application/json
            Body (exemplo):
                {
                  "multipleChoice": true,
                  "numberLines": 3,
                  "educationLevel": "ENSINO_MEDIO",
                  "header": "Enunciado da questão",
                  "headerImage": null,
                  "answerId": null,
                  "enable": true,
                  "accessLevel": "PUBLIC",
                  "tagIds": [1, 2],
                  "alternatives": [
                    {
                      "description": "Opção A",
                      "isCorrect": false,
                      "imagePath": null,
                      "explanation": ""
                    },
                    {
                      "description": "Opção B",
                      "isCorrect": true,
                      "imagePath": null,
                      "explanation": ""
                    }
                  ]
                }
            Retorno:
                201 Created e o JSON QuestionDTO completo, contendo id, personId (ID do usuário extraído do token), answerId (ID da alternativa correta), timestamps etc.
                400 Bad Request se faltar alternativa correta ou mais de uma alternativa correta.
                401 Unauthorized se o token estiver ausente ou inválido.

        GET /questions/all
            Descrição: Retorna todas as questões, sem filtro.
            Headers:
                (Não exige autenticação)
            Retorno:
                200 OK e um JSON List<QuestionDTO>.

        GET /questions
            Descrição: Retorna questões filtradas por parâmetros opcionais.
            Query Params:
                personId (Long) – filtra por ID do usuário que criou
                multipleChoice (boolean) – filtra por tipo (múltipla escolha)
                tagIds (List<Long>) – filtra por listas de tags (OR lógico entre tags)
                accessLevel (int: 0 ou 1) – 0 = PRIVATE, 1 = PUBLIC
                educationLevel (int: 0 a 2) – 0 = ENSINO_FUNDAMENTAL, 1 = ENSINO_MEDIO, 2 = ENSINO_SUPERIOR
                header (String) – filtra se header contiver a substring (case-insensitive)
            Headers:
                (Não exige autenticação)
            Retorno:
                200 OK e um JSON List<QuestionDTO> com as questões que satisfazem todos os filtros.
                Mesmo que não haja resultados, retorna lista vazia com 200 OK.

        GET /questions/{id}
            Descrição: Busca questão pelo ID.
            Path Variable: id (Long) – ID da questão
            Headers:
                (Não exige autenticação)
            Retorno:
                200 OK + JSON QuestionDTO se existir.
                404 Not Found se não encontrar a questão.

        GET /questions/answer-key?questionIds=1,2,3
            Descrição: Retorna pares { questionId, alternativeId } para cada questão solicitada.
            Query Param:
                questionIds (List<Long>) – IDs das questões das quais se quer a “answer key” (ID da alternativa correta).
            Headers:
                (Não exige autenticação)
            Retorno:
                200 OK + JSON List<AnswerKeyDTO>:
                [
                  { "questionId": 1, "alternativeId": 5 },
                  { "questionId": 2, "alternativeId": 9 }
                ]
                404 Not Found se nenhum par for encontrado (lista vazia).
            RabbitMQ: Internamente, este endpoint delega à lógica de service. Há também um consumidor RabbitMQ que atende a requisições assíncronas para “question.answer-key”.

        PUT /questions/{id}
            Descrição: Atualiza uma questão existente pelo ID.
            Path Variable: id (Long) – ID da questão a atualizar.
            Headers:
                Authorization: Bearer <token-jwt>
                Content-Type: application/json
            Body: Objeto QuestionDTO (mesmo formato do POST, mas deve incluir as alternativas, inclusive id das que existirem).
            Fluxo:
                Extrai userId do token { "sub": "<userId>" }.
                Sobrescreve personId no DTO com esse userId.
                Atualiza apenas campos: multipleChoice, numberLines, header, headerImage, enable, accessLevel, educationLevel, tags e alternativas.
                Valida se há exatamente uma alternativa marcada isCorrect = true.
                Atualiza ou cria as alternativas no banco; define answerId corretamente.
            Retorno:
                200 OK + JSON QuestionDTO atualizado.
                400 Bad Request se faltar ou exceder alternativas corretas.
                401 Unauthorized se token inválido ou ausente.
                404 Not Found se a questão não existir.
                500 Internal Server Error para falhas inesperadas.

        DELETE /questions/{id}
            Descrição: “Soft delete” – marca a questão como inativa (enable = false).
            Path Variable: id (Long) – ID da questão.
            Headers:
                Authorization: Bearer <token-jwt>
            Retorno:
                204 No Content se inativação bem-sucedida.
                401 Unauthorized se token inválido/ausente.
                404 Not Found se a questão não existir.

        GET /questions/test
            Descrição: Endpoint de teste que demonstra uso do AsyncRabbitTemplate para solicitar “answer keys” de forma assíncrona:
                asyncRabbitTemplate.convertSendAndReceive("question-exchange", "question.answer-key", List.of(1L,2L))
            Retorna um Mono assíncrono com a resposta recebida do RabbitMQ.
            Headers:
                (Não exige autenticação)
            Retorno:
                200 OK + payload retornado pelo consumidor RabbitMQ (List<AnswerKeyDTO>).

    2. Tags

        Todas as rotas estão prefixadas por /questions/tags.

        POST /questions/tags
            Descrição: Cria uma nova tag no sistema.
            Headers:
                Content-Type: application/json
            Body (exemplo):
                {
                  "name": "Matemática",
                  "description": "Questões do ramo de matemática"
                }
            Retorno:
                201 Created + JSON TagDTO (incluindo o novo id).

        GET /questions/tags
            Descrição: Retorna todas as tags existentes.
            Headers:
                (Não exige autenticação)
            Retorno:
                200 OK + JSON List<TagDTO>.

        GET /questions/tags/{id}
            Descrição: Busca uma tag pelo ID.
            Path Variable: id (Long) – ID da tag.
            Retorno:
                200 OK + JSON TagDTO.
                404 Not Found se a tag não existir.

        PUT /questions/tags/{id}
            Descrição: Atualiza uma tag existente.
            Path Variable: id (Long).
            Headers:
                Content-Type: application/json
            Body:
                {
                  "name": "Física",
                  "description": "Questões de física"
                }
            Retorno:
                200 OK + JSON TagDTO atualizado.
                404 Not Found se a tag não existir.

        DELETE /questions/tags/{id}
            Descrição: Exclui permanentemente uma tag (caso não haja vínculo com questões ou se estiver ok remover).
            Path Variable: id (Long).
            Retorno:
                204 No Content em caso de sucesso.
                404 Not Found se a tag não existir.

## Fluxo de Criação de Questões com User ID

    Autenticação:
        O usuário faz login no Auth Service e recebe um JWT (HS256).
        Esse token deve conter no claim "sub" o ID do usuário (Long).

    Requisição de Criação:
        O front-end (Angular) envia POST /questions com o JWT no header Authorization: Bearer <token>.
        O body inclui todos os campos do QuestionDTO, exceto personId e answerId (estes são sobrescritos/calculados no back-end).

    Controller:
        No QuestionController#createQuestion(...), usamos @AuthenticationPrincipal Jwt jwt para injetar o token decodificado.
        Long userId = Long.valueOf(jwt.getSubject()); recupera o ID do usuário.
        Chamamos questionDTO.setPersonId(userId) para vincular a questão ao usuário logado (ignora qualquer personId enviado no JSON).

    Service:
        No QuestionService#createQuestion(...), ocorre validação de alternativas corretas, mapeamento do DTO para entidade, associação de tags, persistência em duas etapas (para gerar IDs de alternativas), definição de answerId e retorno do DTO final.

    Banco:
        A entidade Question é salva com person_id = userId.
        Alternativas são gravadas na tabela question_alternative vinculadas a question_id.
        Coluna answer_id é atualizada com o ID da alternativa correta.

Dessa forma, todas as questões criadas gravam corretamente o person_id do usuário autenticado, sem depender do front-end para fornecer esse valor.

## Observações Finais

    Enumerações
        QuestionAccessLevel e QuestionEducationLevel usam EnumType.ORDINAL para gravar o índice (0, 1, 2…) no banco. Se a ordem dos valores mudar, o mapeamento pode se quebrar. Se desejar maior resiliência, altere para EnumType.STRING.

    CORS
        O arquivo WebConfig.java habilita CORS para origens http://127.0.0.1:4200 e http://localhost:4200. Se seu front rodar em outra URL, adicione-a em allowedOrigins(...).

    MapStruct
        O QuestionMapper faz conversão bidirecional entre Question ↔ QuestionDTO, assim como entre Alternative ↔ AlternativeDTO e Tag ↔ TagDTO. Ele utiliza métodos customizados para associar tagIds e ids de alternativas.

    Limpeza de tokens e usuários inativos
        Não aplicável diretamente ao Question Service. Se futuramente desejar associar a exclusão de usuário a inativação de suas questões, implemente lógica adicional no service ou consumidor de eventos.

    Testes
        Embora este README não cubra testes, recomenda-se implementar testes unitários para:
            QuestionService (validação de alternativas, criação/atualização de questões).
            TagService (CRUD de tags).
            Consumidor RabbitMQ (Consumer) para garantir que o correto deserializa/serialização e retorno de AnswerKeyDTO.

    Produção
        Altere spring.jpa.hibernate.ddl-auto para update ou validate (evitando create-drop).
        Configure maxAge do CORS para um valor positivo se desejar cache de preflight.
        Armazene credenciais (banco, RabbitMQ, JWT secret) em variáveis de ambiente ou cofre de segredos.
        Monitore a fila question-queue para verificar se não há mensagens não processadas.

Com este README, qualquer desenvolvedor deverá conseguir configurar, executar e entender o fluxo completo do Question Service no projeto Questionarium, incluindo endpoints REST, configurações de segurança JWT e integração RabbitMQ.
