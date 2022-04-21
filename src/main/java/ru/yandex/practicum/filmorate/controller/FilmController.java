package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdGenerator;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> filmRepository = new HashMap<>();
    IdGenerator idGenerator = new IdGenerator();

    @GetMapping
    public List<Film> findAll() {
        log.debug("Получение списка всех фильмов");
        return new ArrayList<>(filmRepository.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.debug("Добавление нового фильма : \n{}", film);
        filmValidation(film);
        film.setId(idGenerator.getNextIdCount());
        filmRepository.put(film.getId(), film);
        return film;
    }

    @PutMapping("/{id}")
    public Film changeFilm(@PathVariable(value = "id") Integer filmId, @RequestBody Film film) {
        log.debug("Изменение нового фильма : \n{}", film);
        if (filmRepository.containsKey(filmId)) {
            filmValidation(film);
            film.setId(filmId);
            filmRepository.put(film.getId(), film);
            return film;
        }
        return null;
    }

    @PutMapping
    public Film changeFilm(@RequestBody Film film) {
        log.debug("Изменение нового фильма : \n{}", film);
        if (filmRepository.containsKey(film.getId())) {
            filmValidation(film);
            filmRepository.put(film.getId(), film);
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
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.warn("Задана неверная дата релиза фильма - " + film.getReleaseDate());
            throw new ValidationException("Неверная дата релиза фильма.");
        }
        if (film.getDuration().isNegative()) {
            log.warn("Задана отрицательная продолжительность - " + film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }
    }

}
