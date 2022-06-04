package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.model.UserValidation.userValidation;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Map<Integer, User> getUsers() {
        return userStorage.getUsers();
    }

    public IdGenerator getIdGenerator() {
        return userStorage.getIdGenerator();
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User getUserById(Integer userId) {
        if (!getUsers().containsKey(userId)) {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return userStorage.getUserById(userId);
    }

    public User addUser(User user) {
        userValidation(user);
        return userStorage.addUser(user);
    }

    public User changeUserById(Integer userId, User user) {
        if (!getUsers().containsKey(userId)) {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        userValidation(user);
        user.setId(userId);
        return userStorage.changeUserById(userId, user);
    }

    public User changeUser(User user) {
        if (!getUsers().containsKey(user.getId())) {
            log.info("Пользователь с идентификатором {} не найден.", user.getId());
            throw new UserNotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        userValidation(user);
        user.setId(user.getId());
        return userStorage.changeUser(user);
    }

    public User deleteUserById(Integer userId) {
        if (!getUsers().containsKey(userId)) {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return userStorage.deleteUserById(userId);
    }

    public User addFriend(Integer userId, Integer friendId) throws UserNotFoundException {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Пользователь с id " + friendId + " не найден");
        }
        return userStorage.addFriend(userId, friendId);
    }

    public User deleteFriend(Integer userId, Integer friendId) throws UserNotFoundException {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Пользователь с id " + friendId + " не найден");
        }
        return userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriendsById(Integer userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return userStorage.getFriendsById(userId);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        if (!userStorage.getUsers().containsKey(otherId)) {
            throw new UserNotFoundException("Пользователь с id " + otherId + " не найден");
        }
        return userStorage.getCommonFriends(id, otherId);
    }

}
