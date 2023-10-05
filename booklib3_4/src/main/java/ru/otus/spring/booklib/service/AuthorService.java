package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.dto.AuthorFioDto;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthor();

    Author createAuthor(AuthorFioDto authorFioDto) throws LibraryError;

    void deleteAuthor(Long id) throws LibraryError;

    Author updateAuthor(AuthorFioDto authorFioDto) throws LibraryError;

    Author getOrCreateAuthorByParam(String authorName, Long authorId) throws LibraryError;

    Author getAuthorByParam(String authorName, Long authorId) throws LibraryError;

    Author getById(Long authorId) throws LibraryError;
}


