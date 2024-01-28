package ru.otus.spring.booklib.dao;

import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryJpa {
    Book insert(Book person);

    Book update(Book person);

    Optional<Book> getById(long id);

    List<BookView> getAll();

    void deleteById(long id);

    void remove(Book book);

    List<Book> getAllBookByAuthor(Long authorId);

    List<BookView> getAllBookByGenre(Long genreId);

    List<Book> getAllTitleAuthorGenre(String title, Long authorId, Long genreId);
}
