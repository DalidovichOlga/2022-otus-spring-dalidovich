package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.BookError;

import java.util.List;

public interface CommentService {
    List<Comment> getComment(Long id) throws BookError;

    void commentBook(Long id, String nick, String text) throws BookError;
}
