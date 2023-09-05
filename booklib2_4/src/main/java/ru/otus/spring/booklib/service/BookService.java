package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;

public interface BookService {

    List<Book> getAllBook();

    Book createBook(String title, String authorName, String genreName, Long authorId, Long genreId) throws LibraryError;

    Book modifyBook(Long bookId, String title, String authorName, String genreName, Long authorId, Long genreId) throws LibraryError;

    void removeBook(Long id) throws LibraryError;

    Book getById(Long id) throws LibraryError;

    List<Book> getBookByAuthor(Long id, String name) throws LibraryError;
}
