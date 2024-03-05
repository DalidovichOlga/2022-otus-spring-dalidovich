package ru.otus.spring.booklib.rest;

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
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.dto.BookDto;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.service.BookService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(
        username = "testuser",
        authorities = {"ROLE_USER"}
)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService service;

    @MockBean
    private UserDetailsService userService;

    @MockBean
    MessageSource messageSource;

    @Autowired
    RestExceptionHandler ExceptionHandler;


    @Test
    @DisplayName("Возвращение всего списка книг")
    void shouldReturnCorrectBookList() throws Exception {
        List<Book> books = List.of(new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                        new Genre("жанр 1")),
                new Book("BOOK NUMBER 2", new Author("Петр", "Петров", "Петрович"),
                        new Genre("жанр 2")));
        given(service.getAllBook()).willReturn(books);

        List<BookDto> expectedResult = books.stream()
                .map(BookDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("Проверить возвращение одной книги, если она есть")
    void shouldReturnBookById() throws Exception {
        Book book = new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                new Genre("жанр 1"));
        given(service.getById(5L)).willThrow(new LibraryError("BOOK_NOT_FOUND", "5"));
        given(service.getById(1L)).willReturn(book);
        BookDto expectedResult = BookDto.toDto(book);

        mvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));

        mvc.perform(get("/api/books/5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверить возвращение книг, по автору")
    void shouldReturnBooksByAuthor() throws Exception {
        Author author = new Author("Иван", "Иванов", "Иванович");
        List<Book> books = List.of(new Book("BOOK NUMBER 1", author,
                        new Genre("жанр 1")),
                new Book("BOOK NUMBER 2", author,
                        new Genre("жанр 2")));
        given(service.getBookByAuthor(any(), eq("Иванов И.И."))).willReturn(books);

        List<BookDto> expectedResult = books.stream()
                .map(BookDto::toDto).collect(Collectors.toList());


        mvc.perform(get("/api/books/author/Иванов И.И."))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));

    }


    @Test
    @DisplayName("Проверить вызова АПИ создания книги")
    void shouldCorrectCreateNewBook() throws Exception {
        Book book = new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                new Genre("жанр 1"));
        given(service.createBook(any())).willReturn(book);
        String expectedResult = mapper.writeValueAsString(BookDto.toDto(book));

        mvc.perform(post("/api/books").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("Проверить вызова АПИ изменения книги")
    void shouldCorrectUpdateBookName() throws Exception {
        Book book = new Book("BOOK NUMBER 1", new Author("Иван", "Иванов", "Иванович"),
                new Genre("жанр 1"));
        Book book2 = new Book("BOOK NUMBER 2", new Author("Иван", "Иванов", "Иванович"),
                new Genre("жанр 1"));
        given(service.getById(1L)).willReturn(book);
        given(service.modifyBook(any())).willReturn(book2);

        String expectedResult = mapper.writeValueAsString(BookDto.toDto(book2));

        mvc.perform(patch("/api/books/1", 1).param("title", "BOOK NUMBER 2")
                .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("Проверить вызова АПИ удаления книги")
    void shouldCorrectDeleteBook() throws Exception {
        mvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
        verify(service, times(1)).removeBook(1L);
    }
}
