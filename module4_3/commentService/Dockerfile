FROM maven:3.6.3-jdk-11-slim as build
WORKDIR app
COPY . ./
RUN mvn clean install

FROM openjdk:11-jre-slim
COPY --from=build app/target/commentService-0.0.1-SNAPSHOT.jar /app/appcomment.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/appcomment.jar"]
