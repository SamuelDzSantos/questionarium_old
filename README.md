# Questionarium: uma plataforma revolucionária para simplificar e aprimorar o processo de avaliação escolar

# Instruções de Instalação e Configuração
Instruções para a instalação e configuração específicas do projeto.

## 1. Requisitos Gerais
### 1.1. Java 17 (para Microsserviços em Java)
- Para instalar o Java 17, siga o tutorial oficial:
[Instalação do Java 17.](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

### 1.2. Python 3.12 (para Microsserviços em Python)
- Para instalar o Python 3.12, siga o tutorial oficial:
[Instalação do Python 3.12.](https://www.python.org/downloads/release/python-3120/)

### 1.3. Node.js e Angular CLI (para Front-end)
- Instale o Node.js conforme as instruções:
[Instalação do Node.js.](https://nodejs.org/pt)

- Instale o Angular CLI globalmente:

  `$npm install -g @angular/cli`

### 1.4. React Native e Expo (para Mobile)
Instale o React Native e o Expo Go conforme as instruções:
- [Instalação do React Native](https://reactnative.dev/docs/environment-setup)
- [Instalação do Expo](https://docs.expo.dev/more/expo-cli/)
- Em seu dispositivo móvel, instale o aplicativo Expo Go

## 2. Configuração do Docker e Serviços Externos
### 2.1. Docker e RabbitMQ
Os serviços do RabbitMQ e banco de dados PostgreSQL estão configurados em um arquivo Docker Compose separado (docker-compose.rabbitmq.yml). Para iniciar o Docker com os serviços:

Certifique-se de que o Docker e o Docker Compose estão instalados. Veja a documentação de instalação:

- [Instalação do Docker](https://docs.docker.com/engine/install/)
- [Instalação do Docker Compose](https://docs.docker.com/compose/install/)
- Navegue até o diretório do arquivo docker-compose.rabbitmq.yml e execute:

  `$docker-compose -f docker-compose.rabbitmq.yml -d up`

Os serviços estarão disponíveis conforme configurado no arquivo docker-compose.rabbitmq.yml.

## 3. Configuração dos Microsserviços
### 3.1. Microsserviços Java (Spring Boot)
Certifique-se de que o Java 17 e o Maven estão instalados. Siga os tutoriais para instalação:

- [Instalação do Maven](https://maven.apache.org/install.html)
- Para construir e rodar os microsserviços Java, navegue até o diretório do microsserviço desejado e execute:

  `$mvn clean install`
  
  `$mvn spring-boot:run`

### 3.2. Microsserviços Python
- Instalar dependências: Navegue até o diretório de cada microsserviço Python (ai-service ou answer-sheet-service) e instale as dependências usando o requirements.txt:

  `$pip install -r requirements.txt`
- Configuração da chave OPENAI (https://hub.asimov.academy/tutorial/como-gerar-uma-api-key-na-openai/): Para utilizar a API do ChatGPT, é necessário configurar a chave da OpenAI em variáveis de ambiente:

  `$export OPENAI_API_KEY="sua-chave-da-openai-aqui"`

- Rodar os microsserviços: Para rodar o microsserviço, execute:

  `python controller.py`

## 4. Configuração do Front-end (Angular)
- Instalar dependências: Navegue até o diretório do front-end e instale as dependências com:

  `$npm install`

- Rodar o front-end: Para rodar a aplicação Angular localmente, execute:

  `$ng serve --open`
A aplicação estará disponível em http://localhost:4200.

## 5. Configuração do Mobile (React Native com Expo Go)
- Instalar dependências: Navegue até o diretório do projeto React Native e instale as dependências:

  `$npm install`
- Rodar a aplicação no Expo: Para rodar o app com o Expo, execute:

  `$npm run start`
Siga as instruções para abrir o app no seu dispositivo através do Expo Go.

#Link para website
https://questionarium.onrender.com

## 🎛️ Configuração de Portas

### 🌐 Backend

| Serviço             | Porta  |
|---------------------|--------|
| Gateway             | 14000  |
| Auth                | 14001  |
| User Service        | 14002  |
| Email Service       | 14003  |
| Question Service    | 14004  |
| Assessment Service  | 14005  |
| Report Service      | 14006  |
| Answer-Sheet Service| 5000   |
| AI-Service          | 5001   |

### 🖥️ Frontend

| Aplicação          | Porta |
|--------------------|-------|
| Angular Frontend   | 4200  |


