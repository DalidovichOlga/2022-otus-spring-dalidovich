package ru.otus.spring.commentservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Column(name = "nick", nullable = false)
    private String nick;

    @Column(name = "commenttext", nullable = false)
    private String commentText;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "bookid", nullable = false)
    private long bookId;

    public Comment(String nick, String commentText, long bookId, long id) {
        this.nick = nick;
        this.commentText = commentText;
        this.id = id;
        this.bookId = bookId;
    }

    public Comment(String nick, String commentText, long bookId) {
        this(nick, commentText, bookId, 0);

    }
}
