package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.AuthorError;
import ru.otus.spring.booklib.error.BookError;
import ru.otus.spring.booklib.error.GenreError;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@ShellComponent
public class LibraryShell {
    private final LibraryService service;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final MessageSource messageSource;
    private final Locale locale;

    public LibraryShell(LibraryService service,
                        AuthorService authorService, GenreService genreService, MessageSource messageSource,
                        @Value("${settings.local}") String local) {

        this.service = service;
        this.authorService = authorService;
        this.genreService = genreService;
        this.messageSource = messageSource;
        locale = Locale.forLanguageTag(local);

    }

    @ShellMethod(value = "Add genre", key = {"gins", "genreInsert"})
    public String ganreAdd(String genrename) {
        Genre g = new Genre(genrename);
        String textResult = "";
        try {
            genreService.createGenre(g);
            textResult = messageSource.getMessage("genresuccess",
                    new String[]{genrename}, locale)
                    + System.lineSeparator();
        } catch (GenreError genreError) {
            textResult = messageSource.getMessage(genreError.getCode(),
                    new String[]{genreError.getDetails()}, locale);
        }
        return textResult;
    }

    @ShellMethod(value = "Delete genre", key = {"gdel", "genreDelete"})
    public String ganreDelete(@ShellOption(defaultValue = "0") long id, @ShellOption(defaultValue = "") String name) {
        Genre g = new Genre(name, id);
        String textResult = "";
        try {
            genreService.deleteGenre(g);
            textResult = messageSource.getMessage("genresuccessdel",
                    new String[]{name}, locale);
        } catch (GenreError genreError) {
            textResult = messageSource.getMessage(genreError.getCode(),
                    new String[]{genreError.getDetails()}, locale);
        }
        return textResult;
    }

    @ShellMethod(value = "get all genres", key = {"allg", "allgenre"})
    public List<String> getAllGanre() {

        List<String> stringList = genreService.getAllGaner().stream().map(t -> t.toString()).collect(Collectors.toList());
        return stringList;

    }

    @ShellMethod(value = "Add author", key = {"ains", "authorInsert"})
    public String authorAdd(@ShellOption(defaultValue = "") String lastName,
                            @ShellOption(defaultValue = "") String firstName,
                            @ShellOption(defaultValue = "") String middleName,
                            @ShellOption(defaultValue = "") String shortName) {

        Author author = new Author(firstName, lastName, middleName, shortName, 0L);
        String textResult = "";
        try {
            authorService.createAuthor(author);
            textResult = messageSource.getMessage("authorsuccess",
                    new String[]{author.getFullName()}, locale);
        } catch (AuthorError authorError) {
            textResult = messageSource.getMessage(authorError.getCode(),
                    new String[]{authorError.getDetails()}, locale);
        }
        return textResult;
    }

    @ShellMethod(value = "delete author", key = {"adel", "authorDelete"})
    public String authorDelete(@ShellOption(defaultValue = "0") long id, @ShellOption(defaultValue = "") String name) {
        String textResult = "";
        String authorTxt = (id == 0) ? name : "id = " + id;
        try {
            authorService.deleteAuthor(id, name);
            textResult = messageSource.getMessage("authorsuccessdel",
                    new String[]{authorTxt}, locale);
        } catch (AuthorError authorError) {
            textResult = messageSource.getMessage(authorError.getCode(),
                    new String[]{authorError.getDetails()}, locale);
        }
        return textResult;
    }

    @ShellMethod(value = "get all authors", key = {"alla", "allauthors"})
    public List<String> getAllAuthor() {

        List<String> stringList = authorService.getAllAuthor().stream().map(t -> t.toString()).collect(Collectors.toList());
        return stringList;

    }

    //@ShellOption(value = { "year" , "y"})
    @ShellMethod(value = "Add book", key = {"bins", "bookInsert"})
    public String bookAdd(String title,
                          String publisher,
                          String year,
                          @ShellOption(defaultValue = "0") Long authorId,
                          @ShellOption(defaultValue = "") String author,
                          @ShellOption(defaultValue = "0") Long genreId,
                          @ShellOption(defaultValue = "") String genre) {


        Book book = new Book(title, year, publisher, authorId, genreId);
        String textResult = "";
        try {

            if (authorId == 0) {
                service.createBookWithAuthorName(book, author, genre);
            } else
                service.createBook(book, genre);
            textResult = messageSource.getMessage("booksuccess",
                    new String[]{title}, locale);

        } catch (BookError bookError) {
            textResult = messageSource.getMessage(bookError.getErrorText(),
                    new String[]{bookError.getDetails()}, locale);
        }
        return textResult;
    }

    @ShellMethod(value = "Modify book", key = {"bmod", "bookModify"})
    public String bookUpdate(Long id,
                             @ShellOption(defaultValue = "") String title,
                             @ShellOption(defaultValue = "") String publisher,
                             @ShellOption(defaultValue = "") String year,
                             @ShellOption(defaultValue = "0") Long authorId,
                             @ShellOption(defaultValue = "") String author,
                             @ShellOption(defaultValue = "0") Long genreId,
                             @ShellOption(defaultValue = "") String genre) {
        Book book = new Book(title, year, publisher, authorId, genreId, id);
        String textResult = "";
        try {
            service.modifyBook(book, author, genre);

            textResult = messageSource.getMessage("booksuccessmod",
                    new String[]{title}, locale);
        } catch (BookError bookError) {
            textResult = messageSource.getMessage(bookError.getErrorText(),
                    new String[]{bookError.getDetails()}, locale);

        }
        return textResult;
    }

    @ShellMethod(value = "delete book", key = {"bdel", "bookDelete"})
    public String bookDelete(Long id) {
        String textResult = "";
        try {
            service.deleteBook(id);

            textResult = messageSource.getMessage("booksuccessdel",
                    new String[]{}, locale);
        } catch (BookError bookError) {
            textResult = messageSource.getMessage(bookError.getErrorText(),
                    new String[]{bookError.getDetails()}, locale);

        }
        return textResult;
    }

    @ShellMethod(value = "get all book", key = {"allb", "allbook"})
    public List<String> getAllBook() {
        List<String> stringList = service.getAllBook().stream().map(t -> t.toString()).collect(Collectors.toList());
        return stringList;
    }

    @ShellMethod(value = "get book by author", key = {"allba", "allbookauthor"})
    public List<String> getAllBookByAuthor(@ShellOption(defaultValue = "0") Long authorid,
                                           @ShellOption(defaultValue = "") String name) {

        List<String> stringList = null;
        try {
            stringList = service.getBookByAuthor(authorid, name).stream().map(t -> t.toString()).collect(Collectors.toList());
        } catch (BookError bookError) {
            System.out.println(messageSource.getMessage(bookError.getErrorText(),
                    new String[]{bookError.getDetails()}, locale));
        }
        return stringList;
    }


}
