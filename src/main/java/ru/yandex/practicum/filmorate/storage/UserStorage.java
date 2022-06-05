package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsers();

    List<User> findAll();

    User getById(Integer userId);

    User add(User user);

    User changeById(Integer userId, User user);

    User change(User user);

    User deleteById(Integer userId);

    User addFriend(Integer userId, Integer friendId);

    User deleteFriend(Integer userId, Integer friendId);

    List<User> getFriendsById(Integer userId);

    List<User> getCommonFriends(Integer id, Integer otherId);

}
