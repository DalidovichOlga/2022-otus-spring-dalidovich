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
public class LibraryServiceImpl implements LibraryService {
    private final AuthorRepositoryJpa authorRepository;
    private final GenreRepositoryJpa genreRepository;
    private final BookRepositoryJpa bookRepository;
    private final CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    public LibraryServiceImpl(AuthorRepositoryJpa authorRepository, GenreRepositoryJpa genreRepository,
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
        Author author;
        if (id > 0) {
            Optional<Author> authorOptional = authorRepository.getById(id);
            if (authorOptional.isEmpty())
                throw new BookError("AUTHOR_NOT_FOUND", "id = " + id);
            author = authorOptional.get();
        } else if (!"".equals(name)) {
            List<Author> authorsByName = authorRepository.getAuthorsByName(name);
            if (authorsByName.isEmpty() || authorsByName.size() > 1)
                throw new BookError("AUTHOR_NOT_FOUND", name);
            author = authorsByName.get(0);
        } else
            throw new BookError("AUTHOR_NOT_FOUND", name);

        return bookRepository.getAllBookByAuthor(author.getId());
    }

    @Transactional
    @Override
    public Book modifyBook(Long bookId, String title, String authorName, String genreName, Long authorId, Long genreId) throws BookError {
        Optional<Book> bookById = bookRepository.getById(bookId);

        if (!bookById.isPresent())
            throw new BookError("BOOK_NOT_FOUND", String.valueOf(bookId));
        // разберемся с автором
        Book book = bookById.get();
        if (authorId == 0 && !authorName.isEmpty()) {
            List<Author> authorByShortName = authorRepository.getAuthorsByName(authorName);
            if (authorByShortName == null || authorByShortName.isEmpty())
                throw new BookError("AUTHOR_NOT_FOUND", authorName);
            book.setAuthor(authorByShortName.get(0));
        } else if (authorId > 0) {
            Optional<Author> author = authorRepository.getById(authorId);

            if (author.isEmpty())
                throw new BookError("AUTHOR_NOT_FOUND", String.valueOf(authorId));
            book.setAuthor(author.get());
        }

        //теперь если меняем жанр проверить
        if (genreId == 0 && !"".equals(genreName)) {
            List<Genre> genreList = genreRepository.getGenreByName(genreName);
            if (genreList.isEmpty()) {
                Genre genre = genreRepository.insert(new Genre(genreName));
                book.setGenre(genre);
            }
            else
                book.setGenre(genreList.get(0));

        } else if (genreId > 0) {
            Optional<Genre> genre = genreRepository.getById(genreId);
            if (genre.isEmpty()) {
                throw new BookError("GANRENAME_NOT_FOUND", String.valueOf(genreId));
            }
            book.setGenre(genre.get());
        }

        book.setTitle("".equals(title) ? bookById.get().getTitle() : title);
        bookRepository.update(book);

        return book;
    }

    @Transactional
    @Override
    public Book createBook(String title, String authorName, String genreName, Long authorId, Long genreId) throws BookError {
        if ("".equals(authorName) && authorId == 0)
            throw new BookError("AUTHOR_NOT_FOUND", "id = 0");
        if ("".equals(title) || "".equals(genreName) && genreId == 0)
            throw new BookError("ATTRIBUTE_NOT_PASSED", title);
        Genre genre = null;
        if (genreId == 0) {
            List<Genre> genreList = genreRepository.getGenreByName(genreName);
            if (genreList.isEmpty()) {
                genre = genreRepository.insert(new Genre(genreName));
            } else genre = genreList.get(0);
        } else {
            genre = genreRepository.getById(genreId).orElseThrow(() -> new BookError("GANRENAME_NOT_FOUND", genreName));
        }

        Author author = null;
        if (authorId == 0) {
            List<Author> authorList = authorRepository.getAuthorsByName(authorName);
            if (authorList.isEmpty()) {
                author = authorRepository.insertByName(authorName);
                if (author == null)
                    throw new BookError("AUTHOR_NOT_FOUND", authorName);
            } else author = authorList.get(0);
        } else {
            author = authorRepository.getById(authorId).orElseThrow(() -> new BookError("AUTHOR_NOT_FOUND", String.valueOf(authorId)));
        }

        List<Book> allTitleAuthorGenre = bookRepository.getAllTitleAuthorGenre(title, author.getId(), genre.getId());
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

    @Transactional
    @Override
    public List<Comment> getComment(Long id) throws BookError {
        final Book book = bookRepository.getById(id).orElseThrow(() -> new BookError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        List<Comment> comments = commentRepositoryJpa.getByBookId(id);
        return comments;
    }

    @Transactional
    @Override
    public void commentBook(Long id, String nick, String text) throws BookError {
        Book book = bookRepository.getById(id).orElseThrow(() -> new BookError("BOOK_NOT_FOUND", "id =" + String.valueOf(id)));
        if ("".equals(text))
            throw new BookError("BOOK_COMMENT_EMPTY", "id =" + String.valueOf(id));
        commentRepositoryJpa.insert(new Comment(nick, text, id));

    }

}
