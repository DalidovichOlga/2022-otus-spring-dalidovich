package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.AuthorError;
import ru.otus.spring.booklib.error.BookError;
import ru.otus.spring.booklib.error.CommentError;
import ru.otus.spring.booklib.error.GenreError;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@ShellComponent
public class LibraryShell {
    private final BookService service;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final MessageSource messageSource;
    private final CommentService commentService;
    private final Locale locale;

    public LibraryShell(BookService service,
                        AuthorService authorService,
                        GenreService genreService,
                        MessageSource messageSource,
                        CommentService commentService,
                        @Value("${settings.local}") String local) {

        this.service = service;
        this.authorService = authorService;
        this.genreService = genreService;
        this.messageSource = messageSource;
        this.commentService = commentService;
        locale = Locale.forLanguageTag(local);

    }


    @ShellMethod(value = "Modify genre", key = {"gmod", "genreModify"})
    public String genreModify(long id, String name) {
        Genre g = new Genre(name, id);
        String textResult = "";
        try {
            genreService.updateGenre(g);
            textResult = messageSource.getMessage("genresuccessmod",
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

    @ShellMethod(value = "Modify author", key = {"amod", "authorModity"})
    public String authorModity(long id,
                               String lastName,
                               String firstName,
                               String middleName
    ) {

        String textResult = "";
        try {
            authorService.updateAuthor(id, lastName, firstName, middleName);
            textResult = messageSource.getMessage("authorsuccess",
                    new String[]{lastName + ' ' + firstName + ' ' + middleName}, locale);
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

    @ShellMethod(value = "Add book", key = {"bins", "bookInsert"})
    public String bookAdd(String title,
                          @ShellOption(defaultValue = "") String author,
                          @ShellOption(defaultValue = "") String genre
    ) {

        String textResult = "";
        try {
            service.createBook(title, author, genre, 0L, 0L);
            textResult = messageSource.getMessage("booksuccess",
                    new String[]{title}, locale);

        } catch (BookError bookError) {
            textResult = messageSource.getMessage(bookError.getErrorText(),
                    new String[]{bookError.getDetails()}, locale);
        }
        return textResult;
    }

    @ShellMethod(value = "Add book with id", key = {"binsid", "bookInsert"})
    public String bookAddWithId(String title,
                                @ShellOption(defaultValue = "") String author,
                                @ShellOption(defaultValue = "") String genre,
                                @ShellOption(defaultValue = "0") Long genreId,
                                @ShellOption(defaultValue = "0") Long authorId
    ) {

        String textResult = "";
        try {
            service.createBook(title, author, genre, authorId, genreId);
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
                             @ShellOption(defaultValue = "") String author,
                             @ShellOption(defaultValue = "") String genre,
                             @ShellOption(defaultValue = "0") Long authorId,
                             @ShellOption(defaultValue = "0") Long genreId) {

        String textResult = "";
        try {
            service.modifyBook(id, title, author, genre, authorId, genreId);

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
            service.removeBook(id);
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
    public List<String> getAllBookByAuthor(@ShellOption(defaultValue = "") String name,
                                           @ShellOption(defaultValue = "0") Long authorId
    ) {

        List<String> stringList = null;
        try {
            stringList = service.getBookByAuthor(authorId, name).stream().map(t -> t.toString()).collect(Collectors.toList());
        } catch (BookError bookError) {
            System.out.println(messageSource.getMessage(bookError.getErrorText(),
                    new String[]{bookError.getDetails()}, locale));
        }
        return stringList;
    }

    @ShellMethod(value = "get book with comment", key = {"bookcom", "bookcomments"})
    public void getBookComment(Long id) {
        Book book = null;
        List<Comment> lstComment = null;


        try {
            book = service.getById(id);
            lstComment = commentService.getComment(id);
        } catch (CommentError commentError) {
            System.out.println(messageSource.getMessage(commentError.getCode(),
                    new String[]{commentError.getDetails()}, locale));
        }
        catch (BookError bookError) {
            System.out.println(messageSource.getMessage(bookError.getErrorText(),
                    new String[]{bookError.getDetails()}, locale));
        }


        if (book != null) {
            String bookInf=messageSource.getMessage("bookname",
                    new String[]{}, locale) + " " + book.getTitle() + "\n\r"+
                    messageSource.getMessage("bookauthor",
                            new String[]{}, locale) + " " + book.getAuthor().getFullName() + "\n\r"+
                    messageSource.getMessage("bookgenre",
                            new String[]{}, locale) + " " + book.getGenre().getGenreName() ;
            System.out.println(bookInf);
            System.out.println(messageSource.getMessage("comments",
                    new String[]{}, locale) );
            int number = 1;
            for (Comment comment :lstComment
                 ) {
                System.out.print(String.valueOf(number)+") "+comment);
                number++;
            }
        }

    }

    @ShellMethod(value = "add book comment", key = {"comadd", "addbookcomments"})
    public String commentBook(Long id, String nick, String text) {
        String textResult = "";
        try {
            commentService.commentBook(id, nick, text);
            textResult = messageSource.getMessage("commentsuccessadd",
                    new String[]{}, locale);

        } catch (CommentError bookError) {
            textResult = messageSource.getMessage(bookError.getCode(),
                    new String[]{bookError.getDetails()}, locale);
        }
        return textResult;
    }

    @ShellMethod(value = "delete comment", key = {"comdel", "deletebookcomments"})
    public String deleteCommentBook(Long bookId, int numberComment) {
        String textResult = "";
        try {
            commentService.deleteComment(bookId, numberComment);
            textResult = messageSource.getMessage("commentsuccessdel",
                    new String[]{}, locale);

        } catch (CommentError bookError) {
            textResult = messageSource.getMessage(bookError.getCode(),
                    new String[]{bookError.getDetails()}, locale);
        }
        return textResult;
    }
}
