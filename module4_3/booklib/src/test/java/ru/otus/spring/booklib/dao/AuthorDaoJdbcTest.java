package ru.otus.spring.booklib.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.booklib.domain.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test для работы с таблицей авторов")
@DataJpaTest
class AuthorDaoJdbcTest {
    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("Добавить нового автора и проверить что работаю методы поиска по имени и идентификатору")
    @Test
    void shouldCreateNewAuthor() {
        Author author = new Author("Горький", "Алексей", "Максимович");
        List<Author> authorList = authorRepository.findByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isTrue();
        author = authorRepository.save(author);
        assertThat(author.getId()).isGreaterThan(0L);
        authorList = authorRepository.findByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isFalse();
        Optional<Author> authorById = authorRepository.findById(author.getId());
        assertThat(authorById.isEmpty()).isFalse();
    }

    @DisplayName("проверить метод получения списка")
    @Test
    void shouldReturnAll() {
        Author author = new Author("Иванов", "Иван ", "Иванович");

        List<Author> authorList = authorRepository.findAll();
        int elementСount = authorList.size();
        authorRepository.save(author);
        List<Author> authorList2 = authorRepository.findAll();

        assertThat(authorList2.size()).isGreaterThan(elementСount);
    }

    @DisplayName("Добавить нового автора удалить его")
    @Test
    void shouldDeleteNewAuthor() {
        Author author = new Author("Петров", "Петр", "Петрович");
        List<Author> authorList = authorRepository.findByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isTrue();
        author = authorRepository.save(author);
        assertThat(author.getId()).isGreaterThan(0L);
        authorList = authorRepository.findByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isFalse();
        authorRepository.deleteById(author.getId());
        authorList = authorRepository.findByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isTrue();
        Optional<Author> authorById = authorRepository.findById(author.getId());
        assertThat(authorById.isEmpty()).isTrue();
    }

    @DisplayName("Добавить нового автора и изменить его")
    @Test
    void shouldUpdateNewAuthor() {
        Author author = new Author("Василий", "Васильев", "Васильевич");
        List<Author> authorList = authorRepository.findByShortName(author.getShortName());
        assertThat(authorList.isEmpty()).isTrue();
        author = authorRepository.save(author);
        author.setLastName("Петров");
        author.setShortName("Петров В.В.");
        authorRepository.save(author);
        Optional<Author> authorById = authorRepository.findById(author.getId());
        assertThat(authorById.get().getShortName()).isEqualTo("Петров В.В.");
        assertThat(authorById.get().getFullName()).isEqualTo("Петров Василий Васильевич");
    }


}