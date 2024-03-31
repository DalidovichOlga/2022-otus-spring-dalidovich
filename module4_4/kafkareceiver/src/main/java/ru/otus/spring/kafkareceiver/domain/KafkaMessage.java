package ru.otus.spring.kafkareceiver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kafkamessage")
@NoArgsConstructor
@Getter
@Setter
public class KafkaMessage {

    @Column(name = "messagetext")
    private String messageText;

    @Column(name = "messagedate")
    private LocalDateTime messageDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public KafkaMessage(String messageText) {
        this.messageText = messageText;
        messageDate = LocalDateTime.now();
    }
}
