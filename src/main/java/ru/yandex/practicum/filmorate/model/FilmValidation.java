package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;

import java.time.LocalDate;

@Slf4j
public class FilmValidation {

    public static void filmValidation(Film film) {
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
