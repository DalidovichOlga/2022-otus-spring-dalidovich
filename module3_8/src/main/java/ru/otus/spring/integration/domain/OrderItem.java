package ru.otus.spring.integration.domain;

public class OrderItem {

    private final Long  id;
    private final String itemName;
    private final String congratulatorText;

    public OrderItem(Long id, String itemName, String congratulatorText) {
        this.id = id;
        this.itemName = itemName;
        this.congratulatorText = congratulatorText;
    }

    public String getItemName() {
        return itemName;
    }
    public String getCongratulatorText() {
        return congratulatorText;
    }

    public Long getId() {
        return id;
    }

}
