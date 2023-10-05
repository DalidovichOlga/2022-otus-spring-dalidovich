package ru.otus.spring.booklib.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.dto.AuthorFioDto;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.service.AuthorService;
import ru.otus.spring.booklib.service.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageControllerAuthor {
    private final AuthorService authorService;
    private final BookService serviceBook;

    @GetMapping("/author/{id}")
    public String getModAuthor(Model model, @PathVariable("id") long id) throws LibraryError {
        long authorId = serviceBook.getById(id).getAuthor().getId();
        AuthorFioDto authorFioDto = AuthorFioDto.toDto(authorService.getById(authorId));
        model.addAttribute("authorFioDto", authorFioDto);
        return "author_upd";
    }

    @PatchMapping("/author/{id}")
    public String patchAuthor(@ModelAttribute("authorFioDto") AuthorFioDto authorFioDto, @PathVariable("id") long id) throws LibraryError {
        authorService.updateAuthor(authorFioDto);
        return "redirect:/book";
    }

    @GetMapping("/author")
    public String listAuthor(Model model) {
        List<Author> authorList = authorService.getAllAuthor();
        model.addAttribute("authors", authorList);
        return "author-list";
    }
}
