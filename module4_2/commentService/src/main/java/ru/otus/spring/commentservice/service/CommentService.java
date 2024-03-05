package ru.otus.spring.commentservice.service;

import ru.otus.spring.commentservice.domain.Comment;
import ru.otus.spring.commentservice.dto.CommentAddDto;
import ru.otus.spring.commentservice.error.LibraryError;

import java.util.List;

public interface CommentService {
    List<Comment> getComment(Long id) throws LibraryError;


    Comment commentBook(Long id, CommentAddDto commentDto) throws LibraryError;

    void deleteComment(Long id, Long commentId) throws LibraryError;

    public void deleteAllComment(Long id);

    Comment modifyComment(Long id, Long commentId, String text, String nick) throws LibraryError;
}
