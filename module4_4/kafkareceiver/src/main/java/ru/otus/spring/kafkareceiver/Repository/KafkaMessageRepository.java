package ru.otus.spring.kafkareceiver.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.kafkareceiver.domain.KafkaMessage;

public interface KafkaMessageRepository extends JpaRepository<KafkaMessage, Long> {
}
