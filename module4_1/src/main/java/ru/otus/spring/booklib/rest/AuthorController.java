package ru.otus.spring.booklib.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.dto.AuthorDto;
import ru.otus.spring.booklib.dto.AuthorFioDto;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService service;

    @GetMapping(value = "/api/authors")
    public ResponseEntity<List<AuthorDto>> getAllBooks() {
        return ResponseEntity.ok().body(service.getAllAuthor().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable("id") long id) throws LibraryError {

        Author author = service.getAuthorByParam( id);

        return ResponseEntity.ok().body(AuthorDto.toDto(author));
    }

    @PatchMapping("/api/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthorById(@PathVariable("id") long id,
                                                      @RequestParam(value = "lastName", required = false, defaultValue = "") String lastName,
                                                      @RequestParam(value = "firstName", required = false, defaultValue = "") String firstName,
                                                      @RequestParam(value = "middleName", required = false, defaultValue = "") String middleName) throws LibraryError {

        AuthorFioDto authorFioDto = new AuthorFioDto (id,firstName,lastName,middleName);
        Author author = service.updateAuthor(authorFioDto);

        return ResponseEntity.ok().body(AuthorDto.toDto(author));
    }
}
