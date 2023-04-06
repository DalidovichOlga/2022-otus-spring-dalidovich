package ru.otus.spring.booklib.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.booklib.domain.Book;
import ru.otus.spring.booklib.domain.BookView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Book insert(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("authorid", book.getAuthorId());
        parameterSource.addValue("genreid", book.getGenreId());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("publisheryear", book.getPublicationYear());
        parameterSource.addValue("publisher", book.getPublisher());

        namedParameterJdbcOperations.update(
                "insert into book (title,publisheryear ,publisher ,authorid , genreid  )" +
                        " values (:title,  :publisheryear ,:publisher ,:authorid , :genreid)",
                parameterSource, keyHolder);
        book.setId(keyHolder.getKey().longValue());
        return book;
    }

    @Override
    public void update(Book book) {

        namedParameterJdbcOperations.update(
                "update book " +
                        "set title  = :title " +
                        " , publisheryear = :year " +
                        " , publisher = :publisher " +
                        " , authorid  = :authorid" +
                        " , genreid  = :genreid" +
                        " where id = :id",
                Map.of("title", book.getTitle(),
                        "year", book.getPublicationYear(), "publisher", book.getPublisher(),
                        "authorid", book.getAuthorId(), "genreid", book.getGenreId(),
                        "id", book.getId()
                ));


    }

    @Override
    public BookView getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        BookView bookView;
        try {
            bookView = namedParameterJdbcOperations.queryForObject(
                    "select b.* , a.shortName as author, g.genreName as genre from book b \n" +
                            " inner join authors a on a.id = b.authorid " +
                            " inner join genres g on g.id = genreid" +
                            " where b.id = :id", params, new BookDaoJdbc.BookViewMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            bookView = null;
        }
        return bookView;
    }

    @Override
    public List<BookView> getAll() {
        return namedParameterJdbcOperations.getJdbcOperations().query("select b.* , a.shortName as author, g.genreName as genre from book b" +
                        " inner join authors a on a.id = b.authorid" +
                        " left join genres g on g.id = genreid",
                new BookDaoJdbc.BookViewMapper());
    }

    @Override
    public List<BookView> getAllTitleAuthorGenre(String title, Long authorId, Long genreId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("authorid", authorId);
        parameterSource.addValue("genreid", genreId);
        parameterSource.addValue("title", title);
        return namedParameterJdbcOperations.query("select b.* , a.shortName as author, g.genreName as genre from book b" +
                        " inner join authors a on a.id = b.authorid" +
                        " left join genres g on g.id = genreid " +
                        " where b.authorid = :authorid " +
                        "   and b.genreid = :genreid" +
                        "   and b.title = :title "
                , parameterSource,
                new BookDaoJdbc.BookViewMapper());
    }

    @Override
    public List<BookView> getAllBookByAuthor(Long authorId) {
        Map<String, Object> params = Collections.singletonMap("id", authorId);
        return namedParameterJdbcOperations.query("select b.* , a.shortName as author, g.genreName as genre from book b" +
                        " inner join authors a on a.id = b.authorid" +
                        " left join genres g on g.id = genreid " +
                        " where b.authorid = :id ", params,
                new BookDaoJdbc.BookViewMapper());
    }

    @Override
    public List<BookView> getAllBookByGenre(Long genreId) {
        Map<String, Object> params = Collections.singletonMap("id", genreId);
        return namedParameterJdbcOperations.query("select b.* , a.shortName as author, g.genreName as genre from book b" +
                        " inner join authors a on a.id = b.authorid" +
                        " left join genres g on g.id = genreid " +
                        " where b.genreid = :id ", params,
                new BookDaoJdbc.BookViewMapper());
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from book where id = :id", params
        );
    }

    private static class BookViewMapper implements RowMapper<BookView> {

        @Override
        public BookView mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");

            String title = resultSet.getString("title");
            String year = resultSet.getString("publisheryear");
            String publisher = resultSet.getString("publisher");
            String author = resultSet.getString("author");
            String genre = resultSet.getString("genre");
            long authorId = resultSet.getLong("authorid");
            long genreId = resultSet.getLong("genreid");

            return new BookView(title, id, year, publisher, author, genre, authorId, genreId);
        }
    }
}
