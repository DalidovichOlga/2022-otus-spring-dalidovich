package ru.otus.spring.booklib.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.booklib.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test для работы с таблицей жанров")
@DataJpaTest
@Import(GenreRepositoryJpaImpl.class)
class GenreDaoJdbcTest {

    @Autowired
    private GenreRepositoryJpaImpl genreRepositoryJpa;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Создать жанр, если его нет")
    @Test
    void shouldCreateNewGenre() {
        Genre ganre = new Genre("Учебники");
        List<Genre> genreList = genreRepositoryJpa.getGenreByName("Учебники");
        assertThat(genreList.isEmpty()).isTrue();
        ganre = genreRepositoryJpa.insert(ganre);
        assertThat(ganre.getId()).isGreaterThan(0L);
        genreList = genreRepositoryJpa.getGenreByName("Учебники");
        assertThat(genreList.isEmpty()).isFalse();
        Optional<Genre> genreById = genreRepositoryJpa.getById(ganre.getId());
        assertThat(genreById.isEmpty()).isFalse();
    }

    @DisplayName("Создать жанр, если его нет")
    @Test
    void shouldCreateandUpdateGenre() {
        Genre ganre = new Genre("СправАчники");
        List<Genre> genreList = genreRepositoryJpa.getGenreByName("СправАчники");
        assertThat(genreList.isEmpty()).isTrue();
        ganre = genreRepositoryJpa.insert(ganre);
        assertThat(ganre.getId()).isGreaterThan(0L);
        genreRepositoryJpa.update(ganre.getId(), "Справочники");
        em.detach(ganre);
        Optional<Genre> genre2 = genreRepositoryJpa.getById(ganre.getId());
        assertThat(genre2.get().getGenreName()).isEqualTo("Справочники");

    }

    @DisplayName("проверить метод получения списка")
    @Test
    void shouldReturnAll() {
        Genre ganre = new Genre("Учебники которых быть не должно");

        List<Genre> genreList = genreRepositoryJpa.getAll();
        int elementСount = genreList.size();
        genreRepositoryJpa.insert(ganre);
        List<Genre> genreList2 = genreRepositoryJpa.getAll();

        assertThat(genreList2.size()).isGreaterThan(elementСount);

    }

}