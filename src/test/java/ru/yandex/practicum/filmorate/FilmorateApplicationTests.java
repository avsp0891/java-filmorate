package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    User user1 = new User("test@test.ru", "userLogin1", "displayName", LocalDate.of(1950, 12, 12));
    User user2 = new User("test@test.ru", "userLogin2", "displayName", LocalDate.of(1950, 12, 12));
    User user3 = new User("test@test.ru", "userLogin3", "displayName", LocalDate.of(1950, 12, 12));
    User user4 = new User("test@test.ru", "userLogin4", "displayName", LocalDate.of(1950, 12, 12));

    Film film1 = new Film("Фильм1", "Описание", LocalDate.of(2000, 12, 12), 120, new Mpa(1));
    Film film2 = new Film("Фильм2", "Описание", LocalDate.of(2000, 12, 12), 120, new Mpa(1));
    Film film3 = new Film("Фильм3", "Описание", LocalDate.of(2000, 12, 12), 120, new Mpa(1));
    Film film4 = new Film("Фильм4", "Описание", LocalDate.of(2000, 12, 12), 120, new Mpa(1));

    private final UserDbStorage userStorage;

    private final FilmDbStorage filmStorage;


    @Test
    void findAllUsers() {
        List<User> list = new ArrayList<>();
        user1.setId(1);
        user2.setId(2);
        user3.setId(3);
        list.add(user1);
        list.add(user2);
        list.add(user3);
        assertEquals(list, userStorage.findAll());
    }

    @Test
    void getUserById() {
        user1.setId(1);
        assertEquals(user1, userStorage.getUserById(1));
    }

    @Test
    void addUser() {
        user4.setId(4);
        userStorage.addUser(user4);
        assertEquals(user4, userStorage.getUsers().get(4));
    }
    @Test
    void deleteUserByIdUser() {
        userStorage.deleteUserById(2);
        assertNull(userStorage.getUsers().get(2));
    }


    @Test
    void changeUser() {
        User userChange = new User("test@test.ru", "userLoginChange", "displayName", LocalDate.of(1950, 12, 12));
        userChange.setId(2);
        userStorage.changeUser(userChange);
        assertEquals(userChange, userStorage.getUsers().get(2));
    }


    @Test
    void addFriend() {
        userStorage.addFriend(1,2);
        assertEquals(Set.of(2), userStorage.getUsers().get(1).getFriends());
//        assertEquals(Set.of(1), userStorage.getUsers().get(2).getFriends());
    }

    @Test
    void deleteFriend() {
        userStorage.addFriend(1,2);
        userStorage.deleteFriend(1,2);
        assertEquals(Set.of(),userStorage.getUsers().get(1).getFriends());
    }


    @Test
    void getFriendsById() {
        userStorage.addFriend(1,2);
        user2.setId(2);
//        user2.getFriends().add(1);
        assertEquals(List.of(user2), userStorage.getFriendsById(1));
    }

    @Test
    void getCommonFriends() {
        user1.setId(1);
//        user1.getFriends().add(2);
//        user1.getFriends().add(3);
        userStorage.addFriend(2,1);
        userStorage.addFriend(3,1);
        assertEquals(List.of(user1), userStorage.getCommonFriends(2,3));
    }


    @Test
    void findAllFilms() {
        film1.setId(1);
        film2.setId(2);
        film3.setId(3);
        ArrayList<Film> arrayList = new ArrayList<>();
        arrayList.add(film1);
        arrayList.add(film2);
        arrayList.add(film3);
        assertEquals(arrayList, filmStorage.findAll());
    }


    @Test
    void getFilmById() {
        film1.setId(1);
        assertEquals(film1, filmStorage.getFilmById(1));
    }

    @Test
    void deleteFilmById() {
        filmStorage.deleteFilmById(1);
        assertNull(filmStorage.getFilms().get(1));
    }


    @Test
    void addFilmStandard() {
        film4.setId(4);
        filmStorage.addFilm(film4);
        assertEquals(film4, filmStorage.getFilms().get(4));
    }


    @Test
    void changeFilm() {
        Film filmChange = new Film("Фильм изменен", "Описание изменен", LocalDate.of(2001, 12, 12), 180, new Mpa(2));
        filmChange.setId(1);
        filmStorage.changeFilm(filmChange);
        assertEquals(filmChange, filmStorage.getFilms().get(1));
    }

    @Test
    void addLike() {
        filmStorage.addLike(1,1);
        assertEquals(1, filmStorage.getFilms().get(1).getLikeCount());
        assertEquals(Set.of(1), filmStorage.getFilms().get(1).getUsersWhoLikedTheMovie());
    }

    @Test
    void deleteLike() {
        filmStorage.addLike(1,1);
        filmStorage.deleteLike(1,1);
        assertEquals(0, filmStorage.getFilms().get(1).getLikeCount());
        assertEquals(Set.of(), filmStorage.getFilms().get(1).getUsersWhoLikedTheMovie());
    }

    @Test
    void getPopularFilms() {
        film1.setId(1);
        film1.setLikeCount(2);
        film1.getUsersWhoLikedTheMovie().add(1);
        film1.getUsersWhoLikedTheMovie().add(2);
        film2.setId(2);
        film2.setLikeCount(1);
        film2.getUsersWhoLikedTheMovie().add(3);
        film3.setId(3);
        film3.setLikeCount(0);
        filmStorage.addLike(1,1);
        filmStorage.addLike(1,2);
        filmStorage.addLike(2,3);
        assertEquals(List.of(film1,film2,film3), filmStorage.getPopularFilms(3));
    }

}




