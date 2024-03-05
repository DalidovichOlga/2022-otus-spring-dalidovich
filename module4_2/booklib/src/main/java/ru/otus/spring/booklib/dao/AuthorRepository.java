package ru.otus.spring.booklib.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.booklib.domain.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByShortName(String shortName);

    List<Author> findByFirstNameAndLastNameAndMiddleName(String firstName, String lastName, String middleName);

    List<Author> findByFirstNameAndLastName(String firstName, String lastName);

    List<Author> findByLastName(String lastName);

}
