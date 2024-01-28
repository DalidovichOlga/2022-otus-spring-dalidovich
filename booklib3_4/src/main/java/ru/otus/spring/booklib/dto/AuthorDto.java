package ru.otus.spring.booklib.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.booklib.domain.Author;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorDto {
    private long id;
    private String shortName;
    private String fullName;

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getShortName(), author.getFullName());
    }
}
