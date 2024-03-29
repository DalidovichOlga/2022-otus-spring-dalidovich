package ru.otus.spring.booklib.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.service.BookService;
import ru.otus.spring.booklib.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommentController {
    private final CommentService service;
    private final BookService bookService;

    public CommentController(CommentService service, BookService bookService) {
        this.service = service;
        this.bookService = bookService;
    }

    @GetMapping("/api/books/{id}/comments")
    public ResponseEntity<BookWithCommentsDto> getCommentsById(@PathVariable("id") long id) throws LibraryError {
        Book book = bookService.getById(id);
        List<Comment> comments = service.getComment(id);

        List<CommentDto> dtoList = comments.stream().map(t -> CommentDto.toDto(t)).
                collect(Collectors.toList());
        return ResponseEntity.ok().body(new BookWithCommentsDto(BookDto.toDto(book), dtoList));

    }

    @PostMapping("/api/books/{bookId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable("bookId") long bookId, @RequestBody CommentAddDto dto) throws LibraryError {
        Comment comment = service.commentBook(bookId, dto.getNick(), dto.getCommentText());

        return ResponseEntity.status(HttpStatus.CREATED).body(CommentDto.toDto(comment));
    }

    @PatchMapping("/api/books/{bookId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("bookId") Long bookId,
                                                    @PathVariable("commentId") Long commentId,
                                                    @RequestParam(value = "nick", required = false, defaultValue = "") String nick,
                                                    @RequestParam(value = "commentText", required = false, defaultValue = "") String commentText) throws LibraryError {
        Comment comment = service.modifyComment(bookId, commentId, commentText, nick);
        return ResponseEntity.ok().body(CommentDto.toDto(comment));
    }

    @DeleteMapping("/api/books/{bookId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("bookId") Long bookId,
                                                @PathVariable("commentId") Long commentId) throws LibraryError {
        service.deleteComment(bookId, commentId);
        return ResponseEntity.ok().body("Deleted");
    }
}


