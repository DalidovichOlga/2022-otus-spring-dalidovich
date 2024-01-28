package ru.otus.spring.booklib.domain;

public class Genre {


    private final String genreName;
    private long id;

    public Genre(String genreName, long id) {
        this.genreName = genreName;
        this.id = id;
    }

    public Genre(String genreName) {
        this.genreName = genreName;
        this.id = 0;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return (id + "     ").substring(0, 5) + "|" + genreName + "\n\r";
    }
}
