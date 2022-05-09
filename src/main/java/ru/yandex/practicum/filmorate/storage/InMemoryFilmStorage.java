package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import java.util.*;

import static ru.yandex.practicum.filmorate.model.FilmValidation.filmValidation;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final  Map<Integer, Film> films = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        return films.get(filmId);
    }

    @Override
    public Film addFilm(Film film) {
        filmValidation(film);
        film.setId(idGenerator.getNextIdCount());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film changeFilmById(Integer id, Film film) {
        if (films.containsKey(id)) {
            filmValidation(film);
            film.setId(id);
            films.put(film.getId(), film);
            return film;
        }
        return null;
    }

    @Override
    public Film changeFilm(Film film) {
        if (films.containsKey(film.getId())) {
            filmValidation(film);
            films.put(film.getId(), film);
            return film;
        }
        return null;
    }

    @Override
    public Film deleteFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден");
        }
        return films.remove(filmId);
    }

}
