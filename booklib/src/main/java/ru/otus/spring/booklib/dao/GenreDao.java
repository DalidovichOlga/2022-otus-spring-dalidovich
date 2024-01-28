package ru.otus.spring.booklib.dao;

import ru.otus.spring.booklib.domain.Genre;

import java.util.List;

public interface GenreDao {
    Genre insert(Genre person);

    Genre getById(long id);

    List<Genre> getGenreByName(String name);

    List<Genre> getAll();

    void deleteById(long id);
}
