package ru.otus.spring.booklib.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.booklib.dao.GenreRepositoryJpa;
import ru.otus.spring.booklib.domain.Genre;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(
        username = "user",
        authorities = {"ROLE_USER"}
)
@SpringBootTest
@AutoConfigureMockMvc
public class GenreIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;

    @Test
    @DisplayName("Возвращение списка жарнов")
    void shouldReturnCorrectAuthorList() throws Exception {
        mvc.perform(get("/api/genres"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Создадим жанр и обновим его")
    void shouldUpdateAuthorList() throws Exception {
        Genre genre = new Genre("пАэзия");
        genre = genreRepositoryJpa.save(genre);

        mvc.perform(patch("/api/genres/" + String.valueOf(genre.getId()), 1).param("genreName", "поэзия"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genreName").value("поэзия"));
    }
}