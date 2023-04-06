package ru.otus.spring.booklib.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.booklib.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test для работы с таблицей авторов")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {
    @Autowired
    private AuthorDaoJdbc dao;

    @DisplayName("Добавить нового автора и проверить что работаю методы поиска по имени и идентификатору")
    @Test
    void shouldCreateNewAuthor() {
        Author author = new Author("Горький", "Алексей", "Максимович");
        List<Author> authorList = dao.getAuthorByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isTrue();
        author = dao.insert(author);
        assertThat(author.getId()).isGreaterThan(0L);
        authorList = dao.getAuthorByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isFalse();
        Author authorById = dao.getById(author.getId());
        assertThat(authorById).isNotNull();
        authorList = dao.getAuthorsByName("Горький Алексей Максимович");
        assertThat(authorList.isEmpty()).isFalse();
    }

    @DisplayName("проверить метод получения списка")
    @Test
    void shouldReturnAll() {
        Author author = new Author("Иванов", "Иван ", "Иванович");

        List<Author> authorList = dao.getAll();
        int elementСount = authorList.size();
        dao.insert(author);
        List<Author> authorList2 = dao.getAll();

        assertThat(authorList2.size()).isGreaterThan(elementСount);

    }

    @DisplayName("Добавить нового автора удалить его")
    @Test
    void shouldDeleteNewAuthor() {
        Author author = new Author("Петров", "Петр", "Петрович");
        List<Author> authorList = dao.getAuthorByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isTrue();
        author = dao.insert(author);
        assertThat(author.getId()).isGreaterThan(0L);
        authorList = dao.getAuthorByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isFalse();
        dao.deleteById(author.getId());
        authorList = dao.getAuthorByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isTrue();
        Author authorById = dao.getById(author.getId());
        assertThat(authorById).isNull();
    }
}