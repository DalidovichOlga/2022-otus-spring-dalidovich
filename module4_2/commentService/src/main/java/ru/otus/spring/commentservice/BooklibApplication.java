package ru.otus.spring.commentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BooklibApplication {


    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BooklibApplication.class, args);

    }

}


/*@EnableWebMvc*/