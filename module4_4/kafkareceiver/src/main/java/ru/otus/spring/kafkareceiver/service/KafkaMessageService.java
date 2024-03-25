package ru.otus.spring.kafkareceiver.service;

import ru.otus.spring.kafkareceiver.domain.KafkaMessage;

import java.util.List;

public interface KafkaMessageService {
    List<KafkaMessage> getAllMessages() ;
}
