#############################################
# Stage 1: Build e install do módulo shared #
#############################################
FROM maven:3.8.7-eclipse-temurin-17-alpine AS build

WORKDIR /usr/src

# 1) Copiar POM pai para /usr/src/pom.xml
COPY pom.xml ./pom.xml

# 2) Copiar pom.xml e src do shared
COPY shared/pom.xml ./shared/pom.xml
COPY shared/src ./shared/src

# 3) Build+install apenas do módulo shared
#    -am (also make): garante que suas dependências (como o parent) sejam construídas
#    -pl shared   : builda só o módulo 'shared'
RUN mvn clean install -DskipTests -pl shared

# (Optionally) Stage 2: extrair o JAR do shared para host, caso deseje
# FROM alpine:3.17
# WORKDIR /out
# COPY --from=build /usr/src/shared/target/shared-1.0-SNAPSHOT.jar ./shared.jar
# ENTRYPOINT ["sh", "-c", "echo 'Shared jar em /out/shared.jar'"]

# Note que não há uma imagem final “runtime” aqui,
# pois normalmente esse JAR será consumido pelos demais builds.
