package ru.yandex.practicum.filmorate.Exceptions;

public class FilmNotFoundException extends  RuntimeException {

    public FilmNotFoundException(String s) {
        super(s);
    }
}
