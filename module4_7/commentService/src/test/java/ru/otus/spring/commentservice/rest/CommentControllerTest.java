package ru.otus.spring.commentservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.commentservice.domain.Comment;
import ru.otus.spring.commentservice.dto.BookDto;
import ru.otus.spring.commentservice.error.LibraryError;
import ru.otus.spring.commentservice.service.BookService;
import ru.otus.spring.commentservice.service.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser(
        username = "testuser",
        authorities = {"ROLE_USER"}
)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService service;

    @MockBean
    private CommentService commentService;

    @MockBean
    MessageSource messageSource;

    @Autowired
    RestExceptionHandler ExceptionHandler;

    @MockBean
    private UserDetailsService userService;


    @Test
    @DisplayName("Проверить возвращение отзыва")
    void shouldReturnComments4BookById() throws Exception {
        BookDto book = new BookDto(1L,"BOOK NUMBER 1", "Иванов Иван Иванович","жанр 1");
        given(service.getById(1L)).willReturn(book);

        Comment comment = new Comment("Сергей Петрович", "Это мой первый отзыв в библиотеке, раньше не было такой возможности", 1L, 1L);
        given(commentService.getComment(1L)).willReturn(List.of(comment));

        mvc.perform(get("/api/book/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.comments[0].commentText").value("Это мой первый отзыв в библиотеке, раньше не было такой возможности"))
        ;

    }

    @Test
    @DisplayName("Проверить вызова АПИ изменения отзыва")
    void shouldCorrectUpdateComment() throws Exception {

        BookDto book = new BookDto(1L , "BOOK NUMBER 1", "Иванов Иван Иванович",
                "жанр 1");
        given(service.getById(1L)).willReturn(book);
        given(commentService.modifyComment(eq(1L), eq(1l), any(), any()))
                .willAnswer((invocation) -> new Comment(invocation.getArgument(3), invocation.getArgument(2), 1, 1));

        mvc.perform(patch("/api/book/1/comments/1", 1).param("commentText", "noviy").param("nick", "Сергей Петрович"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nick").value("Сергей Петрович"))
                .andExpect(jsonPath("$.commentText").value("noviy"));

    }

    @Test
    @DisplayName("Проверить вызова АПИ создания и удалить отзыва")
    void shouldCorrectDeleteComment() throws Exception {
        mvc.perform(delete("/api/book/3/comments/1", 1))
                .andExpect(status().isOk());

        verify(commentService, times(1)).deleteComment(3L, 1L);

    }
}
