package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;

import java.time.LocalDate;

@Slf4j
public class UserValidation {

    public static void userValidation(User user) {
        if (user.getEmail().isBlank() || user.getEmail() == null) {
            log.warn("Передан пустой адрес электронной почты - " + user.getEmail());
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Адрес электронной почты не содержит символ  '@' - " + user.getEmail());
            throw new ValidationException("Адрес электронной почты должен содержать символ '@'.");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null) {
            log.warn("Передан пустой логин пользователя");
            throw new ValidationException("Логин пользователя не может быть пустым.");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя содержит пробелы - " + user.getLogin());
            throw new ValidationException("Логин пользователя не должен содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Неверная дата рождения - " + user.getBirthday());
            throw new ValidationException("Неверная дата рождения.");
        }
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
