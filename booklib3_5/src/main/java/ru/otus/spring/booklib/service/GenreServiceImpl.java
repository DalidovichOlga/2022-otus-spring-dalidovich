package ru.otus.spring.booklib.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.GenreRepository;
import ru.otus.spring.booklib.dao.BookRepository;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.LibraryError;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Override
    public List<Genre> getAllGaner() {
        return genreRepository.findAll();
    }

    @Transactional
    public void deleteGenre(Long genreId) throws LibraryError {
        Boolean existsByGenreId = bookRepository.existsByGenreId(genreId);
        if (!existsByGenreId) {
            genreRepository.deleteById(genreId);
        } else
            throw new LibraryError("GANRENAME_USED_IN_BOOK", "id=" + genreId);

    }

    @Override
    @Transactional
    public Genre getOrCreateGenreByParam(String genreName, Long genreId) throws LibraryError {
        if (genreId == 0 && !"".equals(genreName)) {
            List<Genre> genreList = genreRepository.findByGenreName(genreName);
            if (genreList.isEmpty()) {
                return genreRepository.save(new Genre(genreName));
            } else
                return genreList.get(0);

        }

        return genreRepository.findById(genreId).orElseThrow(
                () -> new LibraryError("GANRENAME_NOT_FOUND", String.valueOf(genreId)));
    }

    @Override
    @Transactional
    public Genre updateGenre(Genre genre) throws LibraryError {
        if ("".equals(genre.getGenreName()))
            throw new LibraryError("GANRENAME_NOT_PASSED", "<empty>");
        if (genre.getId() == 0)
            throw new LibraryError("GANRENAME_NOT_PASSED", "<empty>");

        List<Genre> genreList = genreRepository.findByGenreName(genre.getGenreName());
        if (!genreList.isEmpty()) {
            if (genreList.size() > 1)
                throw new LibraryError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());
            else if (genreList.get(0).getId() != genre.getId()) {
                throw new LibraryError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());
            } else return null;  // иначе ничего менять не надо все и так как хочет пользователь

        }
        return genreRepository.save(genre);

    }

    @Override
    @Transactional
    public Genre createGenre(Genre genre) throws LibraryError {
        if ("".equals(genre.getGenreName())) {
            throw new LibraryError("GANRENAME_NOT_PASSED", "<empty>");
        }
        List<Genre> genreList = genreRepository.findByGenreName(genre.getGenreName());
        if (!genreList.isEmpty()) {
            throw new LibraryError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());
        }
        return genreRepository.save(genre);
    }

}
