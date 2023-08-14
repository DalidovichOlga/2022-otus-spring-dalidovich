package ru.otus.spring.booklib.dao;

import org.springframework.stereotype.Repository;
import ru.otus.spring.booklib.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CommentRepositoryJpaImpl implements CommentRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Comment insert(Comment comment) {
        em.persist(comment);

        return comment;
    }

    @Override
    public List<Comment> getByBookId(long id) {
        TypedQuery<Comment> query = em.createQuery("select s " +
                        "from Comment s " +
                        "where s.bookId = :id",
                Comment.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public void deleteByBookId(long id) {
        Query query = em.createQuery("delete " +
                "from Comment s " +
                "where s.bookId = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
