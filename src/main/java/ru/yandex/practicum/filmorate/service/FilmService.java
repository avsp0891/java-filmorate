package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(Integer filmId, Integer userId) throws UserNotFoundException {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!filmStorage.getFilms().get(filmId).getUsersWhoLikedTheMovie().contains(userId)) {
            filmStorage.getFilms().get(filmId).getUsersWhoLikedTheMovie().add(userId);
            int i = filmStorage.getFilms().get(filmId).getLikeCount();
            i++;
            filmStorage.getFilms().get(filmId).setLikeCount(i);
        }
        return  filmStorage.getFilms().get(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) throws UserNotFoundException {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (filmStorage.getFilms().get(filmId).getUsersWhoLikedTheMovie().contains(userId)) {
            filmStorage.getFilms().get(filmId).getUsersWhoLikedTheMovie().remove(userId);
            int i = filmStorage.getFilms().get(filmId).getLikeCount();
            i--;
            filmStorage.getFilms().get(filmId).setLikeCount(i);
        }
        return  filmStorage.getFilms().get(filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        if (filmStorage.getFilms().size() <= count) {
            count = filmStorage.getFilms().size();
        }
        List<Film> list = new ArrayList<>(filmStorage.getFilms().values());
        list.sort((film1, film2) -> film2.getLikeCount()- film1.getLikeCount());
        return list.subList(0, count);
    }

}
