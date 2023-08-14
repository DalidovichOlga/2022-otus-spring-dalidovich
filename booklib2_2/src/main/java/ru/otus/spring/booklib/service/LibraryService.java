package ru.otus.spring.booklib.service;

import liquibase.pro.packaged.S;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.domain.Comment;
import ru.otus.spring.booklib.error.BookError;

import java.util.List;

public interface LibraryService {

    List<BookView> getAllBook();

    Book createBook(String title, String authorName, String genreName, Long authorId, Long genreId) throws BookError;

    Book modifyBook(Long bookId, String title, String authorName, String genreName, Long authorId, Long genreId) throws BookError;

    void removeBook(Long id) throws BookError;

    Book getById(Long id) throws BookError;

    List<Comment> getComment(Long id) throws BookError;

    void commentBook(Long id, String nick, String text) throws BookError;

    List<Book> getBookByAuthor(Long id, String name) throws BookError;
}
