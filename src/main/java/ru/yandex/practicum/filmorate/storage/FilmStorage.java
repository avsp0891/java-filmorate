package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getFilms();

    List<Film> findAll();

    Film getById(Integer filmId);

    Film add(Film film);

    Film changeById(Integer id, Film film);

    Film change(Film film);

    Film deleteById(Integer filmId);

    Film addLike(Integer filmId, Integer userId);

    Film deleteLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(Integer count);

}
