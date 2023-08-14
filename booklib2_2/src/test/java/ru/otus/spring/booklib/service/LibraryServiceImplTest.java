package ru.otus.spring.booklib.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.booklib.dao.CommentRepositoryJpa;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.BookError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class LibraryServiceImplTest {

    @Autowired
    private LibraryService service;
    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("Создать Автора и его книгу")
    void shouldCreateSomeBooks() {
        Author author = new Author("Автор", "Новиков", "Сочинитель");
        assertDoesNotThrow(() -> authorService.createAuthor(author).getId());

        assertDoesNotThrow(() -> service.createBook("Новая интересная тестовая книга 1",
                "Новиков Автор Сочинитель",
                "Роман", 0L, 0L));
        assertDoesNotThrow(() -> service.createBook("Новая интересная тестовая книга 3",
                "Новиков А.С.",
                "Роман", 0L, 0L));

        assertThrows(BookError.class, () -> service.createBook("Еще новая тестовая книга 1",
                "Новиков757567 А.С.",
                "Роман", 0L, 0L));
        assertDoesNotThrow(() -> service.createBook("Еще новая тестовая книга 1",
                "Новиков Автор Сочинитель",
                "Роман", 0L, 0L));
        assertDoesNotThrow(() -> service.createBook("Еще новая тестовая книга 1",
                "Стариков Автор Сочинитель",
                "Роман", 0L, 0L));

        List<BookView> allBook = service.getAllBook();
        assertThat(allBook.stream().filter((b) -> b.getTitle().startsWith("Новая интересная тестовая книга")).count()).isEqualTo(2);
        long idbook = allBook.stream().filter((b) -> b.getTitle().startsWith("Новая интересная тестовая книга 1")).findFirst().get().getId();
        assertDoesNotThrow(() -> service.removeBook(idbook));
        assertThat(authorService.getAllAuthor().stream().filter(f -> f.getFullName().equals("Новиков Автор Сочинитель")).count()
        ).isEqualTo(1L);
        long idbook2 = allBook.stream().filter((b) -> b.getTitle().startsWith("Новая интересная тестовая книга 3")).findFirst().get().getId();
        try {
            Book bb = service.getById(idbook2);
            Author a = bb.getAuthor();
            assertThat(a).isNotNull();
        } catch (BookError bookError) {
            bookError.printStackTrace();
        }


    }

    @Test
    @DisplayName("Создать книгу а затем ее удалить. Проверить , что автор тоже удалится.")
    void shouldCreateDeleteBooks() {
        Book book1 = null;
        try {
            book1 = service.createBook("Книга первая ИИИ",
                    "Игнатов Игнат Игнатьевич",
                    "Povecnm", 0L, 0L);
        } catch (BookError bookError) {
            bookError.printStackTrace();
        }
        assertTrue(book1 != null);
        assertThat(book1.getId()).isGreaterThan(0L);

        Book book2 = null;
        try {
            book2 = service.createBook("Книга вторая ИИИ",
                    "Игнатов Игнат Игнатьевич",
                    "Роман", 0L, 0L);
        } catch (BookError bookError) {
            bookError.printStackTrace();
        }
        assertThat(book2.getId()).isGreaterThan(0L);

        Long idbook1 = book1.getId();
        Long idbook2 = book2.getId();
        assertDoesNotThrow(() ->
                service.removeBook(idbook1));
        assertThat(authorService.getAllAuthor().stream().filter(f -> f.getFullName().equals("Игнатов Игнат Игнатьевич")).count()
        ).isEqualTo(1L);
        assertDoesNotThrow(() ->
                service.removeBook(idbook2));

        assertThat(authorService.getAllAuthor().stream().filter(f -> f.getFullName().equals("Игнатов Игнат Игнатьевич")).count()
        ).isEqualTo(0L);

    }

    @Test
    @DisplayName("Проверить изменение книги")
    void modifyBook() {
        Author author = new Author("Автор", "Новикович", "Сочинитель");
        assertDoesNotThrow(() -> authorService.createAuthor(author));
        Author author2 = new Author("Автор", "Новикин", "Сочинитель");
        assertDoesNotThrow(() -> authorService.createAuthor(author2));

        Book book = null;
        try {
            book = service.createBook("Интересная тестовая книга 1", "Новикович Автор Сочинитель", "Роман"
                    , 0L, 0L);
        } catch (BookError bookError) {
            bookError.printStackTrace();
        }
        assertThat(book).isNotNull();

        Long bookId = book.getId();

        assertDoesNotThrow(() -> service.modifyBook(bookId, "НАЗВАНИЕ 3333", "Новикин Автор Сочинитель", "Повесть", 0L, 0L));

        List<BookView> allBook = service.getAllBook();

        assertThat(allBook.stream().filter(
                (b) -> b.getTitle().startsWith("НАЗВАНИЕ 3333") &&
                        b.getAuthor().equals("Новикин А.С.") && b.getGenre().equals("Повесть")).count()).isEqualTo(1);

        assertThrows(BookError.class, () -> service.modifyBook(bookId, "", "Нов1икин Автор Сочинитель", "Повесть", 0L, 0L));

        try {
            List<Book> truBook = service.getBookByAuthor(0L, "Новикин А.С.");
            assertThat(truBook.stream().filter(
                    (b) -> b.getTitle().startsWith("НАЗВАНИЕ 3333") &&
                            b.getAuthor().getShortName().equals("Новикин А.С.") && b.getGenre().getGenreName().equals("Повесть")).count()).isEqualTo(1);
        } catch (BookError bookError) {
            bookError.printStackTrace();
        }

    }

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

        assertDoesNotThrow(() -> service.commentBook(bookId, "Вася", "Книга не понравилась, слишком много букв"));
        assertDoesNotThrow(() -> service.commentBook(bookId, "Петя", "Очень интересная книга. Автор пиши еще."));

        List<Comment> lstComment = new ArrayList<>();
        try {
            lstComment = service.getComment(bookId);

        } catch (BookError bookError) {
            bookError.printStackTrace();
        }
        assertThat(lstComment.size()).isEqualTo(2);

        assertDoesNotThrow(() -> service.removeBook(bookId));

    }

    @Test
    @DisplayName("Создать книгу, указав идентификатор автора")
    void createBookWithAuthorId() {
        assertDoesNotThrow(() -> service.createBook(
                "2 Новая интересная тестовая книга 2", "", "Роман", 1L, 0L));
        List<BookView> allBook = service.getAllBook();
        assertThat(allBook.stream().filter((b) -> b.getTitle().startsWith("2 Новая интересная тестовая книга")).count()).isEqualTo(1);
    }


    @Test
    @DisplayName("Создать и удалить автора")
    void createDeleteAuthor() {
        List<Author> allAuthor = authorService.getAllAuthor();
        assertThat(allAuthor.stream().filter((g) -> g.getFullName().equals("88877666 77765 77777")).count()).isEqualTo(0);
        assertDoesNotThrow(() -> authorService.createAuthor(new Author("77765", "88877666", "77777")));
        allAuthor = authorService.getAllAuthor();
        assertThat(allAuthor.stream().filter((g) -> g.getFullName().equals("88877666 77765 77777")).count()).isEqualTo(1);
        assertThat(allAuthor.stream().filter((g) -> g.getShortName().equals("88877666 7.7.")).count()).isEqualTo(1);
        Author id = allAuthor.stream().filter((g) -> g.getShortName().equals("88877666 7.7.")).findAny().get();
        assertDoesNotThrow(() -> authorService.deleteAuthor(id.getId(), ""));
        allAuthor = authorService.getAllAuthor();
        assertThat(allAuthor.stream().filter((g) -> g.getFullName().equals("88877666 77765 77777")).count()).isEqualTo(0);

    }


}