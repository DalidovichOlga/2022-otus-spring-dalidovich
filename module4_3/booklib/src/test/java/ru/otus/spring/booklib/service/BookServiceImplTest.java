package ru.otus.spring.booklib.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.dto.AuthorFioDto;
import ru.otus.spring.booklib.dto.BookAddDto;
import ru.otus.spring.booklib.dto.BookDto;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    private BookService service;
    @Autowired
    private AuthorService authorService;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("Создать Автора и его книгу")
    void shouldCreateSomeBooks() {
        AuthorFioDto author = new AuthorFioDto(0,"Автор", "Новиков", "Сочинитель");
        assertDoesNotThrow(() -> authorService.createAuthor(author).getId());

        assertDoesNotThrow(() -> service.createBook(new BookAddDto("Новая интересная тестовая книга 1",
                "Новиков Автор Сочинитель",
                "Роман")));
        assertDoesNotThrow(() -> service.createBook(new BookAddDto("Новая интересная тестовая книга 3",
                "Новиков А.С.",
                "Роман")));

        assertThrows(LibraryError.class, () -> service.createBook(new BookAddDto("Еще новая тестовая книга 1",
                "Новиков757567 А.С.",
                "Роман")));
        assertDoesNotThrow(() -> service.createBook(new BookAddDto("Еще новая тестовая книга 1",
                "Новиков Автор Сочинитель",
                "Роман")));
        assertDoesNotThrow(() -> service.createBook(new BookAddDto("Еще новая тестовая книга 1",
                "Стариков Автор Сочинитель",
                "Роман")));

        List<Book> allBook = service.getAllBook();
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
        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }


    }

    @Test
    @DisplayName("Создать книгу а затем ее удалить. Проверить , что автор тоже удалится.")
    void shouldCreateDeleteBooks() {
        Book book1 = null;
        try {
            book1 = service.createBook(new BookAddDto("Книга первая ИИИ",
                    "Игнатов Игнат Игнатьевич",
                    "Povecnm"));
        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }
        assertTrue(book1 != null);
        assertThat(book1.getId()).isGreaterThan(0L);

        Book book2 = null;
        try {
            book2 = service.createBook(new BookAddDto("Книга вторая ИИИ",
                    "Игнатов Игнат Игнатьевич",
                    "Роман"));
        } catch (LibraryError bookError) {
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
        AuthorFioDto author = new AuthorFioDto(0,"Автор", "Новикович", "Сочинитель");
        assertDoesNotThrow(() -> authorService.createAuthor(author));
        AuthorFioDto author2 = new AuthorFioDto(0,"Автор", "Новикин", "Сочинитель");
        assertDoesNotThrow(() -> authorService.createAuthor(author2));

        Book book = null;
        try {
            book = service.createBook(new BookAddDto("Интересная тестовая книга 1", "Новикович Автор Сочинитель", "Роман"));
        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }
        assertThat(book).isNotNull();

        Long bookId = book.getId();

        assertDoesNotThrow(() -> service.modifyBook(new BookDto(bookId, "НАЗВАНИЕ 3333", "Новикин Автор Сочинитель", "Повесть")));

        List<Book> allBook = service.getAllBook();

        assertThat(allBook.stream().filter(
                (b) -> b.getTitle().startsWith("НАЗВАНИЕ 3333") &&
                        b.getAuthor().getShortName().equals("Новикин А.С.") && b.getGenre().getGenreName().equals("Повесть")).count()).isEqualTo(1);

        assertThrows(LibraryError.class, () -> service.modifyBook(new BookDto(bookId, "", "Нов1икин", "Повесть")));

        try {
            List<Book> truBook = service.getBookByAuthor(0L, "Новикин А.С.");
            assertThat(truBook.stream().filter(
                    (b) -> b.getTitle().startsWith("НАЗВАНИЕ 3333") &&
                            b.getAuthor().getShortName().equals("Новикин А.С.") && b.getGenre().getGenreName().equals("Повесть")).count()).isEqualTo(1);
        } catch (LibraryError bookError) {
            bookError.printStackTrace();
        }

    }

    @Test
    @DisplayName("Создать и удалить автора")
    void createDeleteAuthor() {
        List<Author> allAuthor = authorService.getAllAuthor();
        assertThat(allAuthor.stream().filter((g) -> g.getFullName().equals("88877666 77765 77777")).count()).isEqualTo(0);
        assertDoesNotThrow(() -> authorService.createAuthor(new AuthorFioDto(0,"77765", "88877666", "77777")));
        allAuthor = authorService.getAllAuthor();
        assertThat(allAuthor.stream().filter((g) -> g.getFullName().equals("88877666 77765 77777")).count()).isEqualTo(1);
        assertThat(allAuthor.stream().filter((g) -> g.getShortName().equals("88877666 7.7.")).count()).isEqualTo(1);
        Author id = allAuthor.stream().filter((g) -> g.getShortName().equals("88877666 7.7.")).findAny().get();
        assertDoesNotThrow(() -> authorService.deleteAuthor(id.getId()));
        allAuthor = authorService.getAllAuthor();
        assertThat(allAuthor.stream().filter((g) -> g.getFullName().equals("88877666 77765 77777")).count()).isEqualTo(0);

    }


}