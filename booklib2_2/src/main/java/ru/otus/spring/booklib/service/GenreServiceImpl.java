package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.booklib.dao.GenreRepositoryJpa;
import ru.otus.spring.booklib.dao.BookRepositoryJpa;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.GenreError;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepositoryJpa genreDao;
    private final BookRepositoryJpa bookDao;

    @Autowired
    public GenreServiceImpl(GenreRepositoryJpa genreDao, BookRepositoryJpa bookDao) {
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }

    @Override
    public List<Genre> getAllGaner() {
        return genreDao.getAll();
    }

    @Transactional
    public void deleteGenre(Genre genre) throws GenreError {
        Long genreId = genre.getId();
        if (genreId == null || genreId == 0)
            if (!"".equals(genre.getGenreName())) {
                List<Genre> genreList = genreDao.getGenreByName(genre.getGenreName());
                if (genreList.isEmpty())
                    throw new GenreError("GANRENAME_NOT_FOUND", String.valueOf(genre.getGenreName()));
                if (genreList.size() > 1)
                    throw new GenreError("GANRENAME_NOT_FOUND", String.valueOf(genre.getGenreName()));
                genreId = genreList.get(0).getId();
            }
        Optional<Genre> genre1 = genreDao.getById(genreId);
        if (genre1 == null)
            throw new GenreError("GANRENAME_NOT_FOUND", String.valueOf(genre.getId()));

        final List<BookView> allbyGenre = bookDao.getAllBookByGenre(genreId);
        if (allbyGenre.isEmpty())
            genreDao.deleteById(genreId);
        else
            throw new GenreError("GANRENAME_USED_IN_BOOK", genre1.get().getGenreName());

    }

    @Override
    @Transactional
    public void updateGenre(Genre genre) throws GenreError {
        if ("".equals(genre.getGenreName()))
            throw new GenreError("GANRENAME_NOT_PASSED", "<empty>");
        if (genre.getId() == 0)
            throw new GenreError("GANRENAME_NOT_PASSED", "<empty>");

        List<Genre> genreList = genreDao.getGenreByName(genre.getGenreName());
        if (!genreList.isEmpty()) {
            if (genreList.size() > 1)
                throw new GenreError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());
            else if (genreList.get(0).getId() != genre.getId()) {
                throw new GenreError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());
            } else return;  // иначе ничего менять не надо все и так как хочет пользователь

        }
        genreDao.update(genre.getId(), genre.getGenreName());

    }

    @Override
    @Transactional
    public Genre createGenre(Genre genre) throws GenreError {
        if ("".equals(genre.getGenreName()))
            throw new GenreError("GANRENAME_NOT_PASSED", "<empty>");
        List<Genre> genreList = genreDao.getGenreByName(genre.getGenreName());
        if (!genreList.isEmpty())
            throw new GenreError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());

        return genreDao.insert(genre);
    }

}
