package ru.otus.spring.booklib.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class BookView {
    private String title;
    private long id;

    private String author;
    private String genre;
    private long authorId;

    private long genreId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookView bookView = (BookView) o;
        return id == bookView.id && authorId == bookView.authorId && genreId == bookView.genreId && Objects.equals(title, bookView.title) && Objects.equals(author, bookView.author) && Objects.equals(genre, bookView.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id, author, genre, authorId, genreId);
    }


    public BookView(String title, long id, String author, String genre, long authorId, long genreId) {
        this.title = title;
        this.id = id;

        this.author = (author == null) ? "" : author;
        this.genre = (genre == null) ? "" : genre;
        this.authorId = authorId;
        this.genreId = genreId;
    }


    @Override
    public String toString() {
        return (id + "     ").substring(0, 5) + "|" + (title + " ".repeat(50)).substring(0, 50) + "|"
                + (author + " ".repeat(50)).substring(0, 40) + "|" + genre + "\n\r";
    }
}
