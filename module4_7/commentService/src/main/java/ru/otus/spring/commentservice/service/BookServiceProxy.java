package ru.otus.spring.commentservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.commentservice.dto.BookDto;
import ru.otus.spring.commentservice.dto.UserDto;

@FeignClient(name = "BOOKLIB")
public interface BookServiceProxy {
    @GetMapping(value = "/api/books/{id}")
    public BookDto getBookById(@RequestHeader("Authorization") String token, @PathVariable Long id);

    @PostMapping(value = "/api/login")
    public String getToken(@RequestBody UserDto userDto);
}
