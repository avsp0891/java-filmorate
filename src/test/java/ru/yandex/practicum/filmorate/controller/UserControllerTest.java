package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private User user;
    private UserController userController;


    @BeforeEach
    private void setUp() {
        userController = new UserController();
        user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("userLogin");
        user.setName("displayName");
        user.setBirthday(LocalDate.of(1950, 12, 12));
    }


    @Test
    void findAllStandard() {
        userController.addUser(user);
        ArrayList<User> arrayList = new ArrayList<>();
        arrayList.add(user);
        assertEquals(arrayList, userController.findAll());
    }

    @Test
    void findAllEmptyList() {
        assertEquals(0, userController.findAll().size());
    }

    @Test
    void addUserStandard() {
        userController.addUser(user);
        assertEquals(user, userController.getUserRepository().get(0));
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

//    @Test
//    void addUserValidation4() {
//        user.setLogin("логин с пробелами");
//        final ValidationException e = assertThrows(
//                ValidationException.class,
//                () -> userController.addUser(user)
//        );
//        assertEquals("Логин пользователя не должен содержать пробелы.", e.getMessage());
//    }

    @Test
    void addUserValidation5() {
        user.setName("");
        userController.addUser(user);
        assertEquals(user, userController.getUserRepository().get(0));
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
        User user2 = new User();
        user2.setEmail("test2@test.ru");
        user2.setLogin("userLogin2");
        user2.setName("displayName2");
        user2.setBirthday(LocalDate.of(1960, 12, 12));
        userController.changeUser(0, user2);
        assertEquals(user2, userController.getUserRepository().get(0));
    }

    @Test
    void changeUserNotFound() {
        userController.changeUser(0, user);
        assertNull(userController.getUserRepository().get(0));
    }


    @Test
    void getNextIdCount() {
        assertEquals(0, userController.idGenerator.getNextIdCount());
        assertEquals(1, userController.idGenerator.getNextIdCount());
    }
}