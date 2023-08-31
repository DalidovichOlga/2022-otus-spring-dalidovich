package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.*;
import ru.otus.spring.booklib.domain.*;
import ru.otus.spring.booklib.error.AuthorError;
import ru.otus.spring.booklib.error.BookError;
import ru.otus.spring.booklib.error.GenreError;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookRepositoryJpa bookRepository;
    private final CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    public BookServiceImpl(AuthorService authorService, GenreService genreService,
                           BookRepositoryJpa bookRepository,
                           CommentRepositoryJpa commentRepositoryJpa
    ) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookRepository = bookRepository;
        this.commentRepositoryJpa = commentRepositoryJpa;
    }


    @Override
    public List<BookView> getAllBook() {
        return bookRepository.getAll();
    }

    @Transactional
    @Override
    public List<Book> getBookByAuthor(Long id, String name) throws BookError {
        Author author = null;
        try {
            author = authorService.getAuthorByParam(name, id);
        } catch (AuthorError authorError) {
            throw new BookError(authorError.getCode(), authorError.getDetails());
        }
        return bookRepository.getAllBookByAuthor(author.getId());
    }

    @Transactional
    @Override
    public Book modifyBook(Long bookId, String title, String authorName, String genreName, Long authorId, Long genreId) throws BookError {
        Optional<Book> bookById = bookRepository.getById(bookId);

        if (!bookById.isPresent())
            throw new BookError("BOOK_NOT_FOUND", String.valueOf(bookId));

        Book book = bookById.get();

        // разберемся с автором
        if ((authorId != 0 || !authorName.isEmpty())) {
            Author authorByParam = null;
            try {
                authorByParam = authorService.getAuthorByParam(authorName, authorId);
            } catch (AuthorError authorError) {
                throw new BookError(authorError.getCode(), authorError.getDetails());
            }
            book.setAuthor(authorByParam);
        }

        //теперь жанр определить как объект базы
        if ((genreId != 0) || (!"".equals(genreName))) {
            Genre g = genreService.getOrCreateGenreByParam(genreName, genreId);
            if (g == null)
                throw new BookError("GANRENAME_NOT_FOUND", String.valueOf(genreId));
            book.setGenre(g);
        }

        book.setTitle("".equals(title) ? bookById.get().getTitle() : title);
        bookRepository.update(book);

        return book;
    }

    @Transactional
    @Override
    public Book createBook(String title, String authorName, String genreName, Long authorId, Long genreId) throws BookError {
        if ("".equals(title) || "".equals(genreName) && genreId == 0)
            throw new BookError("ATTRIBUTE_NOT_PASSED", title);

        Genre genre = genreService.getOrCreateGenreByParam(genreName, genreId);
        if (genre == null)
            throw new BookError("GANRENAME_NOT_FOUND", String.valueOf(genreId));
        Author author = null;
        try {
            author = authorService.getOrCreateAuthorByParam(authorName, authorId);
        } catch (AuthorError authorError) {
            throw new BookError(authorError.getCode(), authorError.getDetails());
        }

        List<Book> allTitleAuthorGenre = bookRepository.getAllTitleAuthorGenre(
                title, author.getId(), genre.getId());
        if (!allTitleAuthorGenre.isEmpty())
            throw new BookError("BOOK_ALREADY_EXISTS", title);

        Book book = new Book(title, author, genre);
        return bookRepository.insert(book);
    }

    @Transactional
    @Override
    public void removeBook(Long id) throws BookError {
        Book book = bookRepository.getById(id).orElseThrow(() -> new BookError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        long genreId = book.getGenre().getId();
        long authorId = book.getAuthor().getId();
        bookRepository.remove(book);

        try {
            authorService.deleteAuthor(authorId);
        } catch (AuthorError authorError) {
            //подавим исключение, так как значит автора удалять рано
        }

        try {
            genreService.deleteGenre(genreId);
        } catch (GenreError genreError) { //подавим исключение, так как жанр удалять рано
        }

        commentRepositoryJpa.deleteByBookId(authorId);

    }

    @Override
    public Book getById(Long id) throws BookError {
        return bookRepository.getById(id).orElseThrow(() -> new BookError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
    }


}
