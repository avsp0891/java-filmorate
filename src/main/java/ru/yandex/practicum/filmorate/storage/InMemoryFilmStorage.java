package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdGenerator;

import java.time.LocalDate;
import java.util.*;


@Slf4j
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

    private void filmValidation(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.warn("Передано пустое название фильма - " + film.getName());
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Превышена максимальная длина описания - " + film.getDescription().length());
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getDescription().isBlank() || film.getDescription() == null) {
            log.warn("Превышена максимальная длина описания - " + film.getDescription());
            throw new ValidationException("Описание фильма не может быть пустым.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.warn("Задана неверная дата релиза фильма - " + film.getReleaseDate());
            throw new ValidationException("Неверная дата релиза фильма.");
        }
        if (film.getDuration() < 0) {
            log.warn("Задана отрицательная продолжительность - " + film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }
    }
}
