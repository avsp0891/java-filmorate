package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;



@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Integer, User> users = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer userId) throws UserNotFoundException {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return users.get(userId);
    }

    @Override
    public User addUser(User user) {
        userValidation(user);
        user.setId(idGenerator.getNextIdCount());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User changeUserById(Integer id, User user) {
        if (users.containsKey(id)) {
            userValidation(user);
            user.setId(id);
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    @Override
    public User changeUser(User user) {
        if (users.containsKey(user.getId())) {
            userValidation(user);
            users.put(user.getId(), user);
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
