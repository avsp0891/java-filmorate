package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.model.FilmValidation.filmValidation;

@Service
@Slf4j
public class FilmService {


    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Map<Integer, Film> getFilms() {
        return filmStorage.getFilms();
    }

    public IdGenerator getIdGenerator() {
        return filmStorage.getIdGenerator();
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilmById(Integer filmId) {
        if (!getFilms().containsKey(filmId)) {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        return filmStorage.getFilmById(filmId);
    }

    public Film addFilm(Film film) {
        filmValidation(film);
        return filmStorage.addFilm(film);
    }

    public Film changeFilmById(Integer id, Film film) {
        if (!getFilms().containsKey(id)) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new FilmNotFoundException("Фильм с id " + id + " не найден");
        }
        filmValidation(film);
        film.setId(id);
        return filmStorage.changeFilmById(id, film);
    }

    public Film changeFilm(Film film) {
        if (!getFilms().containsKey(film.getId())) {
            log.info("Фильм с идентификатором {} не найден.", film.getId());
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        filmValidation(film);
        film.setId(film.getId());
        return filmStorage.changeFilm(film);
    }


    public Film deleteFilmById(Integer filmId) {
        if (!getFilms().containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        return filmStorage.deleteFilmById(filmId);
    }


    public Film addLike(Integer filmId, Integer userId) throws UserNotFoundException, FilmNotFoundException {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return filmStorage.addLike(filmId, userId);
    }

    public Film deleteLike(Integer filmId, Integer userId) throws UserNotFoundException, FilmNotFoundException {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }


}
