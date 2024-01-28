package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.booklib.dao.BookDao;
import ru.otus.spring.booklib.dao.GenreDao;
import ru.otus.spring.booklib.domain.BookView;
import ru.otus.spring.booklib.domain.Genre;
import ru.otus.spring.booklib.error.GenreError;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;
    private final BookDao bookDao;

    @Autowired
    public GenreServiceImpl(GenreDao genreDao, BookDao bookDao) {
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }

    @Override
    public List<Genre> getAllGaner() {
        return genreDao.getAll();
    }


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
        Genre genre1 = genreDao.getById(genreId);
        if (genre1 == null)
            throw new GenreError("GANRENAME_NOT_FOUND", String.valueOf(genre.getId()));

        final List<BookView> allbyGenre = bookDao.getAllBookByGenre(genreId);
        if (allbyGenre.isEmpty())
            genreDao.deleteById(genreId);
        else
            throw new GenreError("GANRENAME_USED_IN_BOOK", genre1.getGenreName());

    }


    @Override
    public Genre createGenre(Genre genre) throws GenreError {
        if ("".equals(genre.getGenreName()))
            throw new GenreError("GANRENAME_NOT_PASSED", "<empty>");
        List<Genre> genreList = genreDao.getGenreByName(genre.getGenreName());
        if (!genreList.isEmpty())
            throw new GenreError("GANRENAME_ALLREADY_EXISTS", genre.getGenreName());

        return genreDao.insert(genre);
    }

}
