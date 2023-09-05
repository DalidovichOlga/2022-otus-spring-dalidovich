package ru.otus.spring.booklib.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.booklib.domain.Book;

import java.util.List;

public interface BookRepositoryJpa extends JpaRepository<Book, Long> {

    @EntityGraph(value = "book-with-author-and-genre")
    List<Book> findAll();

    @EntityGraph(value = "book-with-author-and-genre")
    List<Book>  findByAuthorId(Long authorId);

    Boolean existsByGenreId(Long genreId);

    List<Book> findByTitleAndAuthorId(String title, Long authorId);
}
