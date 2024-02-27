package ru.otus.spring.booklib.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.dto.GenreDto;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;


    @GetMapping(value = "/api/genres")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok().body(service.getAllGaner().stream()
                .map(GenreDto::toDto)
                .collect(Collectors.toList()));
    }

    @PatchMapping("/api/genres/{id}")
    public ResponseEntity<GenreDto> updateGenreById(@PathVariable("id") long id,
                                                    @RequestParam(value = "genreName") String genreName) throws LibraryError {

        Genre genre = service.updateGenre(new Genre(genreName, id));

        return ResponseEntity.ok().body(GenreDto.toDto(genre));
    }
}
