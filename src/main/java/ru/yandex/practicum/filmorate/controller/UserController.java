package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> userRepository = new HashMap<>();
    IdGenerator idGenerator = new IdGenerator();

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(userRepository.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        userValidation(user);
        user.setId(idGenerator.getNextIdCount());
        userRepository.put(user.getId(), user);
        return user;
    }

    @PutMapping("/{id}")
    public User changeUser(@PathVariable(value = "id") Integer userId, @RequestBody User user) {
        if (userRepository.containsKey(userId)) {
            userValidation(user);
            user.setId(userId);
            userRepository.put(user.getId(), user);
            return user;
        }
       return null;
    }


    private void userValidation(User user) {
        if (user.getEmail().isBlank() || user.getEmail() == null) {
            log.warn("Передан пустой адрес электронной почты - " + user.getEmail());
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Адрес электронной почты не содержит символ  '@' - " + user.getEmail());
            throw new ValidationException("Адрес электронной почты должен содержать символ '@'.");
        }
        if (user.getUserLogin().isBlank() || user.getUserLogin() == null) {
            log.warn("Передан пустой логин пользователя - " + user.getUserLogin());
            throw new ValidationException("Логин пользователя не может быть пустым.");
        }
        if (user.getUserLogin().contains(" ")) {
            log.warn("Логин пользователя содержит пробелы - " + user.getUserLogin());
            throw new ValidationException("Логин пользователя не должен содержать пробелы.");
        }
        if (user.getDateOfBirth().isAfter(LocalDate.now())) {
            log.warn("Неверная дата рождения - " + user.getDateOfBirth());
            throw new ValidationException("Неверная дата рождения.");
        }
        if (user.getDisplayName().isBlank() || user.getDisplayName() == null) {
            user.setDisplayName(user.getUserLogin());
        }
    }

}
