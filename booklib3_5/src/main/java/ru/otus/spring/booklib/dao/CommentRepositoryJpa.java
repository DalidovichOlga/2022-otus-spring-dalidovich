package ru.otus.spring.booklib.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.booklib.domain.Comment;

import java.util.List;


public interface CommentRepositoryJpa extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(long bookId);

    @Modifying
    @Query("delete Comment c  where c.bookId = :id")
    void deleteByBookId(@Param("id")  long id);

}
