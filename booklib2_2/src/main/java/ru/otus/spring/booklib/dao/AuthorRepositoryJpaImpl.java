package ru.otus.spring.booklib.dao;

import org.springframework.stereotype.Repository;
import ru.otus.spring.booklib.domain.Author;

import javax.persistence.*;
import java.util.*;

@Repository
public class AuthorRepositoryJpaImpl implements AuthorRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override

    public Author insert(Author author) {
        em.persist(author);
        return author;
    }


    @Override
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> getAll() {

        TypedQuery<Author> query = em.createQuery("select s from Author s ", Author.class);

        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete " +
                "from Author s " +
                "where s.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();

    }

    @Override
    public void remove(Author author) {
        em.remove(author);
    }

    @Override
    public void update(Author author) {
        em.merge(author);
    }

    @Override
    public List<Author> getAuthorByShortName(String name) {
        TypedQuery<Author> query = em.createQuery("select s " +
                        "from Author s " +
                        "where s.shortName = :shortname",
                Author.class);
        query.setParameter("shortname", name);
        return query.getResultList();
    }

    @Override
    public List<Author> getAuthorsByName(String authorName) {
        List<Author> authorByShortName = getAuthorByShortName(authorName);
        if (authorByShortName.isEmpty()) {
            String lastName = authorName;
            String firstName = "";
            String middleName = "";
            if (authorName.indexOf(' ') > 0) {
                lastName = lastName.substring(0, authorName.indexOf(' '));
                firstName = authorName.substring(authorName.indexOf(' ') + 1).trim();
                if (firstName.indexOf(' ') > 0) {
                    middleName = firstName.substring(firstName.indexOf(' ') + 1).trim();
                    firstName = firstName.substring(0, firstName.indexOf(' '));
                }

            }
            authorByShortName = getAuthorByName(firstName, lastName, middleName);

        }
        return authorByShortName;
    }

    @Override
    public Author insertByName(String authorName) {
        //  проверка, что это похоже на имя автора
        if (authorName.isEmpty() || authorName.indexOf('.') > 0 || !authorName.contains(" "))
            return null;
        String lastName = authorName;
        String firstName = "";
        String middleName = "";
        if (authorName.indexOf(' ') > 0) {
            lastName = lastName.substring(0, authorName.indexOf(' '));
            firstName = authorName.substring(authorName.indexOf(' ') + 1).trim();
            if (firstName.indexOf(' ') > 0) {
                middleName = firstName.substring(firstName.indexOf(' ') + 1).trim();
                firstName = firstName.substring(0, firstName.indexOf(' '));
            }

        }

        return insert(new Author(firstName, lastName, middleName));
    }

    private List<Author> getAuthorByName(String firstName, String lastName, String middleName) {

        if (!"".equals(firstName) && !"".equals(lastName) && !"".equals(middleName)) {
            TypedQuery<Author> query = em.createQuery("select s " +
                            "from Author s " +
                            "where s.firstName = :firstname \n" +
                            " and s.lastName = :lastname and s.middleName = :middlename",
                    Author.class);
            query.setParameter("firstname", firstName);
            query.setParameter("lastname", lastName);
            query.setParameter("middlename", middleName);
            return query.getResultList();
        } else if (!"".equals(firstName) && !"".equals(lastName)) {
            TypedQuery<Author> query = em.createQuery("select s " +
                            "from Author s " +
                            "where s.firstName = :firstname \n" +
                            " and s.lastName = :lastname",
                    Author.class);
            query.setParameter("firstname", firstName);
            query.setParameter("lastname", lastName);
            return query.getResultList();
        } else if (!"".equals(lastName)) {
            TypedQuery<Author> query = em.createQuery("select s " +
                            "from Author s " +
                            "where s.firstName = :firstname",
                    Author.class);
            query.setParameter("firstname", firstName);

            return query.getResultList();

        } else {
            return List.of();
        }

    }

}
