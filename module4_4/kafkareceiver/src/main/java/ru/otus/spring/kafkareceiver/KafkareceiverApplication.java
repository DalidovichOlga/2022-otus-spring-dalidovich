package ru.otus.spring.kafkareceiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class KafkareceiverApplication {


    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(KafkareceiverApplication.class, args);

    }

}

