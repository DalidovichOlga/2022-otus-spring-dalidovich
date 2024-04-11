FROM maven:3.6.3-jdk-11-slim as build
WORKDIR app
COPY . ./
RUN mvn clean install

FROM openjdk:11-jre-slim as strt
COPY --from=build app/target/booklib-0.0.1-SNAPSHOT.jar  app/app.jar
EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
