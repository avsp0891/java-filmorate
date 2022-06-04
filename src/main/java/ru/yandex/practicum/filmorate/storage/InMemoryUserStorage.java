package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;



@Component
@Getter
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    private final Set<Friendship> friends = new HashSet<>();


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
        return new ArrayList<>(getUsers().values());
    }

    @Override
    public User getUserById(Integer userId) throws UserNotFoundException {
        return getUsers().get(userId);
    }

    @Override
    public User addUser(User user) {
        user.setId(getIdGenerator().getNextIdCount());
        getUsers().put(user.getId(), user);
        return user;
    }

    @Override
    public User changeUserById(Integer id, User user) {
        user.setId(id);
        getUsers().put(user.getId(), user);
        return user;
    }

    @Override
    public User changeUser(User user) {
        getUsers().put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUserById(Integer userId) {
        for (Integer idFriend : getUsers().get(userId).getFriends()) {
            getUsers().get(idFriend).getFriends().remove(userId);
        }
        return getUsers().remove(userId);
    }


    public User addFriendRequest(Integer userId, Integer friendId) {
        Friendship user = new Friendship(userId, friendId);
        Friendship friend = new Friendship(friendId, userId);
        getFriends().add(user);
        if (getFriends().contains(friend)) {
            getUsers().get(userId).getFriends().add(friendId);
            getUsers().get(friendId).getFriends().add(userId);
        }
        return getUsers().get(userId);
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        getUsers().get(userId).getFriends().add(friendId);
        getUsers().get(friendId).getFriends().add(userId);
        return getUsers().get(userId);
    }

    public User deleteRequest(Integer userId, Integer friendId) {
        Friendship user = new Friendship(userId, friendId);
        Friendship friend = new Friendship(friendId, userId);
        getFriends().remove(user);
        if (getFriends().contains(friend)) {
            getUsers().get(userId).getFriends().remove(friendId);
            getUsers().get(friendId).getFriends().remove(userId);
        }
        return getUsers().get(userId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        getUsers().get(userId).getFriends().remove(friendId);
        getUsers().get(friendId).getFriends().remove(userId);
        return getUsers().get(userId);
    }

    @Override
    public List<User> getFriendsById(Integer userId) {
        ArrayList<User> list = new ArrayList<>();
        for (Integer i : getUsers().get(userId).getFriends()) {
            list.add(getUsers().get(i));
        }
        return list;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        ArrayList<User> list = new ArrayList<>();
        for (User user : getFriendsById(id)) {
            if (getFriendsById(otherId).contains(user)) {
                list.add(user);
            }
        }
        return list;
    }
}
