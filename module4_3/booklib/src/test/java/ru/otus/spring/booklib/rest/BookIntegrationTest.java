package ru.otus.spring.booklib.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.spring.booklib.dto.BookAddDto;
import ru.otus.spring.booklib.dto.BookDto;
import ru.otus.spring.booklib.service.CommentService;


import java.nio.charset.Charset;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @WithMockUser(
            username = "testuser",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Проверить возвращение одной книги, если она есть")
    void shouldReturnBookById() throws Exception {
        BookDto book = new BookDto(1L, "Капитанская дочка", "Пушкин А.С.", "Повесть");
        mvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));

        mvc.perform(get("/api/books/25"))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Проверить что без авторизации книга не вернется")
    void shouldNotReturnBookById() throws Exception {
        BookDto book = new BookDto(1L, "Капитанская дочка", "Пушкин А.С.", "Повесть");
        mvc.perform(get("/api/books/1"))
                .andExpect(status().isForbidden())
                ;

        mvc.perform(get("/api/books/25"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "testuser",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Проверить возвращение книг, по автору")
    void shouldReturnBookByAuthor() throws Exception {
        BookDto book = new BookDto(1L, "Капитанская дочка", "Пушкин А.С.", "Повесть");
        mvc.perform(get("/api/books/author/Пушкин А.С."))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(book))));

    }


    @WithMockUser(
            username = "testuser",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Проверить вызова АПИ создания книги")
    void shouldCorrectCreateNewBook() throws Exception {
        BookAddDto bookDto = new BookAddDto("New Book 123", "Петров Петр Петрович", "новый");
        String expectedResult = mapper.writeValueAsString(bookDto);

        MvcResult result = mvc.perform(post("/api/books").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andReturn();


        String string = result.getResponse().getContentAsString(UTF_8);
        BookDto resultDto = mapper.readValue(string, BookDto.class);
        assertThat(resultDto.getTitle().equals("New Book 123")).isEqualTo(true);
        assertThat(resultDto.getAuthor()).isEqualTo("Петров П.П.");
    }

    @WithMockUser(
            username = "testuser",
            authorities = {"ROLE_ANONYMOUS"}
    )
    @Test
    @DisplayName("Проверить вызова АПИ создания книги пользователем без прав")
    void shouldNotCorrectCreateNewBook() throws Exception {
        BookAddDto bookDto = new BookAddDto("New Book 123", "Петров Петр Петрович", "новый");
        String expectedResult = mapper.writeValueAsString(bookDto);

        MvcResult result = mvc.perform(post("/api/books").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @WithMockUser(
            username = "testuser",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Проверить вызова АПИ изменения книги")
    void shouldCorrectUpdateBookName() throws Exception {
        BookAddDto bookDto = new BookAddDto("Новая Книга Для Изменения", "Vasilev Vasiliy Vasilevich", "новый");
        String expectedResult = mapper.writeValueAsString(bookDto);

        MvcResult result = mvc.perform(post("/api/books").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andReturn();

        String stringJson = result.getResponse().getContentAsString(UTF_8);
        BookDto resultDto = mapper.readValue(stringJson, BookDto.class);

        mvc.perform(patch("/api/books/" + String.valueOf(resultDto.getId()), 1).param("title", "BOOK NUMBER 2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("BOOK NUMBER 2"))
                .andExpect(jsonPath("$.author").value("Vasilev V.V."));
    }

    @WithMockUser(
            username = "testuser",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Проверить вызова АПИ удаления книги")
    void shouldCorrectDeleteBook() throws Exception {
        BookAddDto bookDto = new BookAddDto("Новая Книга Для удаления", "Петров Петр Петрович", "новый");
        String expectedResult = mapper.writeValueAsString(bookDto);

        MvcResult result = mvc.perform(post("/api/books").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andReturn();

        String string = result.getResponse().getContentAsString(Charset.defaultCharset());
        BookDto resultDto = mapper.readValue(string, BookDto.class);


        mvc.perform(delete("/api/books/" + String.valueOf(resultDto.getId())))
                .andExpect(status().isOk());
        mvc.perform(get("/api/books/" + String.valueOf(resultDto.getId())))
                .andExpect(status().isBadRequest());

    }

    @WithMockUser(
            username = "testuser",
            authorities = {"ROLE_ANONYMOUS"}
    )
    @Test
    @DisplayName("Проверить вызова АПИ удаления книги без прав на это")
    void shouldNotCorrectDeleteBook() throws Exception {

        mvc.perform(delete("/api/books/1" ))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/books/2000" ))
                .andExpect(status().isForbidden());

    }

    @WithMockUser(
            username = "testuser",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Возвращение всего списка книг")
    void shouldReturnCorrectBookList() throws Exception {
        mvc.perform(get("/api/books"))
                .andExpect(status().isOk());

    }
}
