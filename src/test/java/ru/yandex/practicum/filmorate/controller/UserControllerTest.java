package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private User user;
    private UserController userController;
    private UserStorage userStorage;
    private UserService userService;


    @BeforeEach
    private void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
        user = new User("test@test.ru", "userLogin", "displayName", LocalDate.of(1950, 12, 12));
    }

    @Test
    void findAllStandard() {
        userController.addUser(user);
        List<User> list = new ArrayList<>();
        list.add(user);
        assertEquals(list, userController.findAll());
    }

    @Test
    void findAllEmptyList() {
        assertEquals(0, userController.findAll().size());
    }

    @Test
    void getUserByIdStandard() {
        userController.addUser(user);
        assertEquals(user, userController.getUserById(1));
    }

    @Test
    void getUserByIdUserNotFound() {
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> userController.getUserById(1)
        );
        assertEquals("Пользователь с id 1 не найден", e.getMessage());
    }

    @Test
    void deleteUserByIdUserStandard() {
        userController.addUser(user);
        userController.deleteUserById(1);
        assertNull(userStorage.getUsers().get(1));
    }

    @Test
    void deleteUserByIdUserNotFound() {
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> userController.deleteUserById(1)
        );
        assertEquals("Пользователь с id 1 не найден", e.getMessage());
    }

    @Test
    void deleteUserByIdCheckDeleteFriend() {
        userController.addUser(user);
        User user2 = new User("2@test.ru", "userLogin2", "displayName2", LocalDate.of(1960, 12, 12));
        userController.addUser(user2);
        userController.addFriend(1,2);
        userController.deleteUserById(1);
        assertFalse(userStorage.getUsers().get(2).getFriends().contains(1));
    }

    @Test
    void addUserStandard() {
        userController.addUser(user);
        assertEquals(user, userStorage.getUsers().get(1));
    }

    @Test
    void addUserValidation1() {
        user.setEmail("");
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("Адрес электронной почты не может быть пустым.", e.getMessage());
    }

    @Test
    void addUserValidation2() {
        user.setEmail("отсутствует символ 'собака'");
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("Адрес электронной почты должен содержать символ '@'.", e.getMessage());
    }

    @Test
    void addUserValidation3() {
        user.setLogin("");
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("Логин пользователя не может быть пустым.", e.getMessage());
    }

    @Test
    void addUserValidation5() {
        user.setName("");
        userController.addUser(user);
        assertEquals(user, userStorage.getUsers().get(1));
    }

    @Test
    void addUserValidation6() {
        user.setBirthday(LocalDate.now().plusDays(1));
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("Неверная дата рождения.", e.getMessage());
    }

    @Test
    void changeUserStandard() {
        userController.addUser(user);
        User user2 = new User("2@test.ru", "userLogin2", "displayName2", LocalDate.of(1960, 12, 12));
        userController.changeUser(1, user2);
        assertEquals(user2, userStorage.getUsers().get(1));
    }

    @Test
    void changeUserNotFound() {
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () ->  userController.changeUser(0, user)
        );
        assertEquals("Пользователь с id 0 не найден", e.getMessage());
    }

    @Test
    void getNextIdCount() {
        assertEquals(1, userStorage.getIdGenerator().getNextIdCount());
        assertEquals(2, userStorage.getIdGenerator().getNextIdCount());
    }

    @Test
    void addFriendStandard() {
        User user2 = new User("2@test.ru", "userLogin2", "displayName2", LocalDate.of(1960, 12, 12));
        userController.addUser(user);
        userController.addUser(user2);
        userController.addFriend(1,2);
        assertEquals(Set.of(2), userStorage.getUsers().get(1).getFriends());
        assertEquals(Set.of(1), userStorage.getUsers().get(2).getFriends());
    }

    @Test
    void addFriendUserNotFound() {
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> userController.addFriend(1,2)
        );
        assertEquals("Пользователь с id 1 не найден", e.getMessage());
    }

    @Test
    void addFriendFriendNotFound() {
        userController.addUser(user);
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> userController.addFriend(1,2)
        );
        assertEquals("Пользователь с id 2 не найден", e.getMessage());
    }

    @Test
    void deleteFriendStandard() {
        User user2 = new User("2@test.ru", "userLogin2", "displayName2", LocalDate.of(1960, 12, 12));
        User user3 = new User("3@test.ru", "userLogin3", "displayName3", LocalDate.of(1961, 12, 12));
        userController.addUser(user);
        userController.addUser(user2);
        userController.addUser(user3);
        userController.addFriend(1,2);
        userController.addFriend(1,3);
        userController.deleteFriend(1,3);
        assertEquals(Set.of(2), userStorage.getUsers().get(1).getFriends());
    }

    @Test
    void deleteFriendUserNotFound() {
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> userController.deleteFriend(1,2)
        );
        assertEquals("Пользователь с id 1 не найден", e.getMessage());
    }

    @Test
    void deleteFriendFriendNotFound() {
        userController.addUser(user);
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> userController.deleteFriend(1,2)
        );
        assertEquals("Пользователь с id 2 не найден", e.getMessage());
    }

    @Test
    void getFriendsByIdStandard() {
        User user2 = new User("2@test.ru", "userLogin2", "displayName2", LocalDate.of(1960, 12, 12));
        User user3 = new User("3@test.ru", "userLogin3", "displayName3", LocalDate.of(1961, 12, 12));
        userController.addUser(user);
        userController.addUser(user2);
        userController.addUser(user3);
        userController.addFriend(1,2);
        userController.addFriend(1,3);
        assertEquals(List.of(user2,user3), userController.getFriendsById(1));
    }

    @Test
    void getFriendsByIdUserNotFound() {
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> userController.getFriendsById(1)
        );
        assertEquals("Пользователь с id 1 не найден", e.getMessage());
    }

    @Test
    void getFriendsByIdEmptyList() {
        userController.addUser(user);
        assertEquals(List.of(), userController.getFriendsById(1));
    }

    @Test
    void getCommonFriendsStandard() {
        User user2 = new User("2@test.ru", "userLogin2", "displayName2", LocalDate.of(1960, 12, 12));
        User user3 = new User("3@test.ru", "userLogin3", "displayName3", LocalDate.of(1961, 12, 12));
        userController.addUser(user);
        userController.addUser(user2);
        userController.addUser(user3);
        userController.addFriend(2,1);
        userController.addFriend(3,1);
        assertEquals(List.of(user), userController.getCommonFriends(2,3));
    }

    @Test
    void getCommonFriendsUserNotFound() {
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> userController.getCommonFriends(1,2)
        );
        assertEquals("Пользователь с id 1 не найден", e.getMessage());
    }

    @Test
    void getCommonFriendsOtherUserNotFound() {
        userController.addUser(user);
        final UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> userController.getCommonFriends(1,2)
        );
        assertEquals("Пользователь с id 2 не найден", e.getMessage());
    }

    @Test
    void getCommonFriendsEmptyList() {
        User user2 = new User("2@test.ru", "userLogin2", "displayName2", LocalDate.of(1960, 12, 12));
        userController.addUser(user);
        userController.addUser(user2);
        assertEquals(List.of(), userController.getCommonFriends(1,2));
    }


}