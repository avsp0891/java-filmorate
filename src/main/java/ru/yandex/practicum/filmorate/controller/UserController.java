package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Getter
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.debug("Получение списка всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Integer id) {
        log.debug("Получение пользователя : \n{}", id);
        return userService.getUserById(id);
    }


    @GetMapping("/{id}/friends")
    public List<User> getFriendsById(@PathVariable(value = "id") Integer id) {
        log.debug("Получение списка всех друзей пользователя : {}", id);
        return userService.getFriendsById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable(value = "id") Integer id, @PathVariable(value = "otherId") Integer otherId) {
        log.debug("Получение списка общих друзей пользователей {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.debug("Добавление нового пользователя : \n{}", user);
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    public User changeUser(@PathVariable(value = "id") Integer id, @Valid @RequestBody User user) {
        log.debug("Изменение нового пользователя : \n{}", user);
        return userService.changeUserById(id, user);
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        log.debug("Изменение нового пользователя : \n{}", user);
        return userService.changeUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "friendId") Integer friendId) {
        log.debug("Добавление в друзья пользователя {} к пользователю {}", friendId, userId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable(value = "id") Integer id, @PathVariable(value = "friendId") Integer friendId) {
        log.debug("Удаление из друзей пользователя {} у пользователя {}", friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    public User deleteUserById(@PathVariable(value = "id") Integer id) {
        log.debug("Удаление пользователя {}", id);
        return userService.deleteUserById(id);
    }




}
