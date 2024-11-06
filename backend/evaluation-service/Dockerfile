# Usando uma imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Definindo o diretório de trabalho
WORKDIR /app

# Copiando o arquivo JAR para o contêiner
# COPY target/avaliacao-service-0.0.1-SNAPSHOT.jar app.jar

# Expondo a porta que o serviço usará
EXPOSE 14005

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
