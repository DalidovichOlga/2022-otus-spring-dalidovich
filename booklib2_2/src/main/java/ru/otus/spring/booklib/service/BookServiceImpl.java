package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.*;
import ru.otus.spring.booklib.domain.*;
import ru.otus.spring.booklib.error.LibraryError;

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
    public List<Book> getBookByAuthor(Long id, String name) throws LibraryError {
        Author author = null;
        author = authorService.getAuthorByParam(name, id);

        return bookRepository.getAllBookByAuthor(author.getId());
    }

    @Transactional
    @Override
    public Book modifyBook(Long bookId, String title, String authorName, String genreName, Long authorId, Long genreId) throws LibraryError {
        Optional<Book> bookById = bookRepository.getById(bookId);

        if (!bookById.isPresent())
            throw new LibraryError("BOOK_NOT_FOUND", String.valueOf(bookId));

        Book book = bookById.get();

        // разберемся с автором
        if ((authorId != 0 || !authorName.isEmpty())) {
            Author authorByParam = null;
            authorByParam = authorService.getAuthorByParam(authorName, authorId);
            book.setAuthor(authorByParam);
        }

        //теперь жанр определить как объект базы
        if ((genreId != 0) || (!"".equals(genreName))) {
            Genre g = genreService.getOrCreateGenreByParam(genreName, genreId);
            book.setGenre(g);
        }

        book.setTitle("".equals(title) ? bookById.get().getTitle() : title);
        bookRepository.update(book);

        return book;
    }

    @Transactional
    @Override
    public Book createBook(String title, String authorName, String genreName, Long authorId, Long genreId) throws LibraryError {
        if ("".equals(title) || "".equals(genreName) && genreId == 0)
            throw new LibraryError("ATTRIBUTE_NOT_PASSED", title);

        Genre genre = genreService.getOrCreateGenreByParam(genreName, genreId);

        Author author = authorService.getOrCreateAuthorByParam(authorName, authorId);

        List<Book> allTitleAuthorGenre = bookRepository.getAllTitleAuthorGenre(
                title, author.getId(), genre.getId());
        if (!allTitleAuthorGenre.isEmpty())
            throw new LibraryError("BOOK_ALREADY_EXISTS", title);

        Book book = new Book(title, author, genre);
        return bookRepository.insert(book);
    }

    @Transactional
    @Override
    public void removeBook(Long id) throws LibraryError {
        Book book = bookRepository.getById(id).orElseThrow(() -> new LibraryError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        long genreId = book.getGenre().getId();
        long authorId = book.getAuthor().getId();
        bookRepository.remove(book);

        try {
            authorService.deleteAuthor(authorId);
        } catch (LibraryError libraryError) {
            //подавим исключение, так как значит автора удалять рано
        }

        try {
            genreService.deleteGenre(genreId);
        } catch (LibraryError genreError) { //подавим исключение, так как жанр удалять рано
        }

        commentRepositoryJpa.deleteByBookId(authorId);

    }

    @Override
    public Book getById(Long id) throws LibraryError {
        return bookRepository.getById(id).orElseThrow(() -> new LibraryError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
    }


}
