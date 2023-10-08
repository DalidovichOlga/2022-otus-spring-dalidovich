package ru.otus.spring.booklib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = true)
public class BooklibApplication {


    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BooklibApplication.class, args);

    }


}
