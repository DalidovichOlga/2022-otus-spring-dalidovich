package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.error.BookError;

import java.util.List;

public interface LibraryService {

    List<BookView> getAllBook();


    Book createBook(Book book, String genreName) throws BookError;

    Book modifyBook(Book book, String authorName, String genreName) throws BookError;

    Book createBookWithAuthorName(Book book, String authorName, String genreName) throws BookError;

    void deleteBook(Long id) throws BookError;

    List<BookView> getBookByAuthor(Long id, String name) throws BookError;
}
