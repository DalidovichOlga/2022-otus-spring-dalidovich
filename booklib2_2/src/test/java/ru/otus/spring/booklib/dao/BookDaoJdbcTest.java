package ru.otus.spring.booklib.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test для работы с таблицей книг")
@DataJpaTest
@Import({BookRepositoryJpaImpl.class})
class BookDaoJdbcTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookRepositoryJpaImpl bookRepositoryJpa;

    private Author author;
    private Genre genre;
    private Genre genre_2;

    @BeforeEach
    void setUp() {
        author = new Author("Сергеев778", "Сергей1", "Сергеевич1");
        author = em.persist(author);
        genre = em.find(Genre.class, 1L);
        genre_2 = em.find(Genre.class, 2L);
    }

    @DisplayName("Сохранить книгу")
    @Test
    void shouldCreateNewBook() {
        assertThat(author.getId()).isGreaterThan(0L);
        List<Book> allbyAuthor = bookRepositoryJpa.getAllBookByAuthor(author.getId());
        int count = allbyAuthor.size();
        Book book = new Book("Новая книга 19922", author, genre);
        book = bookRepositoryJpa.insert(book);
        assertThat(book.getId()).isGreaterThan(0L);
        allbyAuthor = bookRepositoryJpa.getAllBookByAuthor(author.getId());
        assertThat(allbyAuthor.size()).isGreaterThan(count);
    }

    @DisplayName("Сохранить книгу, изменить и удалить книгу")
    @Test
    void shouldModifyNewBook() {
        Book book = new Book("Еще одна книга Новая книга 777722", author, genre);
        book = bookRepositoryJpa.insert(book);
        assertThat(book.getId()).isGreaterThan(0L);
        book.setTitle("новое название книги 7778");
        book.setGenre(genre_2);
        bookRepositoryJpa.update(book);
        Optional<Book> bw = bookRepositoryJpa.getById(book.getId());
        assertThat(bw.get().getTitle()).isEqualTo("новое название книги 7778");
        assertThat(bw.get().getAuthor().getId()).isEqualTo(author.getId());
        assertThat(bw.get().getGenre().getId()).isEqualTo(2L);
        bookRepositoryJpa.deleteById(book.getId());
        em.detach(book);
        assertThat(bookRepositoryJpa.getById(book.getId()).isEmpty()).isTrue();


    }

    @DisplayName("найти книгу по автору ")
    @Test
    void shouldFindBook() {
        List<Book> allby = bookRepositoryJpa.getAllBookByAuthor(author.getId());
        int count = allby.size();
        Book book = new Book("Третья тестовая Новая книга 777722", author, genre);
        book = bookRepositoryJpa.insert(book);
        allby = bookRepositoryJpa.getAllBookByAuthor(author.getId());
        assertThat(allby.size()).isGreaterThan(count);
        assertThat(allby).contains(book);
    }

}