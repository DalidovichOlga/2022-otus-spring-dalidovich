package ru.otus.spring.booklib.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.dto.BookAddDto;
import ru.otus.spring.booklib.error.LibraryError;
import ru.otus.spring.booklib.dto.AuthorFioDto;
import ru.otus.spring.booklib.dto.BookDto;
import ru.otus.spring.booklib.dto.CommentDto;
import ru.otus.spring.booklib.service.BookService;
import ru.otus.spring.booklib.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PagesControllerBook {
    private final BookService serviceBook;

    @GetMapping("/")
    public String indexPage(Model model) {
        List<BookDto> bookDtos = serviceBook.getAllBook().stream().map(BookDto::toDto).collect(Collectors.toList());
        model.addAttribute("books", bookDtos);
        return "book-list";
    }

    @GetMapping("/book")
    public String listBook(Model model) {
        List<BookDto> bookDtos = serviceBook.getAllBook().stream().map(BookDto::toDto).collect(Collectors.toList());
        model.addAttribute("books", bookDtos);
        return "book-list";
    }

    @GetMapping("/book/{id}")
    public String getModBook(Model model, @PathVariable("id") long id) throws LibraryError {
        BookDto bookDto = BookDto.toDto(serviceBook.getById(id));
        model.addAttribute("bookdto", bookDto);
        return "book_upd";
    }

    @GetMapping("/book/delete/{id}")
    public String getDeleteBook(Model model, @PathVariable("id") long id) throws LibraryError {
        BookDto bookDto = BookDto.toDto(serviceBook.getById(id));
        model.addAttribute("bookdto", bookDto);
        return "book_del";
    }

    @GetMapping("/book/add")
    public String getBookAdd(Model model) throws LibraryError {
        BookAddDto bookDto = new BookAddDto();
        model.addAttribute("bookdto", bookDto);
        return "book_add";
    }

    @PatchMapping("/book/{id}")
    public String patchBook(@ModelAttribute("bookdto") BookDto bookdto, @PathVariable("id") long id) throws LibraryError {
        serviceBook.modifyBook(bookdto);
        return "redirect:/book";
    }

    @DeleteMapping("/book/{id}")
    public String deleteBook(@PathVariable("id") long id) throws LibraryError {
        serviceBook.removeBook(id);
        return "redirect:/book";
    }

    @PostMapping("/book")
    public String addBook(@ModelAttribute("bookdto") BookAddDto bookdto) throws LibraryError {
        serviceBook.createBook(bookdto);
        return "redirect:book";
    }


}
