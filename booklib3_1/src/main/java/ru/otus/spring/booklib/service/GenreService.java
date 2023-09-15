package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;

public interface GenreService {
    List<Genre> getAllGaner();

    Genre createGenre(Genre genre) throws LibraryError;

    void deleteGenre(Long genreId) throws LibraryError;

    Genre updateGenre(Genre genre) throws LibraryError;

    Genre getOrCreateGenreByParam(String genreName, Long genreId) throws LibraryError;
}
