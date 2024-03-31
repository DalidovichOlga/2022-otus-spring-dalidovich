package ru.otus.spring.kafkareceiver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import ru.otus.spring.kafkareceiver.Repository.KafkaMessageRepository;
import ru.otus.spring.kafkareceiver.domain.KafkaMessage;

public class TopicListener implements AcknowledgingMessageListener<String, String> {

    private final KafkaMessageRepository messageRepository;

    public TopicListener(KafkaMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        messageRepository.save(new KafkaMessage(data.value()));

        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }

}


