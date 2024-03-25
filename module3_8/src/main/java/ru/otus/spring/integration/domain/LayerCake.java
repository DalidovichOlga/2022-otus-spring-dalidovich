package ru.otus.spring.integration.domain;


public class LayerCake {
    private OrderItem orderItem;
    private String cream;
    private String korzi;
    private String text;


    public LayerCake(Cream cream, Korzi korzi) {
        this.orderItem = cream.getOrderItem();
        this.cream = cream.getDescription();
        this.korzi = korzi.getDescription();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public OrderItem getOrder() {
        return orderItem;
    }

    public boolean needText() {
        return (orderItem.getCongratulatorText() != null) && (!orderItem.getCongratulatorText().isEmpty());
    }

    public String getCream() {
        return cream;
    }

    public String getKorzi() {
        return korzi;
    }

}
