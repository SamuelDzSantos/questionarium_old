#############################################
# Stage 1: Build multimódulo (inclui shared) #
#############################################
FROM maven:3.8.7-eclipse-temurin-17-alpine AS build

WORKDIR /usr/src

# 1) Copiar o POM pai para que o Maven reconheça o projeto multimódulo
COPY pom.xml ./pom.xml

# 2) Copiar módulo shared (pom + src) para dentro do container
COPY shared/pom.xml ./shared/pom.xml
COPY shared/src ./shared/src

# 3) Copiar pom.xml e src do auth
COPY auth/pom.xml ./auth/pom.xml
COPY auth/src ./auth/src

# 4) Build do módulo auth, garantindo que o shared seja compilado antes (-am -pl)
WORKDIR /usr/src
# -am (also make): cria o shared primeiro
# -pl auth      : limita o build ao módulo 'auth' (e ao shared, por causa do -am)
RUN mvn clean package -DskipTests -am -pl auth

#############################################
# Stage 2: Runtime enxuto com JRE 17 Alpine #
#############################################
FROM eclipse-temurin:17-alpine
WORKDIR /app

# Copia apenas o JAR do módulo auth gerado no estágio anterior
COPY --from=build /usr/src/auth/target/auth-1.0-SNAPSHOT.jar app.jar

# Expõe a porta do auth (conforme application.properties: server.port=14001)
EXPOSE 14001

ENTRYPOINT ["java", "-jar", "app.jar"]
