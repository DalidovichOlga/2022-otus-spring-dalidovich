package ru.otus.spring.booklib.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Проверить возвращение отзыва")
    void shouldReturnBookById() throws Exception {
        CommentAddDto comDto = new CommentAddDto("Сергей Петрович", "Это мой первый отзыв в библиотеке, раньше не было такой возможности");
        String expectedResult = mapper.writeValueAsString(comDto);

        mvc.perform(post("/api/books/2/comments").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.commentText").value("Это мой первый отзыв в библиотеке, раньше не было такой возможности"));

        mvc.perform(get("/api/books/2/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.comments[0].commentText").value("Это мой первый отзыв в библиотеке, раньше не было такой возможности"))
                .andReturn();

        mvc.perform(get("/api/books/25/comments"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверить вызова АПИ создания и изменения отзыва")
    void shouldCorrectCreateAndUpdateComment() throws Exception {
        CommentAddDto comDto = new CommentAddDto("Сергей Петрович", "Это мой первый отзыв в библиотеке, раньше не было такой возможности");
        String expectedResult = mapper.writeValueAsString(comDto);

        mvc.perform(post("/api/books/1/comments").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.commentText").value("Это мой первый отзыв в библиотеке, раньше не было такой возможности"));


        mvc.perform(patch("/api/books/1/comments/1", 1).param("commentText", "noviy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.commentText").value("noviy"));


    }



    @Test
    @DisplayName("Проверить вызова АПИ создания и удалить отзыва")
    void shouldCorrectCreateAndDeleteComment() throws Exception {
        CommentAddDto comDto = new CommentAddDto("Сергей Петрович", "Это мой первый отзыв в библиотеке, раньше не было такой возможности");
        String expectedResult = mapper.writeValueAsString(comDto);

        mvc.perform(post("/api/books/3/comments").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.commentText").value("Это мой первый отзыв в библиотеке, раньше не было такой возможности"));


        mvc.perform(delete("/api/books/3/comments/1", 1) )
                .andExpect(status().isOk());

        mvc.perform(delete("/api/books/3/comments/1", 1) )
                .andExpect(status().isBadRequest());

    }

}
