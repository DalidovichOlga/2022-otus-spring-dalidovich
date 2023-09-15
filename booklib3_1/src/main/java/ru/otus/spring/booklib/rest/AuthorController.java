package ru.otus.spring.booklib.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthorController {
    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping(value = "/api/authors")
    public ResponseEntity<List<AuthorDto>> getAllBooks() {
        return ResponseEntity.ok().body(service.getAllAuthor().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable("id") long id) throws LibraryError {

        Author author = service.getAuthorByParam("",id);

        return ResponseEntity.ok().body(AuthorDto.toDto(author));
    }

    @PatchMapping("/api/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthorById(@PathVariable("id") long id,
                                                      @RequestParam(value = "lastName", required = false, defaultValue = "") String lastName,
                                                      @RequestParam(value = "firstName", required = false, defaultValue = "") String firstName,
                                                      @RequestParam(value = "middleName", required = false, defaultValue = "") String middleName) throws LibraryError {

        Author author = service.updateAuthor(id, lastName, firstName, middleName);

        return ResponseEntity.ok().body(AuthorDto.toDto(author));
    }
}
