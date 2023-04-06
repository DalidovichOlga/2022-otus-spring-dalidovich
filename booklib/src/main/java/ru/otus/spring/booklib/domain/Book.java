package ru.otus.spring.booklib.domain;

public class Book {
    private String title;

    private long id;
    private String publicationYear;
    private String publisher;
    private long authorId;
    private long genreId;

    public Book() {
        this.title = "";
        this.publicationYear = "";
        this.publisher = "";
        this.authorId = 0L;
        this.genreId = 0L;
        id = 0L;
    }

    public Book(String title, String publicationYear, String publisher, long authorId, long genreId) {
        this.title = title;

        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.authorId = authorId;
        this.genreId = genreId;
        id = 0L;
    }

    public Book(String title, String publicationYear, String publisher, long authorId, long genreId, long id) {
        this.title = title;

        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.authorId = authorId;
        this.genreId = genreId;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getGenreId() {
        return genreId;
    }

    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }
}
