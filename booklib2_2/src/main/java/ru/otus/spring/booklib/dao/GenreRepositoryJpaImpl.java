package ru.otus.spring.booklib.dao;

import org.springframework.stereotype.Repository;
import ru.otus.spring.booklib.domain.Genre;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;


@Repository
public class GenreRepositoryJpaImpl implements GenreRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void update(long id, String name) {
        Query query = em.createQuery("update Genre s " +
                "set s.genreName = :name " +
                "where s.id = :id");
        query.setParameter("name", name);
        query.setParameter("id", id);
        query.executeUpdate();

    }

    @Override
    public Genre insert(Genre genre) {
        em.persist(genre);

        return genre;
    }

    @Override
    public Optional<Genre> getById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public List<Genre> getGenreByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select s " +
                        "from Genre s " +
                        "where s.genreName = :name",
                Genre.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select s from Genre s  ", Genre.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete " +
                "from Genre s " +
                "where s.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
