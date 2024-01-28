package ru.otus.spring.booklib.dao;

import ru.otus.spring.booklib.domain.Author;

import java.util.List;

public interface AuthorDao {
    Author insert(Author person);

    Author getById(long id);

    List<Author> getAll();

    void deleteById(long id);

    List<Author> getAuthorByShortName(String name);

    List<Author> getAuthorsByName(String authorName);

}
