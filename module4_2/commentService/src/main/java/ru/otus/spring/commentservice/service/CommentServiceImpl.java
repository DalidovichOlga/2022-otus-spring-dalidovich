package ru.otus.spring.commentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.commentservice.dao.CommentRepository;
import ru.otus.spring.commentservice.domain.Comment;
import ru.otus.spring.commentservice.dto.BookDto;
import ru.otus.spring.commentservice.dto.CommentAddDto;
import ru.otus.spring.commentservice.error.LibraryError;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookService bookService;

    @Transactional
    @Override
    public List<Comment> getComment(Long id) throws LibraryError {
        final BookDto book = bookService.getById(id);
         List<Comment> comments = commentRepository.findByBookId(id);
        return comments;
    }

    @Transactional
    @Override
    public Comment commentBook(Long id, CommentAddDto commentDto) throws LibraryError {
        BookDto book = bookService.getById(id);
        if (book == null) {
            throw new LibraryError("BOOK_NOT_FOUND", "id =" + String.valueOf(id));
        }

        if ("".equals(commentDto.getCommentText())) {
            throw new LibraryError("BOOK_COMMENT_EMPTY", "id =" + String.valueOf(id));
        }
        return commentRepository.save(new Comment(commentDto.getNick(), commentDto.getCommentText(), id));

    }

    @Transactional
    @Override
    public void deleteComment(Long id, Long commentId) throws LibraryError {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new LibraryError("NUMBER_INCORRECT", "id =" + String.valueOf(commentId)));

        if (comment.getBookId() != id)
            throw new LibraryError("NUMBER_INCORRECT", "id =" + String.valueOf(commentId));

        commentRepository.delete(comment);
    }

    @Transactional
    @Override
    public void deleteAllComment(Long id)  {
        commentRepository.deleteByBookId(id);
    }

    @Transactional
    @Override
    public Comment modifyComment(Long id, Long commentId, String text, String nick) throws LibraryError {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new LibraryError("NUMBER_INCORRECT", "id =" + String.valueOf(commentId)));

        if (comment.getBookId() != id)
            throw new LibraryError("NUMBER_INCORRECT", "id =" + String.valueOf(commentId));

        if (!"".equals(text)) {
            comment.setCommentText(text);
        }
        if (!"".equals(nick)) {
            comment.setNick(nick);
        }
        return commentRepository.save(comment);
    }
}
