package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.AuthorRepositoryJpa;
import ru.otus.spring.booklib.dao.BookRepositoryJpa;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.error.LibraryError;

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
    public void deleteAuthor(Long id) throws LibraryError {
        final List<Book> allbyAuthor = bookRepository.findByAuthorId(id);
        if (allbyAuthor.isEmpty())
            authorRepository.deleteById(id);
        else throw new LibraryError("AUTHOR_HAS_BOOK", "id = " + id);
    }

    @Override
    @Transactional
    public List<Author> getAllAuthor() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public Author createAuthor(Author author) throws LibraryError {
        if (author.getLastName() == null || "".equals(author.getFirstName()) || "".equals(author.getLastName()))
            throw new LibraryError("AUTHORNAME_NOT_PASSED", "<empty>");


        List<Author> authorList = authorRepository.findByShortName(author.getShortName());
        if (!authorList.isEmpty()) {
            Optional<Author> author1 = authorList.stream().filter(a -> a.getFullName().equals(author.getFullName())).findFirst();
            if (author1.isPresent())
                return author1.get();
        }

        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public Author updateAuthor(Long id, String lastName, String firstName, String middleName) throws LibraryError {
        if ( (lastName == null || "".equals(lastName)) &&
                (firstName == null ||  "".equals(firstName) )&&
                (middleName == null ||  "".equals(middleName) ) || id == null || id == 0)
            throw new LibraryError("AUTHORNAME_NOT_PASSED", "<empty>");


        Optional<Author> byId = authorRepository.findById(id);
        if (!byId.isPresent())
            throw new LibraryError("AUTHORNAME_NOT_PASSED", "<empty>");

        firstName = ("".equals(firstName)) ? byId.get().getFirstName() : firstName;
        lastName = ("".equals(lastName)) ? byId.get().getLastName() : lastName;
        middleName = ("".equals(middleName)) ? byId.get().getMiddleName() : middleName;

        Author author = new Author(firstName, lastName, middleName);

        byId.get().setFirstName(author.getFirstName());
        byId.get().setLastName(author.getLastName());
        byId.get().setMiddleName(author.getMiddleName());
        byId.get().setShortName(author.getShortName());
        return authorRepository.save(byId.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorByParam(String authorName, Long authorId) throws LibraryError {
        if (authorId == 0 && !authorName.isEmpty()) {
            List<Author> authorByShortName = getAuthorsByName(authorName);
            if (authorByShortName == null || authorByShortName.isEmpty())
                throw new LibraryError("AUTHOR_NOT_FOUND", authorName);
            return authorByShortName.get(0);
        } else if (authorId > 0) {
            return authorRepository.findById(authorId).orElseThrow(
                    () -> new LibraryError("AUTHOR_NOT_FOUND", String.valueOf(authorId))
            );
        } else
            throw new LibraryError("AUTHOR_NOT_FOUND", String.valueOf(authorId));
    }

    private Author makeAuthorByString(String authorName) {
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
        return new Author(firstName, lastName, middleName);
    }

    private Author insertByName(String authorName) {
        //  проверка, что это похоже на имя автора
        Author author = makeAuthorByString(authorName);
        if (author == null)
            return null;

        return authorRepository.save(author);
    }

    private List<Author> getAuthorsByName(String authorName) {
        List<Author> authorByShortName = authorRepository.findByShortName(authorName);
        if (authorByShortName.isEmpty()) {
            Author author = makeAuthorByString(authorName);

            if (author != null) {
                if (!"".equals(author.getFirstName()) && !"".equals(author.getLastName()) && !"".equals(author.getMiddleName())) {
                    authorByShortName = authorRepository.findByFirstNameAndLastNameAndMiddleName(author.getFirstName(), author.getLastName(), author.getMiddleName());
                } else if (!"".equals(author.getFirstName()) && !"".equals(author.getLastName())) {
                    authorByShortName = authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
                } else if (!"".equals(author.getLastName())) {
                    authorByShortName = authorRepository.findByLastName(author.getLastName());
                } else {
                    return List.of();
                }
            } else return List.of();
        }
        return authorByShortName;
    }

    @Transactional
    public Author getOrCreateAuthorByParam(String authorName, Long authorId) throws LibraryError {
        if (authorId == 0 && !authorName.isEmpty()) {
            List<Author> authorByShortName = getAuthorsByName(authorName);
            if (authorByShortName == null || authorByShortName.isEmpty()) {
                Author author = insertByName(authorName);
                if (author == null)
                    throw new LibraryError("AUTHOR_NOT_FOUND", authorName);
                return author;
            } else return authorByShortName.get(0);
        } else if (authorId > 0) {
            return authorRepository.findById(authorId).orElseThrow(
                    () -> new LibraryError("AUTHOR_NOT_FOUND", String.valueOf(authorId))
            );
        } else {
            throw new LibraryError("AUTHOR_NOT_FOUND", "id = 0");
        }
    }

}
