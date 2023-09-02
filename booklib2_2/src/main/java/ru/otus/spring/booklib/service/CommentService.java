package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;

public interface CommentService {
    List<Comment> getComment(Long id) throws LibraryError;

    void commentBook(Long id, String nick, String text) throws LibraryError;

    void deleteComment(Long id, Integer commentNumber) throws LibraryError;
}
