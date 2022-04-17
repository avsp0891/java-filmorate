package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private Film film;
    private FilmController filmController;

    @BeforeEach
    private void setUp() {
        filmController = new FilmController();
        film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание");
        film.setReleaseDate(LocalDate.of(1900, 12,12));
        film.setFilmDuration(Duration.ofHours(2));

    }

    @Test
    void findAllStandard() {
        filmController.addFilm(film);
        ArrayList<Film> arrayList = new ArrayList<>();
        arrayList.add(film);
        assertEquals(arrayList, filmController.findAll());
    }

    @Test
    void findAllEmptyList() {
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    void addFilmStandard() {
        filmController.addFilm(film);
        assertEquals(film, filmController.getFilmRepository().get(0));
    }

    @Test
    void addFilmValidation1() {
        film.setName("");
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Название фильма не может быть пустым.", e.getMessage());
    }

    @Test
    void addFilmValidation2() {
        film.setDescription("текст на 200 символов, текст на 200 символов, текст на 200 символов," +
                " текст на 200 символов, текст на 200 символов, текст на 200 символов, текст на 200 символов," +
                " текст на 200 символов, текст на 200 символов");
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Максимальная длина описания — 200 символов.", e.getMessage());
    }

    @Test
    void addFilmValidation3() {
        film.setReleaseDate(LocalDate.of(1895,12,27));
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Неверная дата релиза фильма.", e.getMessage());
    }

    @Test
    void addFilmValidation4() {
        film.setFilmDuration(Duration.ofMinutes(-1));
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Продолжительность фильма не может быть отрицательной.", e.getMessage());
    }

    @Test
    void changeFilmStandard() {
        filmController.addFilm(film);
        Film film2 = new Film();
        film2.setName("Фильм2");
        film2.setDescription("Описание2");
        film2.setReleaseDate(LocalDate.of(1901, 12,12));
        film2.setFilmDuration(Duration.ofHours(3));
        filmController.changeFilm(0, film2);
        assertEquals(film2, filmController.getFilmRepository().get(0));
    }

    @Test
    void changeFilmNotFound() {
        filmController.changeFilm(0, film);
        assertNull(filmController.getFilmRepository().get(0));
    }

    @Test
    void getNextIdCount() {
        assertEquals(0, filmController.idGenerator.getNextIdCount());
        assertEquals(1, filmController.idGenerator.getNextIdCount());
    }
}