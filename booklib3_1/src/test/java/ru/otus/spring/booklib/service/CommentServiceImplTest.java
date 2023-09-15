package ru.otus.spring.booklib.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.LibraryError;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CommentServiceImplTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BookService service;


    @Test
    @DisplayName("Добавить книгу и комментарии к ней")
    void createBookWithComment() {
        Book book = null;
        try {
            book = service.createBook("тестовая книга c комментариями", "Новикович Автор Сочинитель", "Роман"
                    , 0L, 0L);
        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }
        assertThat(book).isNotNull();

        Long bookId = book.getId();

        assertDoesNotThrow(() -> commentService.commentBook(bookId, "Вася", "Книга не понравилась, слишком много букв"));
        assertDoesNotThrow(() -> commentService.commentBook(bookId, "Петя", "Очень интересная книга. Автор пиши еще."));

        List<Comment> lstComment = new ArrayList<>();
        try {
            lstComment = commentService.getComment(bookId);

        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }
        assertThat(lstComment.size()).isEqualTo(2);

        assertDoesNotThrow(() -> service.removeBook(bookId));

    }


    @Test
    @DisplayName("прокоментировать книгу и удалить комментарии к ней")
    void commentBookAndDeleteComment() {
        Long bookId = 5L;

        assertDoesNotThrow(() -> commentService.commentBook(bookId, "Вася", "Книга не понравилась, слишком много букв"));
        assertDoesNotThrow(() -> commentService.commentBook(bookId, "Петя", "Очень интересная книга. Автор пиши еще."));

        List<Comment> lstComment = new ArrayList<>();
        try {
            lstComment = commentService.getComment(bookId);

        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }
        assertThat(lstComment.size()).isEqualTo(2);
        assertDoesNotThrow(() -> commentService.deleteComment(bookId, 1));

        assertDoesNotThrow(() -> commentService.modifyComment(bookId, 1,"новый отзыв", ""));

        try {
            lstComment = commentService.getComment(bookId);

        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }
        assertThat(lstComment.size()).isEqualTo(1);
        assertThat(lstComment.get(0).getNick()).isEqualTo("Петя");
        assertThat(lstComment.get(0).getCommentText()).isEqualTo("новый отзыв");

        assertThrows(LibraryError.class, () -> commentService.deleteComment(bookId, 3));

    }
}
