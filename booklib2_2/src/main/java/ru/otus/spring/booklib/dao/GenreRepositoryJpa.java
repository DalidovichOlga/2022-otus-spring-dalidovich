package ru.otus.spring.booklib.dao;

import ru.otus.spring.booklib.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepositoryJpa {
    Genre insert(Genre person);

    Optional<Genre> getById(long id);

    List<Genre> getGenreByName(String name);

    List<Genre> getAll();

    void update(long id, String name);

    void deleteById(long id);
}
