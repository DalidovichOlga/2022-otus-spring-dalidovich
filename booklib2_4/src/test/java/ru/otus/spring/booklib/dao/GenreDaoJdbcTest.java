package ru.otus.spring.booklib.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.booklib.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test для работы с таблицей жанров")
@DataJpaTest
class GenreDaoJdbcTest {

    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;
    @Autowired
    private TestEntityManager em;

    @DisplayName("Создать жанр, если его нет")
    @Test
    void shouldCreateNewGenre() {
        Genre ganre = new Genre("Учебники");
        List<Genre> genreList = genreRepositoryJpa.findByGenreName("Учебники");
        assertThat(genreList.isEmpty()).isTrue();
        ganre = genreRepositoryJpa.save(ganre);
        assertThat(ganre.getId()).isGreaterThan(0L);
        genreList = genreRepositoryJpa.findByGenreName("Учебники");
        assertThat(genreList.isEmpty()).isFalse();
        Optional<Genre> genreById = genreRepositoryJpa.findById(ganre.getId());
        assertThat(genreById.isEmpty()).isFalse();
    }

    @DisplayName("Создать жанр, если его нет")
    @Test
    void shouldCreateandUpdateGenre() {
        Genre ganre = new Genre("СправАчники");
        List<Genre> genreList = genreRepositoryJpa.findByGenreName("СправАчники");
        assertThat(genreList.isEmpty()).isTrue();
        ganre = genreRepositoryJpa.save(ganre);
        assertThat(ganre.getId()).isGreaterThan(0L);
        ganre.setGenreName("Справочники");
        genreRepositoryJpa.saveAndFlush(ganre) ;
        Optional<Genre> genre2 = genreRepositoryJpa.findById(ganre.getId());
        assertThat(genre2.get().getGenreName()).isEqualTo("Справочники");

    }

    @DisplayName("проверить метод получения списка")
    @Test
    void shouldReturnAll() {
        Genre ganre = new Genre("Учебники которых быть не должно");

        List<Genre> genreList = genreRepositoryJpa.findAll();
        int elementСount = genreList.size();
        genreRepositoryJpa.save(ganre);
        List<Genre> genreList2 = genreRepositoryJpa.findAll();

        assertThat(genreList2.size()).isGreaterThan(elementСount);

    }

}