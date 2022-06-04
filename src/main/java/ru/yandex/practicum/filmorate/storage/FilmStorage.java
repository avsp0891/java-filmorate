package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getFilms();

    IdGenerator getIdGenerator();

    List<Film> findAll();

    Film getFilmById(Integer filmId);

    Film addFilm(Film film);

    Film changeFilmById(Integer id, Film film);

    Film changeFilm(Film film);

    Film deleteFilmById(Integer filmId);

    Film addLike(Integer filmId, Integer userId);

    Film deleteLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(Integer count);

}
