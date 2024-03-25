package ru.otus.spring.messagesource;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.*;
import org.springframework.messaging.MessageHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfigure {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfigure.class);

    @Value("${kafkasender.server}")
    private String server;

    @Value("${kafkasender.topic}")
    private String topic;

    @Bean
    public DirectChannel producingChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "producingChannel")
    public MessageHandler kafkaMessageHandler() {
        logger.info("X889999 Receiver server topic: {}", topic);

        KafkaProducerMessageHandler<String, String> handler =
                new KafkaProducerMessageHandler<>(kafkaTemplate());
        handler.setMessageKeyExpression(new LiteralExpression("somekey"));
        handler.setTopicExpression(new LiteralExpression(topic));

        return handler;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        logger.info("X889999 Receiver server: {}", server);

        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        return properties;
    }

}
