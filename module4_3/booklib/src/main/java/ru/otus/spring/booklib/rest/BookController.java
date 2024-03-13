package ru.otus.spring.booklib.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.dto.BookAddDto;
import ru.otus.spring.booklib.dto.BookDto;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService service;

    @GetMapping(value = "/api/books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok().body(service.getAllBook().stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable("id") long id) throws LibraryError {
        System.out.println("getBookById" + id);
        Book book = service.getById(id);
        System.out.println("getBookById==" + book);
        return ResponseEntity.ok().body(BookDto.toDto(book));
    }


    @GetMapping("/api/books/author/{author}")
    public ResponseEntity<List<BookDto>> getBookByAuthor(@PathVariable(value = "author", required = true) String author) throws LibraryError {
        List<Book> books = service.getBookByAuthor(0L, author);
        return ResponseEntity.ok().body(books.stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList()));
    }


    @PostMapping("/api/books")
    public ResponseEntity<BookDto> createNewBook(@RequestBody BookAddDto dto) throws LibraryError {
        Book book = service.createBook(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(BookDto.toDto(book));
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") long id) throws LibraryError {
        service.removeBook(id);
        return ResponseEntity.ok().body("Deleted");
    }

    @PatchMapping("/api/books/{id}")
    public ResponseEntity<BookDto> updateBookById(@PathVariable("id") long id,
                                                  @RequestParam(value = "title", required = false, defaultValue = "") String title,
                                                  @RequestParam(value = "author", required = false, defaultValue = "") String author,
                                                  @RequestParam(value = "genre", required = false, defaultValue = "") String genre) throws LibraryError {

        Book book = service.modifyBook(new BookDto(id, title, author, genre));

        return ResponseEntity.ok().body(BookDto.toDto(book));
    }

}
