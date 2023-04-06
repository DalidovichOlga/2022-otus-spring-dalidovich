package ru.otus.spring.booklib.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.booklib.domain.Author;
import ru.otus.spring.booklib.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }


    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String genreName = resultSet.getString("genreName");
            return new Genre(genreName, id);
        }
    }

    @Override
    public Genre insert(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("genreName", genre.getGenreName());

        namedParameterJdbcOperations.update(
                "insert into genres (genrename) values (:genreName )",
                parameterSource, keyHolder);
        genre.setId(keyHolder.getKey().longValue());
        return genre;
    }

    @Override
    public Genre getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Genre genre;
        try {
            genre = namedParameterJdbcOperations.queryForObject(
                    "select * from genres where id = :id", params, new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            genre = null;
        }
        return genre;

    }

    @Override
    public List<Genre> getGenreByName(String name) {
        Map<String, Object> params = Collections.singletonMap("genrename", name);
        return namedParameterJdbcOperations.query(
                "select * from genres where genrename = :genrename", params, new GenreDaoJdbc.GenreMapper()
        );
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.getJdbcOperations().query("select * from genres", new GenreDaoJdbc.GenreMapper());
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from genres where id = :id", params
        );
    }
}
