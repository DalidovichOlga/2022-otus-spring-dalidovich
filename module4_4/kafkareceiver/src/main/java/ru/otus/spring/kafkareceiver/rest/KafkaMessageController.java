package ru.otus.spring.kafkareceiver.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.kafkareceiver.domain.KafkaMessage;
import ru.otus.spring.kafkareceiver.dto.KafkaMessageDto;
import ru.otus.spring.kafkareceiver.service.KafkaMessageService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class KafkaMessageController {
    private final KafkaMessageService service;

    @GetMapping("/api/messages")
    public ResponseEntity<List<KafkaMessageDto>> getMessages() {

        List<KafkaMessage> comments = service.getAllMessages();

        List<KafkaMessageDto> dtoList = comments.stream().map(t -> KafkaMessageDto.toDto(t)).
                collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);

    }

}


