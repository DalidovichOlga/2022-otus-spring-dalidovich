package ru.otus.spring.kafkareceiver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.kafkareceiver.Repository.KafkaMessageRepository;
import ru.otus.spring.kafkareceiver.domain.KafkaMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaMessageServiceImpl implements KafkaMessageService {
    private final KafkaMessageRepository messageRepository;

    @Transactional
    @Override
    public List<KafkaMessage> getAllMessages() {
        List<KafkaMessage> messages = messageRepository.findAll();
        return messages;
    }
}
