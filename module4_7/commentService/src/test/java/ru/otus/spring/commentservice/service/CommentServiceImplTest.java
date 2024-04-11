package ru.otus.spring.commentservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.commentservice.domain.Comment;
import ru.otus.spring.commentservice.dto.BookDto;
import ru.otus.spring.commentservice.dto.CommentAddDto;
import ru.otus.spring.commentservice.error.LibraryError;
import ru.otus.spring.commentservice.service.BookService;
import ru.otus.spring.commentservice.service.CommentService;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class CommentServiceImplTest {
    @Autowired
    private CommentService commentService;

    @MockBean
    private BookService service;

    @Test
    @DisplayName("Добавить книгу и комментарии к ней")
    void createBookWithComment() {
        BookDto book = new BookDto(100L, "тестовая книга c комментариями", "Новикович Автор Сочинитель", "Роман");

        try {
            given(service.getById(100L)).willReturn(book);
        } catch (LibraryError libraryError) {
            libraryError.printStackTrace();
        }

        Long bookId = book.getId();

        assertDoesNotThrow(() -> commentService.commentBook(bookId, new CommentAddDto("Вася", "Книга не понравилась, слишком много букв")));
        assertDoesNotThrow(() -> commentService.commentBook(bookId, new CommentAddDto("Петя", "Очень интересная книга. Автор пиши еще.")));

        List<Comment> lstComment = new ArrayList<>();
        try {
            lstComment = commentService.getComment(bookId);

        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }
        assertThat(lstComment.size()).isEqualTo(2);

    }


    @Test
    @DisplayName("прокоментировать книгу и удалить комментарии к ней")
    void commentBookAndDeleteComment() {
        Long bookId = 5L;
        BookDto book = new BookDto(5L, "тестовая книга c комментариями", "Новикович Автор Сочинитель", "Роман");

        try {
            given(service.getById(5L)).willReturn(book);
        } catch (LibraryError libraryError) {
            libraryError.printStackTrace();
        }

        assertDoesNotThrow(() -> commentService.commentBook(bookId, new CommentAddDto("Вася", "Книга не понравилась, слишком много букв")));
        assertDoesNotThrow(() -> commentService.commentBook(bookId, new CommentAddDto("Петя", "Очень интересная книга. Автор пиши еще.")));

        List<Comment> lstComment = new ArrayList<>();
        try {
            lstComment = commentService.getComment(bookId);

        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }
        assertThat(lstComment.size()).isEqualTo(2);
        Long commentId1 = lstComment.get(0).getId();
        assertDoesNotThrow(() -> commentService.deleteComment(bookId, commentId1));
        Long commentId2 = lstComment.get(1).getId();

        assertDoesNotThrow(() -> commentService.modifyComment(bookId, commentId2,"новый отзыв", ""));

        try {
            lstComment = commentService.getComment(bookId);

        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }
        assertThat(lstComment.size()).isEqualTo(1);
        assertThat(lstComment.get(0).getNick()).isEqualTo("Петя");
        assertThat(lstComment.get(0).getCommentText()).isEqualTo("новый отзыв");

        assertThrows(LibraryError.class, () -> commentService.deleteComment(bookId, commentId1));

    }
}
