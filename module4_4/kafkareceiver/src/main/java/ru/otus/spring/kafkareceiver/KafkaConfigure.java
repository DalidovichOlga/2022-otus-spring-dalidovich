package ru.otus.spring.kafkareceiver;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import ru.otus.spring.kafkareceiver.Repository.KafkaMessageRepository;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfigure {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfigure.class);

    @Value("${kafkasender.server}")
    private String server;

    @Value("${kafkasender.topic}")
    private String topic;

    @Autowired
    private final KafkaMessageRepository messageRepository;

    public KafkaConfigure(KafkaMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> messsageListener() {
        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setPollTimeout(10000);

        ConcurrentMessageListenerContainer<String, String> container
                = new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProps);
        container.setupMessageListener(new TopicListener(messageRepository));

        return container;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> properties = new HashMap<>();

        logger.info("X887 Receiver server: {}", server);
        logger.info("X887Receiver server topic: {}", topic);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        //чтобы если запущено несколько сервисов каждый читал свои... ну если несколько разделов в топике
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");


        return properties;
    }
}
