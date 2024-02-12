package ru.otus.spring.integration.Service;

import org.springframework.stereotype.Service;
import ru.otus.spring.integration.domain.LayerCake;

@Service("сongratulatorService")
public class CongratulatorService {
    public LayerCake makeText(LayerCake cake) throws InterruptedException {
        System.out.println("Подписываем торт " + cake.getOrder().getId());
        Thread.sleep(1000);
        cake.setText(cake.getOrder().getCongratulatorText());
        System.out.println("Торт подписан. Заказ :  " + cake.getOrder().getId());
        return cake;
    }
}
