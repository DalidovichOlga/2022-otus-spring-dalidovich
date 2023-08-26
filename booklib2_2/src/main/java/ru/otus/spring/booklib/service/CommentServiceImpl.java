package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.BookRepositoryJpa;
import ru.otus.spring.booklib.dao.CommentRepositoryJpa;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.BookError;

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
    public List<Comment> getComment(Long id) throws BookError {
        final Book book = bookRepository.getById(id).orElseThrow(() -> new BookError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        List<Comment> comments = commentRepositoryJpa.getByBookId(id);
        return comments;
    }

    @Transactional
    @Override
    public void commentBook(Long id, String nick, String text) throws BookError {
        Book book = bookRepository.getById(id).orElseThrow(() -> new BookError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        if ("".equals(text))
            throw new BookError("BOOK_COMMENT_EMPTY", "id =" + String.valueOf(id));
        commentRepositoryJpa.insert(new Comment(nick, text, id));

    }
}
