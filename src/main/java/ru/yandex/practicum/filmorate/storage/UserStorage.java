package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsers();

    IdGenerator getIdGenerator();

    List<User> findAll();

    User getUserById(Integer userId);

    User addUser(User user);

    User changeUserById(Integer userId, User user);

    User changeUser(User user);

    User deleteUserById(Integer userId);

    User addFriend(Integer userId, Integer friendId);

    User deleteFriend(Integer userId, Integer friendId);

    List<User> getFriendsById(Integer userId);

    List<User> getCommonFriends(Integer id, Integer otherId);

}
