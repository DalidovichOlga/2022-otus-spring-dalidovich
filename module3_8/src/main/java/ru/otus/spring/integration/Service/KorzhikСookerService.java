package ru.otus.spring.integration.Service;

import org.springframework.stereotype.Service;
import ru.otus.spring.integration.domain.Korzi;
import ru.otus.spring.integration.domain.OrderItem;

// печет коржи для торта
@Service("korzhiMaker")
public class KorzhikСookerService {

    private Korzi KorzhikFactory(OrderItem orderItem) {
       String testo = "";
       if  (orderItem.getItemName().equals("Ореховый") ||
               orderItem.getItemName().equals("Птичье молоко")||
        orderItem.getItemName().equals("Панчо")){
           testo  = "Бисквитное тесто";
       }
       if (orderItem.getItemName().equals("Наполеон") ){
           testo = "Слоеное тесто";
       }

        if (orderItem.getItemName().equals("Прага") ){
            testo = "Бисквитное тесто с какао";
        }
        return new Korzi(orderItem,testo);
    }

    public Korzi make(OrderItem orderItem) throws InterruptedException {
        System.out.println("Замешиваем тесто для заказа: " + orderItem.getId());
        Korzi korzi = KorzhikFactory(orderItem);
        Thread.sleep(9000);
        System.out.println("Тесто готово для заказа: " + orderItem.getId());
        return korzi;

    }
}
