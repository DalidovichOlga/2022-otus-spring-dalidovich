package ru.otus.spring.messagesource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.concurrent.ForkJoinPool;

@SpringBootApplication
@EnableIntegration
public class MessagesourceApplication {


    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(MessagesourceApplication.class, args);

        MessageChannel producingChannel =
                context.getBean("producingChannel", MessageChannel.class);

        ForkJoinPool pool = ForkJoinPool.commonPool();

        while (true) {

            pool.execute(() -> {
                GenericMessage<String> message =
                        new GenericMessage<>("Сообщение отправляемое раз в минуту" );
                producingChannel.send(message);

            });
            Thread.sleep(60000);
        }

    }

}


/*@EnableWebMvc*/