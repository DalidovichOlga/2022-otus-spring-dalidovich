package ru.otus.spring.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.integration.domain.OrderItem;

@MessagingGateway
public interface CakeOrderProcessor {

    @Gateway(requestChannel = "orderChannel" )
    void process(OrderItem orderItem);
}
