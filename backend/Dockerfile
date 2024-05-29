FROM maven:3.9.7-sapmachine-21 AS build
COPY . .
RUN mvn clean install spring-boot:repackage

FROM openjdk:23-slim-bullseye
COPY --from=build /target/questionarium-0.0.1-SNAPSHOT.jar questionarium.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","questionarium.jar" ]