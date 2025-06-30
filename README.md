# Questionarium - Backend

Backend do Questionarium, desenvolvido em **Spring Boot** com arquitetura de **microsserviços**. O sistema utiliza API Gateway, bancos de dados PostgreSQL e MongoDB, autenticação JWT, filas RabbitMQ e orquestração SAGA para processos distribuídos.

---

## Índice

* [Arquitetura dos Microsserviços](#arquitetura-dos-microsserviços)
* [Requisitos](#requisitos)
* [Instalação e Configuração](#instalação-e-configuração)

  * [1. Configuração dos Containers](#1-configuração-dos-containers)
  * [2. Instalação do PDFNet (Assessment Service)](#2-instalação-do-pdfnet-assessment-service)
  * [3. Build e Execução dos Microsserviços](#3-build-e-execução-dos-microsserviços)
* [Rotas do API Gateway](#rotas-do-api-gateway)
* [Orquestração SAGA e Mensageria](#orquestração-saga-e-mensageria)
* [Observações do Backend](#observações-do-backend)
* [Frontend (Angular)](#frontend-angular)

  * [Requisitos do Frontend](#requisitos-frontend)
  * [Instalação do Frontend](#instalação-frontend)
  * [Rodando o Frontend](#rodando-o-frontend)
  * [Estrutura de Rotas Principais](#estrutura-de-rotas-principais)
  * [Observações do Frontend](#observações-frontend)

---

## Arquitetura dos Microsserviços

| Serviço                | Porta | Descrição                                                         |
| ---------------------- | ----- | ----------------------------------------------------------------- |
| **API Gateway**        | 14000 | Roteamento, circuit breaker e autenticação                        |
| **Auth Service**       | 14001 | Autenticação, registro, validação de usuários (JWT)               |
| **User Service**       | 14002 | Gerenciamento dos dados dos usuários                              |
| **Question Service**   | 14004 | Gerenciamento de questões do sistema                              |
| **Assessment Service** | 14005 | Gerenciamento de avaliações, aplicação de modelos, geração de PDF |
| **Detecção Service**   | 14010 | Serviço de detecção                                               |
| **AI Service**         | 14011 | Serviços de inteligência artificial                               |

---

## Requisitos

* **Java 17** (para todos os microsserviços)
* **Maven** (build dos projetos)
* **Docker e Docker Compose** (bancos e infraestrutura)
* **PDFNet SDK** (apenas para o serviço assessment, veja abaixo)

---

## Instalação e Configuração

### 1. Configuração dos Containers

Utilize o arquivo `docker-compose.yml` conforme exemplo abaixo. Ajuste os caminhos se necessário:

```yaml
version: "3.8"
services:
  postgres:
    image: postgres:latest
    container_name: postgres
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./scripts/sql/:/docker-entrypoint-initdb.d/:rw
    ports:
      - "5432:5432"
    networks:
      - questionarium-network

  pgadmin:
    image: dpage/pgadmin4
    container_name: pgadmin
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@admin.com
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "5050:80"
    depends_on:
      - postgres
    networks:
      - questionarium-network

  mongo:
    image: mongo
    restart: always
    ports:
      - 27017:27017
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: admin
    volumes:
      - ./scripts/linux/:/docker-entrypoint-initdb.d/:rw
    networks:
      - questionarium-network

  mongo-express:
    image: mongo-express
    restart: always
    ports:
      - 8081:8081
    environment:
      ME_CONFIG_MONGODB_SERVER: mongo
      ME_CONFIG_MONGODB_ADMINUSERNAME: admin
      ME_CONFIG_MONGODB_ADMINPASSWORD: admin
      ME_CONFIG_BASICAUTH: falsedock
    networks:
      - questionarium-network

  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=admin
    networks:
      - questionarium-network

networks:
  questionarium-network:
    name: questionarium-network

volumes:
  postgres-data:
```

> Os diretórios `./scripts/sql/` e `./scripts/linux/` podem conter scripts de inicialização do banco, se necessário.

**Para subir os serviços:**

```sh
docker-compose up -d
```

---

### 2. Instalação do PDFNet (Assessment Service)

O serviço de avaliação utiliza o **PDFNet SDK** para gerar PDFs.
Este passo é obrigatório **apenas** para o microsserviço `assessment`.

**a. Baixe os arquivos:**

* [Download PDFNetJava.zip](https://www.pdftron.com/downloads/PDFNetJava.zip)
* [Download HTML2PDFWindows.zip](https://www.pdftron.com/downloads/HTML2PDFWindows.zip)

**b. Extraia e copie para o diretório** `backend/assessment/lib`:

* `PDFNet.jar` (do PDFNetJava.zip)
* Pasta/descompactação de `HTML2PDFWindows` (do HTML2PDFWindows.zip)

**c. Instale o JAR manualmente no seu repositório Maven local:**

```sh
mvn install:install-file -Dfile="CAMINHO/para/PDFNet.jar" -DgroupId="com.pdftron" -DartifactId="PDFNet" -Dversion="10.10.0" -Dpackaging=jar
```

> Altere `CAMINHO/para/PDFNet.jar` para o caminho real no seu sistema.

No `pom.xml` do módulo `assessment`, adicione:

```xml
<dependency>
    <groupId>com.pdftron</groupId>
    <artifactId>PDFNet</artifactId>
    <version>10.10.0</version>
</dependency>
```

---

### 3. Build e Execução dos Microsserviços

Cada microsserviço é um projeto Spring Boot independente, porém há um pom pai no diretório raiz do projeto para facilitar a compilação de todos os microsserviços com um único comando.

Execute no diretório raíz do backend ou em cada pasta de serviço:

```sh
mvn clean install
```

**Rodando cada serviço:**
Exemplo para o serviço de Auth

```sh
cd backend/auth
./mvnw spring-boot:run
```

Repita para os demais microsserviços (`user`, `question`, `assessment`, `ai`, `deteccao`, etc).

O **Gateway** roda na porta **14000**:

```sh
cd backend/gateway
./mvnw spring-boot:run
```

---

## Rotas do API Gateway

| Caminho                                                                                                             | Serviço Interno    | Porta |
| ------------------------------------------------------------------------------------------------------------------- | ------------------ | ----- |
| /auth/register                                                                                                      | SAGA               | 15000 |
| /auth/\*\*                                                                                                          | Auth Service       | 14001 |
| /users/\*\*                                                                                                         | User Service       | 14002 |
| /questions/\*\*                                                                                                     | Question Service   | 14004 |
| /assessment-headers/**, /assessment-models/**, /applied-assessments/**, /record-assessments/**, /pdf/**, /report/** | Assessment Service | 14005 |
| /ai/\*\*                                                                                                            | AI Service         | 14011 |
| /deteccao/\*\*                                                                                                      | Detecção Service   | 14010 |

> O roteamento completo está em `RouteConfig.java`.

---

## Orquestração SAGA e Mensageria

* O processo de **criação de usuário** utiliza um orquestrador SAGA, enviando eventos por RabbitMQ para os serviços de autenticação, registro, confirmação e envio de e-mails.
* RabbitMQ precisa estar disponível (porta 5672 para app, 15672 para dashboard).
* Filas principais:

  * `CREATE_AUTH_USER_EVENT`
  * `REVERT_CREATE_AUTH_USER_EVENT`
  * `CREATE_CONFIRMATION_TOKEN_EVENT`
  * `CREATE_USER_EVENT`
  * `SEND_EMAIL_EVENT`
  * Outras relacionadas a questions e alternativas.

---

## Observações do backend

* Todos os serviços assumem variáveis de ambiente padrão, conforme o docker-compose.
* O sistema utiliza autenticação **JWT** (token gerado via `/auth`).
* Caso queira testar visualmente os bancos:

  * Mongo Express: [Acessar Mongo Express](http://localhost:8081)
  * PgAdmin: [Acessar PgAdmin](http://localhost:5050) (login: [admin@admin.com](mailto:admin@admin.com) / admin)
* Para dúvidas ou problemas na instalação do PDFNet, consulte a [documentação oficial PDFTron](https://www.pdftron.com/documentation/java/guides/install/).

---

## Frontend (Angular)

O frontend do Questionarium é uma aplicação Angular moderna, estruturada por rotas e componentes, consumindo APIs dos microsserviços via Gateway.

---

## Requisitos Frontend

* **Node.js** (recomenda-se versão LTS, ex: 20.x)
* **npm** (instalado com o Node)
* **Angular CLI**

  * Instale globalmente caso não possua:

    ```sh
    npm install -g @angular/cli
    ```

---

## Instalação Frontend

**1. Instale as dependências:**

* Rode o comando:

  ```sh
  npm install
  ```

* O diretório `node_modules` está no `.gitignore` e não é versionado, por isso é necessário rodar este comando.

**2. Configuração de ambiente:**

* Crie manualmente a pasta e o arquivo:

  * `frontend/src/environments/environment.development.ts`

* Exemplo de conteúdo do arquivo (preencha a chave da OpenAI se desejar usar):

  ```typescript
  export const environment = {
    apiUrl: "http://localhost:14000",
    openAiApiKey: ""
  };
  ```

* **Importante:** Não coloque sua chave de API no repositório! O arquivo NÃO é versionado por motivos de segurança.

---

## Rodando o Frontend

* Para rodar o servidor de desenvolvimento, utilize:

  ```sh
  ng serve
  ```

  ou

  ```sh
  npm start
  ```

* O frontend ficará disponível por padrão em:

  * [Acessar Frontend](http://localhost:4200)

---

## Estrutura de Rotas Principais

| Caminho              | Componente                  |
| -------------------- | --------------------------- |
| /                    | MainComponent               |
| /login               | LoginComponent              |
| /cadastro            | CadastrarComponent          |
| /home                | HomeComponent               |
| /sobre               | SobreComponent              |
| /devs                | DevsComponent               |
| /recuperar-senha     | RecuperarSenha              |
| /edicao/conta        | EdicaoComponent             |
| /edicao/senha        | EdicaoComponent             |
| /edicao/perfil       | EdicaoComponent             |
| /avaliacao           | AvaliacaoComponent          |
| /avaliacao/criar     | AvaliacaoCriarComponent     |
| /avaliacao/aplicar   | AvaliacaoAplicarComponent   |
| /avaliacao/aplicadas | AvaliacaoAplicadasComponent |
| /questions           | ViewQuestionsComponent      |
| /questions/criar     | CreateQuestionComponent     |
| /questions/\:id      | CreateQuestionComponent     |
| /relatorios          | ListarRelatoriosComponent   |
| /relatorios/\:id     | VerRelatorioComponent       |

> Todas as rotas são protegidas pelo `defaultCanActivateGuard`

---

## Observações Frontend

* O ambiente de desenvolvimento já aponta o `apiUrl` para o Gateway (`http://localhost:14000`), que é a porta padrão de todos os serviços backend.
* Não se esqueça de criar e configurar a OpenAI API Key caso vá usar funcionalidades integradas.
* O frontend espera que os microsserviços backend estejam disponíveis e rodando, conforme as instruções acima.
* Utilize o Angular CLI para builds, testes e serve.

**Comandos úteis:**

* `ng build` – build de produção
* `ng test` – testes unitários
* `ng lint` – análise estática de código

---
