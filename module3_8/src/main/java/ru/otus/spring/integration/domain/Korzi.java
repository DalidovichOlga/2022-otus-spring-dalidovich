package ru.otus.spring.integration.domain;

public class Korzi implements Ingredients {
    private final String description ;
    private final OrderItem orderItem;

    public Korzi(OrderItem orderItem , String description) {
        this.description = description;
        this.orderItem = orderItem;
    }

    @Override
    public String getDescription() {return description;}

    @Override
    public OrderItem getOrderItem() {
        return orderItem;
    }
}
