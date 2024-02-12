package ru.otus.spring.integration.Service;

import org.springframework.stereotype.Service;
import ru.otus.spring.integration.domain.Cream;
import ru.otus.spring.integration.domain.OrderItem;

//изготовитель крема
@Service("creamMaker")
public class CreamMakerService {

    private Cream CreamFactory(OrderItem orderItem) {
        String cream = "неизвестный";
        if (orderItem.getItemName().equals("Наполеон"))
            cream = "Заварной крем";
        if (orderItem.getItemName().equals("Ореховый"))
            cream = "Масляный крем";
        if (orderItem.getItemName().equals("Птичье молоко"))
            cream = "Суфле";
        if (orderItem.getItemName().equals("Панчо"))
            cream = "Сметанный крем";
        if (orderItem.getItemName().equals("Прага"))
            cream = "Масляный крем с шоколадом";

        return new Cream(orderItem, cream);
    }

    public Cream make(OrderItem orderItem) throws InterruptedException {
        System.out.println("Приготовим крем для торта " + orderItem.getItemName() + " Заказ " + orderItem.getId());
        Cream cream = CreamFactory(orderItem);
        Thread.sleep(7000);
        System.out.println("Крем готов. Заказ:  " + orderItem.getId());
        return cream;

    }
}
