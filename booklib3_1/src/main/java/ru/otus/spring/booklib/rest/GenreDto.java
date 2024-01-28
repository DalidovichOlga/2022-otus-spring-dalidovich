package ru.otus.spring.booklib.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Genre;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenreDto {
    private long id;
    private String genreName;

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getGenreName());
    }
}
