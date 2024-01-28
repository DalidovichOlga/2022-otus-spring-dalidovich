package ru.otus.spring.booklib.dao;

import ru.otus.spring.booklib.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepositoryJpa {
    Author insert(Author person);

    Author insertByName(String authorname);

    Optional<Author> getById(long id);

    List<Author> getAll();

    void deleteById(long id);

    List<Author> getAuthorByShortName(String name);

    List<Author> getAuthorsByName(String authorName);

    void remove(Author author);

    void update(Author author);

}
