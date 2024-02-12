package ru.otus.spring.integration.Service;

import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.messaging.Message;
import ru.otus.spring.integration.domain.Ingredients;

public class CorrelationIngredientsStrategy implements CorrelationStrategy {
        @Override
        public Object getCorrelationKey(Message<?> message) {
            Ingredients ingredients = (Ingredients) message.getPayload();
            return ingredients.getOrderItem().getId();
        }
}
