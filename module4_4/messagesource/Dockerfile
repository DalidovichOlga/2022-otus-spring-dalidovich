FROM maven:3.6.3-jdk-11-slim as build
WORKDIR app
COPY . ./
RUN mvn clean install

FROM openjdk:11-jre-slim
COPY --from=build app/target/messagesource-0.0.1-SNAPSHOT.jar /app/messagesource.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/messagesource.jar"]
