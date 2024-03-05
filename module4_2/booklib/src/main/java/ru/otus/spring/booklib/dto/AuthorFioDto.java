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
public class AuthorFioDto {
    long id;
    private String firstName;
    private String lastName;
    private String middleName;

    public static AuthorFioDto toDto(Author author) {
        return new AuthorFioDto(author.getId(), author.getFirstName(), author.getLastName(), author.getMiddleName());
    }
    public static Author toAuthor(AuthorFioDto author) {
        return new Author(author.getFirstName(), author.getLastName(), author.getMiddleName(), author.getId());
    }
}
