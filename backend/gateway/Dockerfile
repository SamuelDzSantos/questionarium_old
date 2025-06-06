# === Etapa 1: build com Maven/JDK17 ===
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace

# 1. Copia o pom.xml pai
COPY ../pom.xml ./pom.xml

# 2. Copia o módulo shared
COPY ../shared ./shared

# 3. Copia o módulo gateway
COPY . ./gateway

# 4. Compila somente o módulo gateway (e suas dependências: shared)
WORKDIR /workspace
RUN mvn clean package -DskipTests -pl gateway -am

# === Etapa 2: runtime com JRE17 ===
FROM eclipse-temurin:17-jre
WORKDIR /app

# (Opcional) cria usuário não-root
RUN groupadd -r appgroup && useradd -r -g appgroup appuser
USER appuser

# Copia o JAR gerado no build
COPY --from=build /workspace/gateway/target/gateway-1.0-SNAPSHOT.jar ./gateway.jar

# Expõe a porta 14000 (conforme server.port no application.properties)
EXPOSE 14000

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java -jar /app/gateway.jar"]
