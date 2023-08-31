package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.AuthorRepositoryJpa;
import ru.otus.spring.booklib.dao.BookRepositoryJpa;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.error.AuthorError;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepositoryJpa authorRepository;
    private final BookRepositoryJpa bookRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepositoryJpa authorRepository, BookRepositoryJpa bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) throws AuthorError {
        final List<Book> allbyAuthor = bookRepository.getAllBookByAuthor(id);
        if (allbyAuthor.isEmpty())
            authorRepository.deleteById(id);
        else throw new AuthorError("AUTHOR_HAS_BOOK", "id = " + id);
    }

    @Override
    @Transactional
    public List<Author> getAllAuthor() {
        return authorRepository.getAll();
    }

    @Override
    @Transactional
    public Author createAuthor(Author author) throws AuthorError {
        if (author.getLastName() == null || "".equals(author.getFirstName()) || "".equals(author.getLastName()))
            throw new AuthorError("AUTHORNAME_NOT_PASSED", "<empty>");


        List<Author> authorList = authorRepository.getAuthorByShortName(author.getShortName());
        if (!authorList.isEmpty()) {
            Optional<Author> author1 = authorList.stream().filter(a -> a.getFullName().equals(author.getFullName())).findFirst();
            if (author1.isPresent())
                return author1.get();
        }

        return authorRepository.insert(author);
    }

    @Override
    @Transactional
    public void updateAuthor(Long id, String lastName, String firstName, String middleName) throws AuthorError {
        if (lastName == null || "".equals(firstName) || "".equals(lastName) || id == null || id == 0)
            throw new AuthorError("AUTHORNAME_NOT_PASSED", "<empty>");

        Author author = new Author(firstName, lastName, middleName);
        Optional<Author> byId = authorRepository.getById(id);
        if (!byId.isPresent())
            throw new AuthorError("AUTHORNAME_NOT_PASSED", "<empty>");

        byId.get().setFirstName(author.getFirstName());
        byId.get().setLastName(author.getLastName());
        byId.get().setMiddleName(author.getMiddleName());
        byId.get().setShortName(author.getShortName());

        authorRepository.update(byId.get());
    }

    public Author getAuthorByParam(String authorName, Long authorId) throws AuthorError {
        if (authorId == 0 && !authorName.isEmpty()) {
            List<Author> authorByShortName = authorRepository.getAuthorsByName(authorName);
            if (authorByShortName == null || authorByShortName.isEmpty())
                throw new AuthorError("AUTHOR_NOT_FOUND", authorName);
            return authorByShortName.get(0);
        } else if (authorId > 0) {
            return authorRepository.getById(authorId).orElseThrow(
                    () -> new AuthorError("AUTHOR_NOT_FOUND", String.valueOf(authorId))
            );
        } else
            throw new AuthorError("AUTHOR_NOT_FOUND", String.valueOf(authorId));
    }

    public Author getOrCreateAuthorByParam(String authorName, Long authorId) throws AuthorError {
        if (authorId == 0 && !authorName.isEmpty()) {
            List<Author> authorByShortName = authorRepository.getAuthorsByName(authorName);
            if (authorByShortName == null || authorByShortName.isEmpty()) {
                Author author = authorRepository.insertByName(authorName);
                if (author == null)
                    throw new AuthorError("AUTHOR_NOT_FOUND", authorName);
                return author;
            } else return authorByShortName.get(0);
        } else if (authorId > 0) {
            return authorRepository.getById(authorId).orElseThrow(
                    () -> new AuthorError("AUTHOR_NOT_FOUND", String.valueOf(authorId))
            );
        } else {
            throw new AuthorError("AUTHOR_NOT_FOUND", "id = 0");
        }
    }

}
