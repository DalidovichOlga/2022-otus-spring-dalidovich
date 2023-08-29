package ru.otus.spring.booklib.dao;

import ru.otus.spring.booklib.domain.Comment;

import java.util.List;


public interface CommentRepositoryJpa {
    Comment insert(Comment comment);

    List<Comment> getByBookId(long id);

    void deleteByBookId(long id);

    void remove(Comment comment);
}
