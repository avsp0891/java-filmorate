package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


@Slf4j
@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> map = new HashMap<>();
        for (Film film : findAll()) {
            map.put(film.getId(), film);
        }
        return map;
    }

    @Override
    public List<Film> findAll() {
        String sql = "select * from filmorate_film";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film getById(Integer filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from filmorate_film where FILM_ID = ?", filmId);
        if (filmRows.next()) {
            Film film = makeFilm(filmRows);
            film.setId(filmId);
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
    }

    @Override
    public Film add(Film film) {
        Integer id = getNextFilmId();
        String sql = "INSERT INTO filmorate_film (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES (?,?,?,?,?) ";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
//        film.setMpa(getMpa(film.getMpa().getId()));
        return getById(id);
    }

    @Override
    public Film changeById(Integer id, Film film) {
        String sql = "UPDATE filmorate_film SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? WHERE FILM_ID = '" + id + "'";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        return getById(id);
    }


    @Override
    public Film change(Film film) {
        String sql = "UPDATE filmorate_film SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? WHERE FILM_ID = '" + film.getId() + "'";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        return getById(film.getId());
    }

    @Override
    public Film deleteById(Integer filmId) {
        String sql = "DELETE FROM filmorate_film WHERE FILM_ID = ?";
        Film film = getById(filmId);
        jdbcTemplate.update(sql, filmId);
        return film;
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO filmorate_like (FILM_ID, USER_ID) VALUES (?,?)";
        jdbcTemplate.update(sql,
                filmId,
                userId);
        return getById(filmId);
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        String sql = " DELETE FROM filmorate_like WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql,
                filmId,
                userId);
        return getById(filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (getFilms().size() <= count) {
            count = getFilms().size();
        }
        String sql = "select FF.FILM_ID," +
                "FF.NAME," +
                "FF.DESCRIPTION," +
                "FF.RELEASE_DATE," +
                "FF.DURATION," +
                "FF.MPA_ID," +
                "COUNT(FL.FILM_ID) AS COUNT " +
                "FROM FILMORATE_FILM AS FF " +
                "LEFT JOIN FILMORATE_LIKE AS FL on FF.FILM_ID = FL.FILM_ID " +
                "GROUP BY FF.FILM_ID " +
                "ORDER BY COUNT DESC LIMIT " + count;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer filmId = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer duration = rs.getInt("duration");
        Mpa mpa = getMpa(rs.getInt("mpa_id"));
        Film film = new Film(name, description, releaseDate, duration, mpa);
        film.setId(filmId);
        film.setLikeCount(getCountFilmLikes(filmId));
        film.getUsersWhoLikedTheMovie().addAll(getUsersWhoLikedTheMovie(filmId));
        film.getGenres().addAll(getGenres(filmId));
        return film;
    }

    private Film makeFilm(SqlRowSet srs) {
        Integer filmId = srs.getInt("film_id");
        String name = srs.getString("name");
        String description = srs.getString("description");
        LocalDate releaseDate = srs.getDate("release_date").toLocalDate();
        Integer duration = srs.getInt("duration");
        Mpa mpa = getMpa(srs.getInt("mpa_id"));
        Film film = new Film(name, description, releaseDate, duration, mpa);
        film.setId(filmId);
        film.setLikeCount(getCountFilmLikes(filmId));
        film.getUsersWhoLikedTheMovie().addAll(getUsersWhoLikedTheMovie(filmId));
        film.getGenres().addAll(getGenres(filmId));
        return film;
    }

    private Integer getCountFilmLikes(Integer filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select COUNT(FILM_ID) AS count from FILMORATE_LIKE where FILM_ID = ?", filmId);
        if (filmRows.next())
            return filmRows.getInt("COUNT");
        else return 0;
    }

    private List<Integer> getUsersWhoLikedTheMovie(Integer filmId) {
        String sql = "select USER_ID from FILMORATE_LIKE where FILM_ID = " + filmId;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"));
    }

    private Mpa getMpa(Integer filmId) {
        String sql = "select MPA_NAME from FILMORATE_MPA where MPA_ID = " + filmId;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql);
        String mpaName = "";
        if (filmRows.next()) {
            mpaName =  filmRows.getString("MPA_NAME");
        }
        return new Mpa(filmId, mpaName);
    }

    private List<Integer> getGenres(Integer filmId) {
        String sql = "select GENRE_ID from FILMORATE_FILM_GENRE where FILM_ID = " + filmId;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("genre_id"));
    }

    private Integer getNextFilmId() {
        Integer max = jdbcTemplate.queryForObject("select MAX(film_id) MAX from filmorate_film", new Object[]{}, Integer.class);
        if (max == null) {
            return 1;
        } else return ++max;
    }


}
