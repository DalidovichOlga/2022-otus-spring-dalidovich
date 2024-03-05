package ru.otus.spring.booklib.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.AuthorRepository;
import ru.otus.spring.booklib.dao.BookRepository;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.dto.AuthorFioDto;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Author getById(Long authorId) throws LibraryError {
        return authorRepository.findById(authorId).orElseThrow(() -> new LibraryError("AUTHOR_NOT_FOUND", String.valueOf(authorId)));
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) throws LibraryError {
        final List<Book> allbyAuthor = bookRepository.findByAuthorId(id);
        if (allbyAuthor.isEmpty()) {
            authorRepository.deleteById(id);
        } else throw new LibraryError("AUTHOR_HAS_BOOK", "id = " + id);
    }

    @Override
    @Transactional
    public List<Author> getAllAuthor() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public Author createAuthor(AuthorFioDto authorFioDto) throws LibraryError {
        Author author = AuthorFioDto.toAuthor(authorFioDto);
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
    public Author updateAuthor(AuthorFioDto authorFioDto) throws LibraryError {
        if ((authorFioDto.getLastName() == null || "".equals(authorFioDto.getLastName())) &&
                (authorFioDto.getFirstName() == null || "".equals(authorFioDto.getFirstName())) &&
                (authorFioDto.getMiddleName() == null || "".equals(authorFioDto.getMiddleName())) ||
                authorFioDto.getId() == 0)
            throw new LibraryError("AUTHORNAME_NOT_PASSED", "<empty>");


        Optional<Author> byId = authorRepository.findById(authorFioDto.getId());
        if (!byId.isPresent())
            throw new LibraryError("AUTHORNAME_NOT_PASSED", "<empty>");

        authorFioDto.setFirstName(("".equals(authorFioDto.getFirstName())) ? byId.get().getFirstName() : authorFioDto.getFirstName());
        authorFioDto.setLastName(("".equals(authorFioDto.getLastName())) ? byId.get().getLastName() : authorFioDto.getLastName());
        authorFioDto.setMiddleName(("".equals(authorFioDto.getMiddleName())) ? byId.get().getMiddleName() : authorFioDto.getMiddleName());

        Author author = new Author(authorFioDto.getFirstName(), authorFioDto.getLastName(), authorFioDto.getMiddleName());

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
        }
        return authorRepository.findById(authorId).orElseThrow(
                () -> new LibraryError("AUTHOR_NOT_FOUND", String.valueOf(authorId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorByParam(Long authorId) throws LibraryError {
        return authorRepository.findById(authorId).orElseThrow(
                () -> new LibraryError("AUTHOR_NOT_FOUND", String.valueOf(authorId))
        );
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
        if (author == null) {
            return null;
        }

        return authorRepository.save(author);
    }

    private List<Author> getAuthorsByName(String authorName) {
        List<Author> authorByShortName = authorRepository.findByShortName(authorName);
        if (!authorByShortName.isEmpty()) {
            return authorByShortName;
        }

        Author author = makeAuthorByString(authorName);
        if (author != null) {
            if (!"".equals(author.getFirstName()) && !"".equals(author.getLastName()) && !"".equals(author.getMiddleName())) {
                authorByShortName = authorRepository.findByFirstNameAndLastNameAndMiddleName(author.getFirstName(), author.getLastName(), author.getMiddleName());
                return authorByShortName;
            }

            if (!"".equals(author.getFirstName()) && !"".equals(author.getLastName())) {
                authorByShortName = authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
                return authorByShortName;
            }

            if (!"".equals(author.getLastName())) {
                authorByShortName = authorRepository.findByLastName(author.getLastName());
                return authorByShortName;
            }
        }
        return List.of();
    }

    @Transactional
    public Author getOrCreateAuthorByParam(String authorName, Long authorId) throws LibraryError {
        if (authorId == 0 && !authorName.isEmpty()) {
            List<Author> authorByShortName = getAuthorsByName(authorName);
            if (!authorByShortName.isEmpty())
                return authorByShortName.get(0);

            Author author = insertByName(authorName);
            if (author == null)
                throw new LibraryError("AUTHOR_NOT_FOUND", authorName);
            return author;
        }

        return authorRepository.findById(authorId).orElseThrow(
                () -> new LibraryError("AUTHOR_NOT_FOUND", String.valueOf(authorId))
        );
    }
}
