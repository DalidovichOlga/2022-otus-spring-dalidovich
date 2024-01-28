package ru.otus.spring.booklib.domain;

import java.util.Objects;

public class BookView {
    private final String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookView bookView = (BookView) o;
        return id == bookView.id && authorId == bookView.authorId && genreId == bookView.genreId && Objects.equals(title, bookView.title) && Objects.equals(publicationYear, bookView.publicationYear) && Objects.equals(publisher, bookView.publisher) && Objects.equals(author, bookView.author) && Objects.equals(genre, bookView.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id, publicationYear, publisher, author, genre, authorId, genreId);
    }

    private final long id;
    private final String publicationYear;
    private final String publisher;
    private final String author;
    private final String genre;
    private final long authorId;

    public long getAuthorId() {
        return authorId;
    }

    public long getGenreId() {
        return genreId;
    }

    private final long genreId;


    public BookView(String title, long id, String publicationYear, String publisher, String author, String genre, long authorId, long genreId) {
        this.title = title;
        this.id = id;
        this.publicationYear = (publicationYear == null) ? "" : publicationYear;
        this.publisher = (publisher == null) ? "" : publisher;
        this.author = (author == null) ? "" : author;
        this.genre = (genre == null) ? "" : genre;
        this.authorId = authorId;
        this.genreId = genreId;
    }

    public String getTitle() {
        return title;
    }


    public long getId() {
        return id;
    }


    public String getPublicationYear() {
        return publicationYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getAuthor() {
        return author;
    }


    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return (id + "     ").substring(0, 5) + "|" + (title + " ".repeat(50)).substring(0, 50) + "|"
                + (author + " ".repeat(50)).substring(0, 40) + "|" + genre + "\n\r";
    }
}
