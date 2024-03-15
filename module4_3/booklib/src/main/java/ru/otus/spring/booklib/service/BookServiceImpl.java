package ru.otus.spring.booklib.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.*;
import ru.otus.spring.booklib.domain.*;
import ru.otus.spring.booklib.dto.BookAddDto;
import ru.otus.spring.booklib.dto.BookDto;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookRepository bookRepository;
    private final CommentService commentService;

    @Override
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public List<Book> getBookByAuthor(Long id, String name) throws LibraryError {
        Author author = null;
        author = authorService.getAuthorByParam(name, id);

        return bookRepository.findByAuthorId(author.getId());
    }

    @Transactional
    @Override
    public Book modifyBook(BookDto bookDto) throws LibraryError {
        Book book = bookRepository.findById(bookDto.getId()).orElseThrow(() ->
                new LibraryError("book_not_found", String.valueOf(bookDto.getId())));

        Long OldAuthorId = 0L;
        Long OldGenreId = 0L;

        // разберемся с автором
        if (bookDto.getAuthor() != null && (!bookDto.getAuthor().isEmpty())) {
            OldAuthorId = book.getAuthor().getId();
            Author authorByParam = authorService.getOrCreateAuthorByParam(bookDto.getAuthor(), 0L);
            if (authorByParam.getId() == OldAuthorId)
                OldAuthorId = 0L;
            book.setAuthor(authorByParam);
        }

        //теперь жанр определить как объект базы
        if ((!"".equals(bookDto.getGenre()))) {
            OldGenreId = book.getGenre().getId();
            Genre genreByParam = genreService.getOrCreateGenreByParam(bookDto.getGenre(), 0L);
            if (genreByParam.getId() == OldGenreId)
                OldGenreId = 0L;
            book.setGenre(genreByParam);
        }

        book.setTitle("".equals(bookDto.getTitle()) ? book.getTitle() : bookDto.getTitle());
        bookRepository.save(book);

        if (OldAuthorId > 0) {
            try {
                authorService.deleteAuthor(OldAuthorId);
            } catch (LibraryError libraryError) { //подавим исключение, так как значит автора удалять рано
            }
        }

        if (OldGenreId > 0) {
            try {
                genreService.deleteGenre(OldGenreId);
            } catch (LibraryError genreError) { //подавим исключение, так как жанр удалять рано
            }
        }
        return book;
    }

    @Transactional
    @Override
    public Book createBook(BookAddDto bookAddDto) throws LibraryError {
        if ("".equals(bookAddDto.getTitle()) || "".equals(bookAddDto.getGenre()))
            throw new LibraryError("attribute_not_passed", bookAddDto.getTitle());

        Genre genre = genreService.getOrCreateGenreByParam(bookAddDto.getGenre(), 0L);

        Author author = authorService.getOrCreateAuthorByParam(bookAddDto.getAuthor(), 0L);

        List<Book> allTitleAuthorGenre = bookRepository.findByTitleAndAuthorId(
                bookAddDto.getTitle(), author.getId());
        if (!allTitleAuthorGenre.isEmpty()) {
            throw new LibraryError("book_already_exists", bookAddDto.getTitle());
        }

        Book book = new Book(bookAddDto.getTitle(), author, genre);
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public void removeBook(Long id) throws LibraryError {
        Book book = bookRepository.findById(id).orElseThrow(() -> new LibraryError("book_not_found", "id =" + String.valueOf(id)));
        long genreId = book.getGenre().getId();
        long authorId = book.getAuthor().getId();
        bookRepository.delete(book);

        try {
            authorService.deleteAuthor(authorId);
        } catch (LibraryError libraryError) {
            //подавим исключение, так как значит автора удалять рано
        }

        try {
            genreService.deleteGenre(genreId);
        } catch (LibraryError genreError) { //подавим исключение, так как жанр удалять рано
        }

        //здесь должно было быть событие, но мы проходим rest
        commentService.ClearComment(id);

    }

    @Override
    public Book getById(Long id) throws LibraryError {
        return bookRepository.findById(id).orElseThrow(() -> new LibraryError("book_not_found", "id =" + String.valueOf(id)));
    }


}
