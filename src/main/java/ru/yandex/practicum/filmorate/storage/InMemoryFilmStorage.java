package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdGenerator;

import java.util.*;


@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(Integer filmId) {
        return films.get(filmId);
    }

    @Override
    public Film add(Film film) {
        film.setId(idGenerator.getNextIdCount());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film changeById(Integer id, Film film) {
        film.setId(id);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film change(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteById(Integer filmId) {
        return films.remove(filmId);
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        getFilms().get(filmId).getUsersWhoLikedTheMovie().add(userId);
        getFilms().get(filmId).setLikeCount(getFilms().get(filmId).getUsersWhoLikedTheMovie().size());
        return getFilms().get(filmId);
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        getFilms().get(filmId).getUsersWhoLikedTheMovie().remove(userId);
        getFilms().get(filmId).setLikeCount(getFilms().get(filmId).getUsersWhoLikedTheMovie().size());
        return getFilms().get(filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (getFilms().size() <= count) {
            count = getFilms().size();
        }
        List<Film> list = new ArrayList<>(getFilms().values());
        list.sort((film1, film2) -> film2.getLikeCount() - film1.getLikeCount());
        return list.subList(0, count);
    }

}
