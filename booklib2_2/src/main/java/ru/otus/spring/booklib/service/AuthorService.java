package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.error.AuthorError;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthor();

    Author createAuthor(Author author) throws AuthorError;

    void deleteAuthor(Long id) throws AuthorError;

    void updateAuthor(Long id , String lastName, String firstName, String middleName) throws AuthorError;

    Author getOrCreateAuthorByParam(String authorName, Long authorId) throws AuthorError;

    Author getAuthorByParam(String authorName, Long authorId) throws AuthorError;
}
