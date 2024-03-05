package ru.otus.spring.booklib.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book")
@NoArgsConstructor
@Getter
@Setter
@NamedEntityGraph(name = "book-with-author-and-genre",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})
public class Book {

    @Column(name = "title", nullable = false, unique = true)
    private String title;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Указывает на связь между таблицами "один к одному"

    @ManyToOne(targetEntity = Author.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "authorid")
    private Author author;

    @ManyToOne(targetEntity = Genre.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "genreid")
    private Genre genre;

    public Book(String title, Author a, Genre g) {
        this.title = title;
        this.author = a;
        this.genre = g;
    }
}
