package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.BookRepositoryJpa;
import ru.otus.spring.booklib.dao.CommentRepositoryJpa;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.BookError;
import ru.otus.spring.booklib.error.CommentError;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepositoryJpa commentRepositoryJpa;
    private final BookRepositoryJpa bookRepository;

    @Autowired
    public CommentServiceImpl(BookRepositoryJpa bookRepository,
                              CommentRepositoryJpa commentRepositoryJpa
    ) {
        this.bookRepository = bookRepository;
        this.commentRepositoryJpa = commentRepositoryJpa;
    }

    @Transactional
    @Override
    public List<Comment> getComment(Long id) throws CommentError {
        final Book book = bookRepository.getById(id).orElseThrow(() -> new CommentError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        List<Comment> comments = commentRepositoryJpa.getByBookId(id);
        return comments;
    }

    @Transactional
    @Override
    public void commentBook(Long id, String nick, String text) throws CommentError {
        Book book = bookRepository.getById(id).orElseThrow(() -> new CommentError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        if ("".equals(text))
            throw new CommentError("BOOK_COMMENT_EMPTY", "id =" + String.valueOf(id));
        commentRepositoryJpa.insert(new Comment(nick, text, id));

    }

    @Transactional
    @Override
    public void deleteComment(Long id, Integer commentNumber) throws CommentError {
        Book book = bookRepository.getById(id).orElseThrow(() -> new CommentError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        List<Comment> comments = commentRepositoryJpa.getByBookId(id);
        if (commentNumber < 1 || comments.size() < commentNumber)
            throw new CommentError("NUMBER_INCORRECT", String.valueOf(commentNumber));
        commentRepositoryJpa.remove(comments.get(commentNumber - 1));
    }
}
