package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private User user;
    private Film film;
    private FilmController filmController;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private FilmService filmService;

    @BeforeEach
    private void setUp() {
        userStorage = new InMemoryUserStorage();
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
        film = new Film("Фильм", "Описание", LocalDate.of(1900, 12, 12), 120, new Mpa(1));
        user = new User("test@test.ru", "userLogin", "displayName", LocalDate.of(1950, 12, 12));

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
    void getFilmByIdStandard() {
        filmController.addFilm(film);
        assertEquals(film, filmController.getFilmById(1));
    }

    @Test
    void getFilmByIdFilmNotFound() {
        final FilmNotFoundException e = assertThrows(
                FilmNotFoundException.class,
                () -> filmController.getFilmById(1)
        );
        assertEquals("Фильм с id 1 не найден", e.getMessage());
    }

    @Test
    void deleteFilmByIdStandard() {
        filmController.addFilm(film);
        filmController.deleteFilmById(1);
        assertNull(filmStorage.getFilms().get(1));
    }

    @Test
    void deleteFilmByIdNotFound() {
        final FilmNotFoundException e = assertThrows(
                FilmNotFoundException.class,
                () -> filmController.deleteFilmById(1)
        );
        assertEquals("Фильм с id 1 не найден", e.getMessage());
    }

    @Test
    void addFilmStandard() {
        filmController.addFilm(film);
        assertEquals(film, filmStorage.getFilms().get(1));
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
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Неверная дата релиза фильма.", e.getMessage());
    }

    @Test
    void addFilmValidation4() {
        film.setDuration(-1);
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Продолжительность фильма не может быть отрицательной.", e.getMessage());
    }

    @Test
    void changeFilmStandard() {
        filmController.addFilm(film);
        Film film2 = new Film("Фильм2", "Описание2", LocalDate.of(1901, 12, 12), 180, new Mpa(2));
        filmController.changeFilm(1, film2);
        assertEquals(film2, filmStorage.getFilms().get(1));
    }

    @Test
    void changeFilmNotFound() {
        final FilmNotFoundException e = assertThrows(
                FilmNotFoundException.class,
                () -> filmController.changeFilm(0, film)
        );
        assertEquals("Фильм с id 0 не найден", e.getMessage());

    }

//    @Test
//    void getNextIdCount() {
//        assertEquals(1, filmStorage.getIdGenerator().getNextIdCount());
//        assertEquals(2, filmStorage.getIdGenerator().getNextIdCount());
//    }

    @Test
    void addLikeStandard() {
        userStorage.add(user);
        filmController.addFilm(film);
        filmController.addLike(1,1);
        assertEquals(1, filmStorage.getFilms().get(1).getLikeCount());
        assertEquals(Set.of(1), filmStorage.getFilms().get(1).getUsersWhoLikedTheMovie());
    }

    @Test
    void addLikeFilmNotFound() {
        userStorage.add(user);
        final FilmNotFoundException e = assertThrows(
                FilmNotFoundException.class,
                () -> filmController.addLike(1,1)
        );
        assertEquals("Фильм с id 1 не найден", e.getMessage());
    }

    @Test
    void addLikeUserNotFound() {
        filmController.addFilm(film);
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> filmController.addLike(1,1)
        );
        assertEquals("Пользователь с id 1 не найден", e.getMessage());
    }

    @Test
    void deleteLikeStandard() {
        userStorage.add(user);
        filmController.addFilm(film);
        filmController.addLike(1,1);
        filmController.deleteLike(1,1);
        assertEquals(0, filmStorage.getFilms().get(1).getLikeCount());
        assertEquals(Set.of(), filmStorage.getFilms().get(1).getUsersWhoLikedTheMovie());
    }

    @Test
    void deleteLikeFilmNotFound() {
        final FilmNotFoundException e = assertThrows(
                FilmNotFoundException.class,
                () -> filmController.deleteLike(1,1)
        );
        assertEquals("Фильм с id 1 не найден", e.getMessage());
    }

    @Test
    void deleteLikeUserNotFound() {
        filmController.addFilm(film);
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> filmController.deleteLike(1,1)
        );
        assertEquals("Пользователь с id 1 не найден", e.getMessage());
    }

    @Test
    void getPopularFilmsStandard() {
        userStorage.add(user);
        User user2 = new User("2@test.ru", "userLogin2", "displayName2", LocalDate.of(1960, 12, 12));
        Film film2 = new Film("Фильм2", "Описание2", LocalDate.of(1901, 12, 12), 180, new Mpa(2));
        userStorage.add(user);
        userStorage.add(user2);
        filmController.addFilm(film);
        filmController.addFilm(film2);
        filmController.addLike(1,1);
        filmController.addLike(2,1);
        filmController.addLike(2,2);
        assertEquals(List.of(film2,film), filmController.getPopularFilms(2));

    }
}