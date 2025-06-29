-- Essa instrução será executada assim que o container do Postgres iniciar,
-- criando o database user_db (o owner será o usuário “admin” conforme o seu Docker Compose).
CREATE DATABASE questionarium_user OWNER admin;
CREATE DATABASE questionarium OWNER admin;