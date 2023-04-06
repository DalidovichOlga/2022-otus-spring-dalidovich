package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.booklib.dao.AuthorDao;
import ru.otus.spring.booklib.dao.BookDao;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.error.AuthorError;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;
    private final BookDao bookDao;

    @Autowired
    public AuthorServiceImpl(AuthorDao authorDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
    }

    @Override
    public void deleteAuthor(Long id, String name) throws AuthorError {
        Author author;
        if (id > 0) {
            author = authorDao.getById(id);
            if (author == null)
                throw new AuthorError("AUTHOR_NOT_FOUND", "id = " + id);
        } else if (!"".equals(name)) {
            List<Author> authorsByName = authorDao.getAuthorsByName(name);
            if (authorsByName.isEmpty() || authorsByName.size() > 1)
                throw new AuthorError("AUTHOR_NOT_FOUND", name);
            author = authorsByName.get(0);
        } else
            throw new AuthorError("AUTHOR_NOT_FOUND", name);
        Long authorid = author.getId();
        final List<BookView> allbyAuthor = bookDao.getAllBookByAuthor(authorid);
        if (allbyAuthor.isEmpty())
            authorDao.deleteById(authorid);
        else throw new AuthorError("AUTHOR_HAS_BOOK", "id = " + authorid);

    }

    @Override
    public List<Author> getAllAuthor() {
        return authorDao.getAll();
    }

    @Override
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
}
