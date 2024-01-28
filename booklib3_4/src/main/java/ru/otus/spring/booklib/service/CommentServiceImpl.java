package ru.otus.spring.booklib.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.BookRepositoryJpa;
import ru.otus.spring.booklib.dao.CommentRepositoryJpa;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.dto.CommentAddDto;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepositoryJpa commentRepositoryJpa;
    private final BookRepositoryJpa bookRepository;

    @Transactional
    @Override
    public List<Comment> getComment(Long id) throws LibraryError {
        final Book book = bookRepository.findById(id).orElseThrow(() -> new LibraryError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        List<Comment> comments = commentRepositoryJpa.findByBookId(id);
        return comments;
    }

    @Transactional
    @Override
    public Comment commentBook(Long id,   CommentAddDto commentDto) throws LibraryError {
        Book book = bookRepository.findById(id).orElseThrow(() -> new LibraryError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        if ("".equals(commentDto.getCommentText()))
            throw new LibraryError("BOOK_COMMENT_EMPTY", "id =" + String.valueOf(id));
        return commentRepositoryJpa.save(new Comment(commentDto.getNick(), commentDto.getCommentText(), id));

    }

    @Transactional
    @Override
    public void deleteComment(Long id, Long commentId) throws LibraryError {
        Book book = bookRepository.findById(id).orElseThrow(() -> new LibraryError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        List<Comment> comments = commentRepositoryJpa.findByBookId(id);
        Comment comDel = comments.stream().filter(t -> t.getId() == commentId).findFirst().orElseThrow(
                () -> new LibraryError("NUMBER_INCORRECT", "id =" + String.valueOf(commentId))
        );
        commentRepositoryJpa.delete(comDel);
    }

    @Transactional
    @Override
    public Comment modifyComment(Long id, Long commentId, String text, String nick) throws LibraryError {
        Book book = bookRepository.findById(id).orElseThrow(() -> new LibraryError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        List<Comment> comments = commentRepositoryJpa.findByBookId(id);
        Comment comFind = comments.stream().filter(t -> t.getId() == commentId).findFirst().orElseThrow(
                () -> new LibraryError("NUMBER_INCORRECT", "id =" + String.valueOf(commentId))
        );
        if (!"".equals(text))
            comFind.setCommentText(text);
        if (!"".equals(nick))
            comFind.setNick(nick);
        return commentRepositoryJpa.save(comFind);
    }
}
