package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.booklib.dao.*;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.BookError;

import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookDao bookDao;

    @Autowired
    public LibraryServiceImpl(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }


    @Override
    public List<BookView> getAllBook() {
        return bookDao.getAll();
    }


    @Override
    public List<BookView> getBookByAuthor(Long id, String name) throws BookError {
        Author author;
        if (id > 0) {
            author = authorDao.getById(id);
            if (author == null)
                throw new BookError("AUTHOR_NOT_FOUND", "id = " + id);
        } else if (!"".equals(name)) {
            List<Author> authorsByName = authorDao.getAuthorsByName(name);
            if (authorsByName.isEmpty() || authorsByName.size() > 1)
                throw new BookError("AUTHOR_NOT_FOUND", name);
            author = authorsByName.get(0);
        } else
            throw new BookError("AUTHOR_NOT_FOUND", name);

        return bookDao.getAllBookByAuthor(author.getId());
    }

    @Override
    public Book modifyBook(Book book, String authorName, String genreName) throws BookError {
        BookView bookById = bookDao.getById(book.getId());

        if (bookById == null)
            throw new BookError("BOOK_NOT_FOUND", String.valueOf(book.getId()));
        // разберемся с автором
        if (book.getAuthorId() == 0 && !authorName.isEmpty()) {
            List<Author> authorByShortName = authorDao.getAuthorsByName(authorName);
            if (authorByShortName == null || authorByShortName.isEmpty())
                throw new BookError("AUTHOR_NOT_FOUND", authorName);
            book.setAuthorId(authorByShortName.get(0).getId());
        } else if (book.getAuthorId() > 0) {
            Author author = authorDao.getById(book.getAuthorId());

            if (author == null)
                throw new BookError("AUTHOR_NOT_FOUND", String.valueOf(book.getAuthorId()));
        }

        //теперь если меняем жанр проверить
        if (book.getGenreId() == 0 && !"".equals(genreName)) {
            List<Genre> genreList = genreDao.getGenreByName(genreName);
            if (genreList.isEmpty())
                throw new BookError("GENRE_NOT_FOUND", genreName);
            book.setGenreId(genreList.get(0).getId());

        } else if (book.getGenreId() > 0) {
            if (genreDao.getById(book.getGenreId()) == null)
                throw new BookError("GENRE_NOT_FOUND", genreName);
        }


        book.setGenreId(book.getGenreId() == 0 ? bookById.getGenreId() : book.getGenreId());
        book.setAuthorId(book.getAuthorId() == 0 ? bookById.getAuthorId() : book.getAuthorId());
        book.setTitle("".equals(book.getTitle()) ? bookById.getTitle() : book.getTitle());
        book.setPublisher("".equals(book.getPublisher()) ? bookById.getPublisher() : book.getPublisher());
        book.setPublicationYear("".equals(book.getPublicationYear()) ? bookById.getPublicationYear() : book.getPublicationYear());
        bookDao.update(book);

        return book;
    }

    @Override
    public Book createBook(Book book, String genreName) throws BookError {
        if (book.getAuthorId() == 0)
            throw new BookError("AUTHOR_NOT_FOUND", "id = 0");
        if ("".equals(book.getTitle()) || "".equals(genreName) && book.getGenreId() == 0)
            throw new BookError("ATTRIBUTE_NOT_PASSED", book.getTitle());
        if (book.getGenreId() == 0) {
            List<Genre> genreList = genreDao.getGenreByName(genreName);
            if (genreList.isEmpty())
                throw new BookError("GENRE_NOT_FOUND", genreName);
            book.setGenreId(genreList.get(0).getId());
        } else {
            Genre genre = genreDao.getById(book.getGenreId());
            if (genre == null)
                throw new BookError("GENRE_NOT_FOUND", genreName);
        }
        Author author = authorDao.getById(book.getAuthorId());
        if (author == null)
            throw new BookError("AUTHOR_NOT_FOUND", String.valueOf(book.getAuthorId()));

        List<BookView> allTitleAuthorGenre = bookDao.getAllTitleAuthorGenre(book.getTitle(), book.getAuthorId(), book.getGenreId());
        if (!allTitleAuthorGenre.isEmpty())
            throw new BookError("BOOK_ALREADY_EXISTS", book.getTitle());

        return bookDao.insert(book);
    }

    @Override
    public Book createBookWithAuthorName(Book book, String authorName, String genreName) throws BookError {

        if (book.getAuthorId() == 0 && "".equals(authorName))
            throw new BookError("AUTHOR_NOT_FOUND", "id = 0");
        List<Author> authorByShortName = authorDao.getAuthorsByName(authorName);
        if (authorByShortName.isEmpty()) {
            throw new BookError("AUTHOR_NOT_FOUND", String.valueOf(book.getAuthorId()));
        }
        book.setAuthorId(authorByShortName.get(0).getId());

        return createBook(book, genreName);
    }

    @Override
    public void deleteBook(Long id) throws BookError {
        BookView byId = bookDao.getById(id);
        if (byId == null)
            throw new BookError("BOOK_NOT_FOUND", "id=" + id);
        bookDao.deleteById(id);
    }


}
