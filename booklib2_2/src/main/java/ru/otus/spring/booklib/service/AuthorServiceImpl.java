package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.AuthorRepositoryJpa;
import ru.otus.spring.booklib.dao.BookRepositoryJpa;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.error.AuthorError;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepositoryJpa authorDao;
    private final BookRepositoryJpa bookDao;

    @Autowired
    public AuthorServiceImpl(AuthorRepositoryJpa authorDao, BookRepositoryJpa bookDao) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id, String name) throws AuthorError {
        Author author;
        if (id > 0) {
            Optional<Author> optionalAuthor = authorDao.getById(id);
            if (optionalAuthor.isEmpty())
                throw new AuthorError("AUTHOR_NOT_FOUND", "id = " + id);
            author = optionalAuthor.get();
        } else if (!"".equals(name)) {
            List<Author> authorsByName = authorDao.getAuthorsByName(name);
            if (authorsByName.isEmpty() || authorsByName.size() > 1)
                throw new AuthorError("AUTHOR_NOT_FOUND", name);
            author = authorsByName.get(0);
        } else
            throw new AuthorError("AUTHOR_NOT_FOUND", name);
        Long authorid = author.getId();
        final List<Book> allbyAuthor = bookDao.getAllBookByAuthor(authorid);
        if (allbyAuthor.isEmpty())
            authorDao.remove(author);
        else throw new AuthorError("AUTHOR_HAS_BOOK", "id = " + authorid);

    }

    @Override
    @Transactional
    public List<Author> getAllAuthor() {
        return authorDao.getAll();
    }

    @Override
    @Transactional
    public Author createAuthor(Author author) throws AuthorError {
        if (author.getLastName() == null || "".equals(author.getFirstName()) || "".equals(author.getLastName()))
            throw new AuthorError("AUTHORNAME_NOT_PASSED", "<empty>");


        List<Author> authorList = authorDao.getAuthorByShortName(author.getShortName());
        if (!authorList.isEmpty()) {
            Optional<Author> author1 = authorList.stream().filter(a -> a.getFullName().equals(author.getFullName())).findFirst();
            if (author1.isPresent())
                return author1.get();
        }

        return authorDao.insert(author);
    }

    @Override
    @Transactional
    public void updateAuthor(Long id, String lastName, String firstName, String middleName) throws AuthorError {
        if (lastName == null || "".equals(firstName) || "".equals(lastName) || id == null || id == 0)
            throw new AuthorError("AUTHORNAME_NOT_PASSED", "<empty>");

        Author author = new Author(firstName, lastName, middleName);
        Optional<Author> byId = authorDao.getById(id);
        if (!byId.isPresent())
            throw new AuthorError("AUTHORNAME_NOT_PASSED", "<empty>");

        byId.get().setFirstName(author.getFirstName());
        byId.get().setLastName(author.getLastName());
        byId.get().setMiddleName(author.getMiddleName());
        byId.get().setShortName(author.getShortName());

        authorDao.update(byId.get());
    }
}
