#############################################
# Stage 1: Build multimódulo (inclui shared) #
#############################################
FROM maven:3.8.7-eclipse-temurin-17-alpine AS build

WORKDIR /usr/src

# 1) Copiar o POM pai
COPY pom.xml ./pom.xml

# 2) Copiar módulo shared (pom + src)
COPY shared/pom.xml ./shared/pom.xml
COPY shared/src ./shared/src

# 3) Copiar pom.xml e src do user-service
COPY user-service/pom.xml ./user-service/pom.xml
COPY user-service/src ./user-service/src

# 4) Build do módulo user-service (incluindo shared via -am)
WORKDIR /usr/src
RUN mvn clean package -DskipTests -am -pl user-service

#############################################
# Stage 2: Runtime enxuto com JRE 17 Alpine #
#############################################
FROM eclipse-temurin:17-alpine
WORKDIR /app

# Copiar o JAR gerado de user-service
COPY --from=build /usr/src/user-service/target/user-service-1.0-SNAPSHOT.jar app.jar

# Expõe a porta (14002 conforme application.properties)
EXPOSE 14002

ENTRYPOINT ["java", "-jar", "app.jar"]
