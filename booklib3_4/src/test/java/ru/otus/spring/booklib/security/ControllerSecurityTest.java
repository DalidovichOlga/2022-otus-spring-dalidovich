package ru.otus.spring.booklib.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.pages.PagesControllerBook;
import ru.otus.spring.booklib.dto.BookDto;
import ru.otus.spring.booklib.rest.RestExceptionHandler;
import ru.otus.spring.booklib.service.AuthorService;
import ru.otus.spring.booklib.service.BookService;
import ru.otus.spring.booklib.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PagesControllerBook.class)
@Import(SecurityConfiguration.class)
public class ControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService service;

    @MockBean
    private AuthorService serviceAuthor;

    @MockBean
    private CommentService serviceComment;

    @Autowired
    private HttpSecurity http;
    @MockBean
    private UserDetailsService userService;

    @MockBean
    MessageSource messageSource;

    @Autowired
    RestExceptionHandler ExceptionHandler;


    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("проверить что доступ открыт для пользователя user")
    void shouldReturnOk() throws Exception {
        List<Book> books = List.of(new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                        new Genre("жанр 1")),
                new Book("BOOK NUMBER 2", new Author("Петр", "Петров", "Петрович"),
                        new Genre("жанр 2")));
        given(service.getAllBook()).willReturn(books);

        List<BookDto> expectedResult = books.stream()
                .map(BookDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/book"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Возвращение всего списка книг для неавторизованного пользователя")
    void shouldReturnNoOk() throws Exception {
        List<Book> books = List.of(new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                        new Genre("жанр 1")),
                new Book("BOOK NUMBER 2", new Author("Петр", "Петров", "Петрович"),
                        new Genre("жанр 2")));
        given(service.getAllBook()).willReturn(books);

        List<BookDto> expectedResult = books.stream()
                .map(BookDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/book"))
                .andExpect(status().is3xxRedirection());

    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Пользователь может получить книгу  для редактирования")
    void shouldReturnBookById() throws Exception {
        Book book = new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                new Genre("жанр 1"));
        given(service.getById(5L)).willThrow(new LibraryError("BOOK_NOT_FOUND", "5"));
        given(service.getById(1L)).willReturn(book);
        BookDto expectedResult = BookDto.toDto(book);

        mvc.perform(get("/book/1"))
                .andExpect(status().isOk());

        mvc.perform(get("/book/5"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Проверить вызова АПИ создания книги")
    void shouldCorrectCreateNewBook() throws Exception {
        Book book = new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                new Genre("жанр 1"));
        given(service.createBook(any())).willReturn(book);
        String expectedResult = mapper.writeValueAsString(BookDto.toDto(book));

        mvc.perform(post("/book").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().is3xxRedirection());
    }


    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Проверить доступность формы удаления книги")
    void shouldShowDeleteBook() throws Exception {
        Book book = new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                new Genre("жанр 1"));
        given(service.getById(1L)).willReturn(book);
        String expectedResult = mapper.writeValueAsString(BookDto.toDto(book));

        mvc.perform(get("/book/delete/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    @DisplayName("Проверить возможность вызова АПИ изменения книги")
    void shouldCorrectUpdateBookName() throws Exception {
        Book book = new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                new Genre("жанр 1"));
        Book book2 = new Book("BOOK NUMBER 2", new Author("Иван", "Иванов", "Иванович"),
                new Genre("жанр 1"));
        given(service.getById(1L)).willReturn(book);
        given(service.modifyBook(any())).willReturn(book2);

        String expectedResult = mapper.writeValueAsString(BookDto.toDto(book2));

        mvc.perform(patch("/book/1", 1).contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Проверить вызова АПИ  удаления книги без пользователя")
    void shouldCorrectDeleteBook() throws Exception {
        mvc.perform(delete("/book/1"))
                .andExpect(status().is3xxRedirection());
        //не вызовется, так как не аутентифицирован пользователь
        verify(service, times(0)).removeBook(1L);
    }
}



