package ru.otus.spring.booklib.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.error.BookError;

import java.util.List;

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
        Book book = new Book("Новая интересная тестовая книга 1", "Год", "Издательство", 0, 0);
        assertDoesNotThrow(() -> service.createBookWithAuthorName(book, "Новиков Автор Сочинитель", "Роман"));
        Book book2 = new Book("Новая интересная тестовая книга 3", "Год", "Издательство", 0, 0);
        assertDoesNotThrow(() -> service.createBookWithAuthorName(book2, "Новиков А.С.", "Роман"));
        Book book3 = new Book("Еще новая тестовая книга 1", "Год", "Издательство", 0, 0);
        assertThrows(BookError.class, () -> service.createBookWithAuthorName(book3, "Новиков757567 А.С.", "Роман"));
        Book book4 = new Book("Еще новая тестовая книга 1", "Год", "Издательство", 0, 0);
        assertThrows(BookError.class, () -> service.createBookWithAuthorName(book4, "Новиков А.С.", "8888"));

        assertThrows(BookError.class, () -> service.createBookWithAuthorName(book, "Новиков Автор Сочинитель", "Роман"));

        List<BookView> allBook = service.getAllBook();
        assertThat(allBook.stream().filter((b) -> b.getTitle().startsWith("Новая интересная тестовая книга")).count()).isEqualTo(2);
        assertThrows(BookError.class, () -> service.deleteBook(-111L));
        assertDoesNotThrow(() -> service.deleteBook(book.getId()));

    }

    @Test
    @DisplayName("Проверить изменение книги")
    void modifyBook() {
        Author author = new Author("Автор", "Новикович", "Сочинитель");
        assertDoesNotThrow(() -> authorService.createAuthor(author));
        Author author2 = new Author("Автор", "Новикин", "Сочинитель");
        assertDoesNotThrow(() -> authorService.createAuthor(author2));

        Book book = new Book("Интересная тестовая книга 1", "Год", "Издательство", 0, 0);
        assertDoesNotThrow(() -> service.createBookWithAuthorName(book, "Новикович Автор Сочинитель", "Роман"));
        List<BookView> allBook = service.getAllBook();

        book.setAuthorId(0);
        book.setGenreId(0);
        book.setTitle("НАЗВАНИЕ 3333");

        assertDoesNotThrow(() -> service.modifyBook(book, "Новикин Автор Сочинитель", "Повесть"));

        allBook = service.getAllBook();

        assertThat(allBook.stream().filter(
                (b) -> b.getTitle().startsWith("НАЗВАНИЕ 3333") &&
                        b.getAuthor().equals("Новикин А.С.") && b.getGenre().equals("Повесть")).count()).isEqualTo(1);

        book.setAuthorId(0);
        assertThrows(BookError.class, () -> service.modifyBook(book, "Нов1икин Автор Сочинитель", "Повесть"));

        try {
            allBook = service.getBookByAuthor(0L, "Новикин А.С.");
            assertThat(allBook.stream().filter(
                    (b) -> b.getTitle().startsWith("НАЗВАНИЕ 3333") &&
                            b.getAuthor().equals("Новикин А.С.") && b.getGenre().equals("Повесть")).count()).isEqualTo(1);
        } catch (BookError bookError) {
            bookError.printStackTrace();
        }

    }

    @Test
    @DisplayName("Создать книгу указав идентификатор автора")
    void createBookWithAuthorId() {
        Book book = new Book("2 Новая интересная тестовая книга 2", "Год", "Издательство", 1, 0);
        assertDoesNotThrow(() -> service.createBook(book, "Роман"));
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