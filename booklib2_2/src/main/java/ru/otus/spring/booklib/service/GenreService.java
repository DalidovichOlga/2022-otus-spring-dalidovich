package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.GenreError;

import java.util.List;

public interface GenreService {
    List<Genre> getAllGaner();

    Genre createGenre(Genre genre) throws GenreError;

    void deleteGenre(Genre genre) throws GenreError;

    void updateGenre(Genre genre) throws GenreError;
}
