package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.*;
import ru.otus.spring.booklib.domain.*;
import ru.otus.spring.booklib.error.BookError;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepositoryJpa authorRepository;
    private final GenreRepositoryJpa genreRepository;
    private final BookRepositoryJpa bookRepository;
    private final CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    public BookServiceImpl(AuthorRepositoryJpa authorRepository, GenreRepositoryJpa genreRepository,
                           BookRepositoryJpa bookRepository,
                           CommentRepositoryJpa commentRepositoryJpa
    ) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
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
        Author author = getAuthorByParam(name, id);
        if (author == null)
            throw new BookError("AUTHOR_NOT_FOUND", name);
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
            Author authorByParam = getAuthorByParam(authorName, authorId);
            book.setAuthor(authorByParam);
        }

        //теперь жанр определить как объект базы
        if ((genreId != 0) || (!"".equals(genreName))) {
            Genre g = getOrCreateGenreByParam(genreName, genreId);
            book.setGenre(g);
        }

        book.setTitle("".equals(title) ? bookById.get().getTitle() : title);
        bookRepository.update(book);

        return book;
    }


    private Genre getOrCreateGenreByParam(String genreName, Long genreId) throws BookError {
        if (genreId == 0 && !"".equals(genreName)) {
            List<Genre> genreList = genreRepository.getGenreByName(genreName);
            if (genreList.isEmpty()) {
                Genre genre = genreRepository.insert(new Genre(genreName));
                return genre;
            } else
                return genreList.get(0);

        } else if (genreId > 0) {
            return genreRepository.getById(genreId).orElseThrow(() -> new BookError("GANRENAME_NOT_FOUND", String.valueOf(genreId)));

        }
        return null;
    }

    private Author getAuthorByParam(String authorName, Long authorId) throws BookError {
        if (authorId == 0 && !authorName.isEmpty()) {
            List<Author> authorByShortName = authorRepository.getAuthorsByName(authorName);
            if (authorByShortName == null || authorByShortName.isEmpty())
                throw new BookError("AUTHOR_NOT_FOUND", authorName);
            return authorByShortName.get(0);
        } else if (authorId > 0) {
            return authorRepository.getById(authorId).orElseThrow(
                    () -> new BookError("AUTHOR_NOT_FOUND", String.valueOf(authorId))
            );
        }
        return null;
    }

    private Author getOrCreateAuthorByParam(String authorName, Long authorId) throws BookError {
        if (authorId == 0 && !authorName.isEmpty()) {
            List<Author> authorByShortName = authorRepository.getAuthorsByName(authorName);
            if (authorByShortName == null || authorByShortName.isEmpty()) {
                Author author = authorRepository.insertByName(authorName);
                if (author == null)
                    throw new BookError("AUTHOR_NOT_FOUND", authorName);
                return author;
            } else return authorByShortName.get(0);
        } else if (authorId > 0) {
            return authorRepository.getById(authorId).orElseThrow(
                    () -> new BookError("AUTHOR_NOT_FOUND", String.valueOf(authorId))
            );
        } else {
            if ("".equals(authorName) && authorId == 0)
                throw new BookError("AUTHOR_NOT_FOUND", "id = 0");
        }
        return null;
    }

    @Transactional
    @Override
    public Book createBook(String title, String authorName, String genreName, Long authorId, Long genreId) throws BookError {
        if ("".equals(title) || "".equals(genreName) && genreId == 0)
            throw new BookError("ATTRIBUTE_NOT_PASSED", title);

        Genre genre = getOrCreateGenreByParam(genreName, genreId);
        Author author = getOrCreateAuthorByParam(authorName, authorId);

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
        final List<Book> allBookByAuthor = bookRepository.getAllBookByAuthor(authorId);
        if (allBookByAuthor.isEmpty())
            authorRepository.deleteById(authorId);
        final List<BookView> allBookByGenre = bookRepository.getAllBookByGenre(genreId);
        if (allBookByGenre.isEmpty())
            genreRepository.deleteById(genreId);

        commentRepositoryJpa.deleteByBookId(authorId);

    }

    @Override
    public Book getById(Long id) throws BookError {
        return bookRepository.getById(id).orElseThrow(() -> new BookError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
    }



}
