package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Получение списка всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(value = "id") Integer id) {
        log.debug("Получение фильма : {}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.debug("Получение списка популярных фильмов");
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("Добавление нового фильма : \n{}", film);
        return filmService.addFilm(film);
    }

    @PutMapping("/{id}")
    public Film changeFilm(@PathVariable(value = "id") Integer id, @Valid @RequestBody Film film) {
        log.debug("Изменение нового фильма : \nid - {} \nbody - {}", id, film);
        return filmService.changeFilmById(id, film);
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.debug("Изменение нового фильма : \n{}", film);
        return filmService.changeFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable(value = "filmId") Integer filmId, @PathVariable(value = "userId") Integer userId) {
        log.debug("Пользователь {} ставит лайк фильму {}",userId,  filmId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable(value = "filmId") Integer filmId, @PathVariable(value = "userId") Integer userId) {
        log.debug("Пользователь {} удаляет лайк фильму {}",userId, filmId);
        return filmService.deleteLike(filmId, userId);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilmById(@PathVariable(value = "id") Integer id) {
        log.debug("Удаление фильма : {}", id);
        return filmService.deleteFilmById(id);
    }


}
