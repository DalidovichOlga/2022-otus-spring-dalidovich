package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.GenreRepositoryJpa;
import ru.otus.spring.booklib.dao.BookRepositoryJpa;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepositoryJpa genreRepository;
    private final BookRepositoryJpa bookRepository;

    @Autowired
    public GenreServiceImpl(GenreRepositoryJpa genreRepository, BookRepositoryJpa bookRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Genre> getAllGaner() {
        return genreRepository.getAll();
    }

    @Transactional
    public void deleteGenre(Long genreId) throws LibraryError {
        final List<BookView> allbyGenre = bookRepository.getAllBookByGenre(genreId);
        if (allbyGenre.isEmpty())
            genreRepository.deleteById(genreId);
        else
            throw new LibraryError("GANRENAME_USED_IN_BOOK", "id=" + genreId);

    }

    @Override
    @Transactional
    public Genre getOrCreateGenreByParam(String genreName, Long genreId) throws LibraryError {
        if (genreId == 0 && !"".equals(genreName)) {
            List<Genre> genreList = genreRepository.getGenreByName(genreName);
            if (genreList.isEmpty()) {
                Genre genre = genreRepository.insert(new Genre(genreName));
                return genre;
            } else
                return genreList.get(0);

        } else if (genreId > 0) {
            Optional<Genre> genre = genreRepository.getById(genreId);
            if (genre.isPresent())
                return genre.get();

            throw new LibraryError("GANRENAME_NOT_FOUND", String.valueOf(genreId));

        } else
            throw new LibraryError("GANRENAME_NOT_FOUND", String.valueOf(genreId));
    }

    @Override
    @Transactional
    public void updateGenre(Genre genre) throws LibraryError {
        if ("".equals(genre.getGenreName()))
            throw new LibraryError("GANRENAME_NOT_PASSED", "<empty>");
        if (genre.getId() == 0)
            throw new LibraryError("GANRENAME_NOT_PASSED", "<empty>");

        List<Genre> genreList = genreRepository.getGenreByName(genre.getGenreName());
        if (!genreList.isEmpty()) {
            if (genreList.size() > 1)
                throw new LibraryError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());
            else if (genreList.get(0).getId() != genre.getId()) {
                throw new LibraryError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());
            } else return;  // иначе ничего менять не надо все и так как хочет пользователь

        }
        genreRepository.update(genre.getId(), genre.getGenreName());

    }

    @Override
    @Transactional
    public Genre createGenre(Genre genre) throws LibraryError {
        if ("".equals(genre.getGenreName()))
            throw new LibraryError("GANRENAME_NOT_PASSED", "<empty>");
        List<Genre> genreList = genreRepository.getGenreByName(genre.getGenreName());
        if (!genreList.isEmpty())
            throw new LibraryError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());

        return genreRepository.insert(genre);
    }

}
