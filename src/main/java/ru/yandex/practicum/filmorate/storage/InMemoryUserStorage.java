package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

import static ru.yandex.practicum.filmorate.model.UserValidation.userValidation;


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
        else throw new UserNotFoundException("Пользователь с id " + user.getId() + " не найден");
    }

    @Override
    public User deleteUserById(Integer userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        for (Integer idFriend: users.get(userId).getFriends()) {
            users.get(idFriend).getFriends().remove(userId);
        }
        return users.remove(userId);
    }
}
