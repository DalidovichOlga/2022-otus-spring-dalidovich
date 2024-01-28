package ru.otus.spring.booklib.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.booklib.dto.BookDto;
import ru.otus.spring.booklib.dto.CommentAddDto;
import ru.otus.spring.booklib.dto.CommentDto;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.service.BookService;
import ru.otus.spring.booklib.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PagesControllerComment {

    private final CommentService serviceComment;
    private final BookService serviceBook;

    //комментарии
    @PostMapping("/book/{id}/сomment")
    public String patchBook(@ModelAttribute("CommentDto") CommentAddDto commentDto, @PathVariable("id") long id) throws LibraryError {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getName();
        commentDto.setNick(username);
        serviceComment.commentBook(id, commentDto);
        return "redirect:/book/" + id + "/comment";
    }

    @GetMapping("/book/{id}/comment")
    public String getCommentBook(Model model, @PathVariable("id") long id) throws LibraryError {
        BookDto bookDto = BookDto.toDto(serviceBook.getById(id));
        model.addAttribute("bookdto", bookDto);
        List<CommentDto> commentDtos = serviceComment.getComment(id).stream().map(CommentDto::toDto).collect(Collectors.toList());
        model.addAttribute("comments", commentDtos);
        model.addAttribute("CommentDto", new CommentAddDto());
        return "book_comment";
    }

    @DeleteMapping("/book/{id}/comment/{commentId}")
    public String deleteComment(@PathVariable("id") long id, @PathVariable("commentId") long commentId) throws LibraryError {
        serviceComment.deleteComment(id, commentId);
        return "redirect:/book/" + id + "/comment";
    }
}
