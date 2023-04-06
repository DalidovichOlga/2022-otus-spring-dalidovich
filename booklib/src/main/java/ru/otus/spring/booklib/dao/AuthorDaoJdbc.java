package ru.otus.spring.booklib.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.booklib.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Author insert(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("firstName", author.getFirstName());
        parameterSource.addValue("lastname", author.getLastName());
        parameterSource.addValue("middleName", author.getMiddleName());
        parameterSource.addValue("shortName", author.getShortName());
        namedParameterJdbcOperations.update(
                "insert into authors (firstname, lastname, middlename ,shortname ) values (:firstName , :lastname , :middleName , :shortName)",
                parameterSource, keyHolder);
        author.setId(keyHolder.getKey().longValue());
        return author;
    }

    @Override
    public Author getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Author author;

        try {
            author = namedParameterJdbcOperations.queryForObject(
                    "select * from authors where id = :id", params, new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            author = null;
        }
        return author;
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.getJdbcOperations().query("select * from authors", new AuthorMapper());
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from authors where id = :id", params
        );
    }

    @Override
    public List<Author> getAuthorByShortName(String name) {
        Map<String, Object> params = Collections.singletonMap("shortname", name);
        return namedParameterJdbcOperations.query(
                "select * from authors where shortname = :shortname", params, new AuthorMapper()
        );
    }

    @Override
    public List<Author> getAuthorsByName(String authorName) {
        List<Author> authorByShortName = getAuthorByShortName(authorName);
        if (authorByShortName.isEmpty()) {
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
            authorByShortName = getAuthorByName(firstName, lastName, middleName);

        }
        return authorByShortName;
    }

    private List<Author> getAuthorByName(String firstName, String lastname, String middleName) {
        Map<String, Object> params = Map.of("firstname", firstName, "lastname", lastname,
                "middlename", middleName
        );
        if (!"".equals(firstName) && !"".equals(lastname) && !"".equals(middleName))
            return namedParameterJdbcOperations.query(
                    "select * from authors where firstname = :firstname \n" +
                            " and lastname = :lastname and middlename = :middlename"
                    , params, new AuthorMapper()
            );
        else if (!"".equals(firstName) && !"".equals(lastname) && "".equals(middleName))
            return namedParameterJdbcOperations.query(
                    "select * from authors where firstname = :firstname \n" +
                            " and lastname = :lastname "
                    , params, new AuthorMapper()
            );
        else if ("".equals(firstName) && !"".equals(lastname) && "".equals(middleName))
            return namedParameterJdbcOperations.query(
                    "select * from authors where lastname = :lastname "
                    , params, new AuthorMapper()
            );
        else {
            return List.of();
        }

    }


    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");

            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String middleName = resultSet.getString("middlename");
            String shortName = resultSet.getString("shortname");
            return new Author(firstName, lastName, middleName, shortName, id);
        }
    }
}
