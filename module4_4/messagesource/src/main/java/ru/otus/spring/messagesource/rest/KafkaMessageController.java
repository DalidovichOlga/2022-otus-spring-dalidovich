package ru.otus.spring.messagesource.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.messagesource.dto.KafkaMessageDto;

@RestController
@RequiredArgsConstructor
public class KafkaMessageController {
    @Autowired
    private final MessageChannel producingChannel;

    @PostMapping("/api/message")
    public ResponseEntity<KafkaMessageDto> sendMessage(@RequestBody KafkaMessageDto dto) {

        producingChannel.send(new GenericMessage<String>(dto.getMessageText()));

        return ResponseEntity.ok().body(dto);

    }

}


