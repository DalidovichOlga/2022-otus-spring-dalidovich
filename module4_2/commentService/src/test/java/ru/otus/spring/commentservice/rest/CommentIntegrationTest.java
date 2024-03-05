package ru.otus.spring.commentservice.rest;

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
import ru.otus.spring.commentservice.dto.BookDto;
import ru.otus.spring.commentservice.dto.CommentAddDto;
import ru.otus.spring.commentservice.dto.CommentDto;
import ru.otus.spring.commentservice.error.LibraryError;
import ru.otus.spring.commentservice.service.BookService;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(
        username = "testuser",
        authorities = {"ROLE_USER"}
)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService service;

    @Test
    @DisplayName("Проверить возвращение отзыва")
    void shouldReturnBookById() throws Exception {
        CommentAddDto comDto = new CommentAddDto("Сергей Петрович", "Это мой первый отзыв в библиотеке, раньше не было такой возможности");
        String expectedResult = mapper.writeValueAsString(comDto);

        given(service.getById(2L)).willReturn(new BookDto(2L, "123", "12312312", "123123"));
        given(service.getById(25L)).willThrow(new LibraryError("BOOK_NOT_FOUND",""));

        mvc.perform(post("/api/book/2/comments").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.commentText").value("Это мой первый отзыв в библиотеке, раньше не было такой возможности"));

        mvc.perform(get("/api/book/2/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.comments[0].commentText").value("Это мой первый отзыв в библиотеке, раньше не было такой возможности"));

        mvc.perform(get("/api/book/25/comments"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверить вызова АПИ создания и изменения отзыва")
    void shouldCorrectCreateAndUpdateComment() throws Exception {
        CommentAddDto comDto = new CommentAddDto("Сергей Петрович", "Это мой первый отзыв в библиотеке, раньше не было такой возможности");
        String expectedResult = mapper.writeValueAsString(comDto);

        given(service.getById(1L)).willReturn(new BookDto(1L, "123", "12312312", "123123"));

        MvcResult result = mvc.perform(post("/api/book/1/comments").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.commentText").value("Это мой первый отзыв в библиотеке, раньше не было такой возможности"))
                .andReturn();

        String stringJson = result.getResponse().getContentAsString(UTF_8);
        CommentDto resultDto = mapper.readValue(stringJson, CommentDto.class);

        mvc.perform(patch("/api/book/1/comments/" + resultDto.getId(), 1).param("commentText", "noviy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.commentText").value("noviy"));

    }

    @Test
    @DisplayName("Проверить вызова АПИ создания и удалить отзыва")
    void shouldCorrectCreateAndDeleteComment() throws Exception {
        CommentAddDto comDto = new CommentAddDto("Сергей Петрович", "Это мой первый отзыв в библиотеке, раньше не было такой возможности");
        String expectedResult = mapper.writeValueAsString(comDto);

        given(service.getById(3L)).willReturn(new BookDto(3L, "123", "12312312", "123123"));

        MvcResult result = mvc.perform(post("/api/book/3/comments").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.commentText").value("Это мой первый отзыв в библиотеке, раньше не было такой возможности"))
                .andReturn();

        String stringJson = result.getResponse().getContentAsString(UTF_8);
        CommentDto resultDto = mapper.readValue(stringJson, CommentDto.class);

        mvc.perform(delete("/api/book/3/comments/" + resultDto.getId(), 1))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/book/3/comments/" + resultDto.getId(), 1))
                .andExpect(status().isBadRequest());

    }

}
