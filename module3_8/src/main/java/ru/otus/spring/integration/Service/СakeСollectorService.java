package ru.otus.spring.integration.Service;

import org.springframework.stereotype.Service;
import ru.otus.spring.integration.domain.*;

import java.util.List;


@Service("cakeСollectorService")
public class СakeСollectorService {

    public LayerCake cook(List<Ingredients> ingredientsList) throws Exception {
        Cream crem = null;
        Korzi korzi = null;

        if (ingredientsList.get(0) instanceof Cream)
            crem = (Cream) ingredientsList.get(0);
        else
            korzi = (Korzi) ingredientsList.get(0);

        if (ingredientsList.get(1) instanceof Cream)
            crem = (Cream) ingredientsList.get(1);
        else
            korzi = (Korzi) ingredientsList.get(1);

        System.out.println("Собирает торт  по заказу: " + crem.getOrderItem().getId());

        Thread.sleep(500);
        System.out.println("Заказ: " +  crem.getOrderItem().getId()+". Торт " + crem.getOrderItem().getItemName() + " готов");

        return new LayerCake(crem ,korzi);
    }
}
