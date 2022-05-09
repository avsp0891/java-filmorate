package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer userId, Integer friendId) throws UserNotFoundException {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Пользователь с id " + friendId + " не найден");
        }
        userStorage.getUsers().get(userId).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) throws UserNotFoundException {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Пользователь с id " + friendId + " не найден");
        }
        userStorage.getUsers().get(userId).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(userId);
    }

    public List<User> getFriendsById(Integer userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        ArrayList<User> list = new ArrayList<>();
        for (Integer i : userStorage.getUsers().get(userId).getFriends()) {
            list.add(userStorage.getUsers().get(i));
        }
        return list;
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        if (!userStorage.getUsers().containsKey(otherId)) {
            throw new UserNotFoundException("Пользователь с id " + otherId + " не найден");
        }
        ArrayList<User> list = new ArrayList<>();
        for (User user : getFriendsById(id)) {
            if (getFriendsById(otherId).contains(user)) {
                list.add(user);
            }
        }
        return list;
    }

}
