package ru.otus.spring.booklib.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test для работы с таблицей книг")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class})
class BookDaoJdbcTest {
    @Autowired
    private AuthorDaoJdbc daoAuthor;
    @Autowired
    private BookDaoJdbc dao;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("Сергеев778", "Сергей1", "Сергеевич1");
        author = daoAuthor.insert(author);
    }

    @DisplayName("Сохранить книгу")
    @Test
    void shouldCreateNewBook() {
        assertThat(author.getId()).isGreaterThan(0L);
        List<BookView> allbyAuthor = dao.getAllBookByAuthor(author.getId());
        int count = allbyAuthor.size();
        Book book = new Book("Новая книга 19922", "3010", "44444", author.getId(), 1L);
        book = dao.insert(book);
        assertThat(book.getId()).isGreaterThan(0L);
        allbyAuthor = dao.getAllBookByAuthor(author.getId());
        assertThat(allbyAuthor.size()).isGreaterThan(count);
    }

    @DisplayName("Сохранить книгу, изменить и удалить книгу")
    @Test
    void shouldModifyNewBook() {
        Book book = new Book("Еще одна книга Новая книга 777722", "3010", "44444", author.getId(), 1L);
        book = dao.insert(book);
        assertThat(book.getId()).isGreaterThan(0L);
        book.setTitle("новое название книги 7778");
        book.setGenreId(2L);
        dao.update(book);
        BookView bw = dao.getById(book.getId());
        assertThat(bw.getTitle()).isEqualTo("новое название книги 7778");
        assertThat(bw.getAuthorId()).isEqualTo(author.getId());
        assertThat(bw.getGenreId()).isEqualTo(2L);
        dao.deleteById(book.getId());
        assertThat(dao.getById(book.getId())).isNull();
    }

    @DisplayName("найти книгу по автору , жанру , все")
    @Test
    void shouldFindBook() {
        List<BookView> allby = dao.getAllBookByAuthor(author.getId());
        int count = allby.size();
        Book book = new Book("Третья тестовая Новая книга 777722", "3010", "8888", author.getId(), 1L);
        book = dao.insert(book);
        BookView bookView = dao.getById(book.getId());
        allby = dao.getAllBookByAuthor(author.getId());
        assertThat(allby.size()).isGreaterThan(count);
        assertThat(allby).contains(bookView);
        assertThat(dao.getAllBookByGenre(1l)).containsAnyOf(bookView);
        assertThat(dao.getAll()).containsAnyOf(bookView);
        allby = dao.getAllTitleAuthorGenre("Третья тестовая Новая книга 777722", author.getId(), 1L);
        assertThat(allby).contains(bookView);
    }

}