package ru.yandex.practicum.filmorate.Exceptions;

public class UserNotFoundException extends  RuntimeException {

    public UserNotFoundException(String s) {
        super(s);
    }
}
