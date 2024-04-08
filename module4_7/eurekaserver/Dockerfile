FROM maven:3.6.3-jdk-11-slim as build
WORKDIR app
COPY . ./
RUN mvn clean install

FROM openjdk:11-jre-slim
COPY --from=build app/target/eurekaservice.jar /app/eurekaservice.jar
EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/eurekaservice.jar"]
