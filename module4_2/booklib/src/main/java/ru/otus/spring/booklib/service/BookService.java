package ru.otus.spring.booklib.service;

import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.dto.BookAddDto;
import ru.otus.spring.booklib.dto.BookDto;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;

public interface BookService {

    List<Book> getAllBook();

    Book createBook(BookAddDto bookAddDto) throws LibraryError;

    Book modifyBook(BookDto bookDto) throws LibraryError;

    void removeBook(Long id) throws LibraryError;

    Book getById(Long id) throws LibraryError;

    List<Book> getBookByAuthor(Long id, String name) throws LibraryError;
}
