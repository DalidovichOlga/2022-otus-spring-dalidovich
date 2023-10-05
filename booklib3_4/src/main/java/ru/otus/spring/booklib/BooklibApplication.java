package ru.otus.spring.booklib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.spring.booklib.security.UserService;


@SpringBootApplication
public class BooklibApplication {


    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BooklibApplication.class, args);

    }

    @Bean
    public UserDetailsService getUserDetailsService(){
        return new UserService();
    }
}
