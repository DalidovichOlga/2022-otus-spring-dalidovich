package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.error.AuthorError;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthor();

    Author createAuthor(Author author) throws AuthorError;

    void deleteAuthor(Long id, String name) throws AuthorError;
}
