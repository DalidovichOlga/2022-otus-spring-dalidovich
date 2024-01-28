package ru.otus.spring.booklib.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.booklib.domain.Genre;

import java.util.List;

public interface GenreRepositoryJpa extends JpaRepository<Genre, Long>  {

    List<Genre> findByGenreName(String name);

    List<Genre>  findAll();
}
