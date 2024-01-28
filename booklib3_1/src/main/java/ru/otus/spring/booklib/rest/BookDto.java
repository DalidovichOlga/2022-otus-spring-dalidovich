package ru.otus.spring.booklib.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.booklib.domain.Book;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDto {
    private long id;
    private String title;
    private String author;
    private String genre;

    public static BookDto toDto(Book book) {
        if (book == null) return null;
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor().getShortName(), book.getGenre().getGenreName());
    }
}
