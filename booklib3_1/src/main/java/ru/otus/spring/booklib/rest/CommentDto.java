package ru.otus.spring.booklib.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.booklib.domain.Comment;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    private long id;
    private String nick;
    private String commentText;

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getNick(), comment.getCommentText());
    }
}
