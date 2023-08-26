package ru.otus.spring.booklib.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.BookError;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
        } catch (BookError bookError) {
            bookError.printStackTrace();
        }
        assertThat(book).isNotNull();

        Long bookId = book.getId();

        assertDoesNotThrow(() -> commentService.commentBook(bookId, "Вася", "Книга не понравилась, слишком много букв"));
        assertDoesNotThrow(() -> commentService.commentBook(bookId, "Петя", "Очень интересная книга. Автор пиши еще."));

        List<Comment> lstComment = new ArrayList<>();
        try {
            lstComment = commentService.getComment(bookId);

        } catch (BookError bookError) {
            bookError.printStackTrace();
        }
        assertThat(lstComment.size()).isEqualTo(2);

        assertDoesNotThrow(() -> service.removeBook(bookId));

    }
}
