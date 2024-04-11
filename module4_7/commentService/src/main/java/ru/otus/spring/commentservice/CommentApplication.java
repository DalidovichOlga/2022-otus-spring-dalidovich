package ru.otus.spring.commentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableCircuitBreaker
@EnableEurekaClient
@EnableFeignClients(basePackages = "ru.otus.spring.commentservice.service" )
public class CommentApplication {


    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CommentApplication.class, args);

    }

}

