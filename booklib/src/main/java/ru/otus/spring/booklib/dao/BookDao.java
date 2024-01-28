package ru.otus.spring.booklib.dao;

import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;

import java.util.List;

public interface BookDao {
    Book insert(Book person);

    void update(Book person);

    BookView getById(long id);

    List<BookView> getAll();

    void deleteById(long id);

    List<BookView> getAllBookByAuthor(Long authorId);

    List<BookView> getAllBookByGenre(Long genreId);

    List<BookView> getAllTitleAuthorGenre(String title, Long authorId, Long genreId);
}
