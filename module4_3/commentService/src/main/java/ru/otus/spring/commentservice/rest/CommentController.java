package ru.otus.spring.commentservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.commentservice.domain.Comment;
import ru.otus.spring.commentservice.dto.BookDto;
import ru.otus.spring.commentservice.dto.BookWithCommentsDto;
import ru.otus.spring.commentservice.dto.CommentAddDto;
import ru.otus.spring.commentservice.dto.CommentDto;
import ru.otus.spring.commentservice.error.LibraryError;
import ru.otus.spring.commentservice.service.BookService;
import ru.otus.spring.commentservice.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class CommentController {
    private final CommentService service;
    private final BookService bookService;

    @GetMapping("/{id}/comments")
    public ResponseEntity<BookWithCommentsDto> getCommentsById(@PathVariable("id") long id) throws LibraryError {
        BookDto book = bookService.getById(id);
        List<Comment> comments = service.getComment(id);

        List<CommentDto> dtoList = comments.stream().map(t -> CommentDto.toDto(t)).
                collect(Collectors.toList());
        return ResponseEntity.ok().body(new BookWithCommentsDto(book, dtoList));

    }

    @PostMapping("/{bookId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable("bookId") long bookId, @RequestBody CommentAddDto dto) throws LibraryError {
        Comment comment = service.commentBook(bookId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommentDto.toDto(comment));
    }

    @PatchMapping("/{bookId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("bookId") Long bookId,
                                                    @PathVariable("commentId") Long commentId,
                                                    @RequestParam(value = "nick", required = false, defaultValue = "") String nick,
                                                    @RequestParam(value = "commentText", required = false, defaultValue = "") String commentText) throws LibraryError {
        Comment comment = service.modifyComment(bookId, commentId, commentText, nick);
        return ResponseEntity.ok().body(CommentDto.toDto(comment));
    }

    @DeleteMapping("/{bookId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("bookId") Long bookId,
                                                @PathVariable("commentId") Long commentId) throws LibraryError {
        service.deleteComment(bookId, commentId);
        return ResponseEntity.ok().body("Deleted");
    }

    @DeleteMapping("/comment/{bookId}")
    public ResponseEntity<String> deleteCommentByBook(@PathVariable("bookId") Long bookId                                              ) throws LibraryError {
        service.deleteAllComment(bookId);
        return ResponseEntity.ok().body("Deleted");
    }
}


