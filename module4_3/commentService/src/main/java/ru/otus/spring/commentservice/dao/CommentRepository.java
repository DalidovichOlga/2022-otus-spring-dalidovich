package ru.otus.spring.commentservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.commentservice.domain.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBookId(long bookId);

    Comment findById(long commentId);

    @Modifying
    @Query("delete Comment c  where c.bookId = :id")
    void deleteByBookId(@Param("id")  long id);

}
