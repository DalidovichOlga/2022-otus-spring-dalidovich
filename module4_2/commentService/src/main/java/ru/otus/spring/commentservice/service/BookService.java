package ru.otus.spring.commentservice.service;

import ru.otus.spring.commentservice.dto.BookDto;
import ru.otus.spring.commentservice.error.LibraryError;

public interface BookService {
    BookDto getById(Long id) throws LibraryError;
}
