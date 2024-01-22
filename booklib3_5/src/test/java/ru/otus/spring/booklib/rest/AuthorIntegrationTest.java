package ru.otus.spring.booklib.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.booklib.dao.AuthorRepositoryJpa;
import ru.otus.spring.booklib.domain.Author;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(
        username = "testuser",
        authorities = {"ROLE_USER"}
)

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Test
    @DisplayName("Возвращение списка авторов")
    void shouldReturnCorrectAuthorList() throws Exception {
        mvc.perform(get("/api/authors"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Возвращение авторa по идентификатору")
    void shouldReturnCorrectBookList() throws Exception {
        mvc.perform(get("/api/authors/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortName").value("Толстой Л.Н."))
                .andExpect(jsonPath("$.fullName").value("Толстой Лев Николаевич"));

    }


    @Test
    @DisplayName("Создадим автора и обновим его")
    void shouldUpdateAuthorList() throws Exception {
        Author author = new Author("Алексей", "Сергеев", "Сергеевич");
        author = authorRepositoryJpa.save(author);

        mvc.perform(patch("/api/authors/" + String.valueOf(author.getId()), 1).param("firstName", "Сергей"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Сергеев Сергей Сергеевич"))
                .andExpect(jsonPath("$.shortName").value("Сергеев С.С."));
    }

}
