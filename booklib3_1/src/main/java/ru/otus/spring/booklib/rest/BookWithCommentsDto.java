package ru.otus.spring.booklib.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookWithCommentsDto {
    private BookDto          book;
    private List<CommentDto> comments;
}
