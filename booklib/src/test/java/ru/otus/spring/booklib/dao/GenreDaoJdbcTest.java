package ru.otus.spring.booklib.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.booklib.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test для работы с таблицей жанров")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    @Autowired
    private GenreDaoJdbc dao;

    @DisplayName("Создать жанр, если его нет")
    @Test
    void shouldCreateNewGenre() {
        Genre ganre = new Genre("Учебники");
        List<Genre> genreList = dao.getGenreByName("Учебники");
        assertThat(genreList.isEmpty()).isTrue();
        ganre = dao.insert(ganre);
        assertThat(ganre.getId()).isGreaterThan(0L);
        genreList = dao.getGenreByName("Учебники");
        assertThat(genreList.isEmpty()).isFalse();
        Genre genreById = dao.getById(ganre.getId());
        assertThat(genreById).isNotNull();
    }

    @DisplayName("проверить метод получения списка")
    @Test
    void shouldReturnAll() {
        Genre ganre = new Genre("Учебники которых быть не должно");

        List<Genre> genreList = dao.getAll();
        int elementСount = genreList.size();
        dao.insert(ganre);
        List<Genre> genreList2 = dao.getAll();

        assertThat(genreList2.size()).isGreaterThan(elementСount);

    }

}