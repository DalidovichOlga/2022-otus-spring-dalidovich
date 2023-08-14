package ru.otus.spring.booklib.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.booklib.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenreServiceImplTest {
    @Autowired
    private GenreServiceImpl genreService;

    @Test
    @DisplayName("Создать и удалить жанр")
    void createDeleteGenre() {
        List<Genre> allGaner = genreService.getAllGaner();
        assertThat(allGaner.stream().filter((g) -> g.getGenreName().equals("88877666")).count()).isEqualTo(0);
        assertDoesNotThrow(() -> genreService.createGenre(new Genre("88877666")));
        allGaner = genreService.getAllGaner();
        assertThat(allGaner.stream().filter((g) -> g.getGenreName().equals("88877666")).count()).isEqualTo(1);
        Genre id = allGaner.stream().filter((g) -> g.getGenreName().equals("88877666")).findAny().get();
        assertDoesNotThrow(() -> genreService.deleteGenre(id));
        allGaner = genreService.getAllGaner();
        assertThat(allGaner.stream().filter((g) -> g.getGenreName().equals("88877666")).count()).isEqualTo(0);

    }

}