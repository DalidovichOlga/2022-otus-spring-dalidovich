package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.CommentError;

import java.util.List;

public interface CommentService {
    List<Comment> getComment(Long id) throws CommentError;

    void commentBook(Long id, String nick, String text) throws CommentError;

    void deleteComment(Long id, Integer commentNumber) throws CommentError;
}
