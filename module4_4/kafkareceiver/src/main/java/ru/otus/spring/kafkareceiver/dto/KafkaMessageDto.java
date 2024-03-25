package ru.otus.spring.kafkareceiver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.kafkareceiver.domain.KafkaMessage;

import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KafkaMessageDto {
    private String messageText;
    private String dateTime;
    public static KafkaMessageDto toDto(KafkaMessage msg) {
        String dateTime =msg.getMessageDate().format(DateTimeFormatter.ISO_DATE_TIME);
        return new KafkaMessageDto(msg.getMessageText(), dateTime);
    }

}
