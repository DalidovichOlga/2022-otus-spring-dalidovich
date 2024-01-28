package ru.otus.spring.booklib.dao;

import org.springframework.stereotype.Repository;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class BookRepositoryJpaImpl implements BookRepositoryJpa {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Book insert(Book book) {
        em.persist(book);
        return book;
    }

    @Override
    public Book update(Book book) {
        return em.merge(book);
    }

    @Override
    public void remove(Book book) {
        em.remove(book);
    }

    @Override
    public Optional<Book> getById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<BookView> getAll() {
        // тоже побеждает проблему n+1
        TypedQuery<BookView> query = em.createQuery("select new ru.otus.spring.booklib.domain.BookView (" +
                " b.title , b.id , a.shortName , g.genreName , a.id , b.id) " +
                "  from Book b join  b.author a  join b.genre g", BookView.class);
        return query.getResultList();

    }

    @Override
    public List<Book> getAllTitleAuthorGenre(String title, Long authorId, Long genreId) {
        TypedQuery<Book> query = em.createQuery("select b " +
                "  from Book b join  fetch  b.author a  join fetch b.genre g" +
                "  where b.title = :title and a.id  = :authorId  ", Book.class);

        query.setParameter("title", title);
        query.setParameter("authorId", authorId);
        return query.getResultList();
    }

    @Override
    public List<Book> getAllBookByAuthor(Long authorId) {
        TypedQuery<Book> query = em.createQuery("select b " +
                "  from Book b join fetch b.author a  join fetch b.genre g" +
                "  where a.id  = :authorId  ", Book.class);

        query.setParameter("authorId", authorId);
        return query.getResultList();

    }

    @Override
    public List<BookView> getAllBookByGenre(Long genreId) {
        TypedQuery<BookView> query = em.createQuery("select new ru.otus.spring.booklib.domain.BookView (" +
                " b.title , b.id , a.shortName , g.genreName , a.id , b.id) " +
                "  from Book b join  b.author a join b.genre g " +
                "  where g.id  = :genreid  ", BookView.class);

        query.setParameter("genreid", genreId);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete " +
                "from Book s " +
                "where s.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();

    }

}
