package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map<Integer, User> map = new HashMap<>();
        for (User user : findAll()) {
            map.put(user.getId(), user);
        }
        return map;
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from filmorate_user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User getById(Integer userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from filmorate_user where USER_ID = ?", userId);
        if (userRows.next()) {
            return makeUser(userRows);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    @Override
    public User add(User user) {
        user.setId(getNextUserId());
        String sql = "INSERT INTO filmorate_user (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return user;
    }

    @Override
    public User changeById(Integer userId, User user) {
        String sql = "UPDATE filmorate_user SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = '" + userId + "'";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return user;
    }

    @Override
    public User change(User user) {
        String sql = "UPDATE filmorate_user SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = '" + user.getId() + "'";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return user;
    }

    @Override
    public User deleteById(Integer userId) {
        String sql = "DELETE FROM filmorate_user WHERE USER_ID = ?";
        User user = getById(userId);
        jdbcTemplate.update(sql, userId);
        return user;
    }


    public User addFriendRequest(Integer userId, Integer friendId) {
        String sql = "INSERT INTO filmorate_friendship (USER_ID, FRIEND_ID) VALUES (?,?)";
        jdbcTemplate.update(sql,
                userId,
                friendId);
        return getById(userId);
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        String sql = "INSERT INTO filmorate_friendship (USER_ID, FRIEND_ID) VALUES (?,?)";
        jdbcTemplate.update(sql,
                userId,
                friendId);
        //Убрано добавление в связи с тестами postman
//        jdbcTemplate.update(sql,
//                friendId,
//                userId);
        return getById(userId);
    }

    public User deleteFriendRequest(Integer userId, Integer friendId) {
        String sql = "DELETE FROM filmorate_friendship WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql,
                userId,
                friendId);
        jdbcTemplate.update(sql,
                friendId,
                userId);
        return getById(userId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM filmorate_friendship WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql,
                userId,
                friendId);
        return getById(userId);
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


    private User makeUser(ResultSet rs) throws SQLException {
        Integer userId = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = new User(email, login, name, birthday);
        user.setId(userId);
        user.getFriends().addAll(makeFriends(userId));
        return user;
    }

    private User makeUser(SqlRowSet srs) {
        Integer userId = srs.getInt("user_id");
        String email = srs.getString("email");
        String login = srs.getString("login");
        String name = srs.getString("name");
        LocalDate birthday = srs.getDate("birthday").toLocalDate();
        User user = new User(email, login, name, birthday);
        user.setId(userId);
        user.getFriends().addAll(makeFriends(userId));
        return user;
    }

    private Integer getNextUserId() {
        Integer max = jdbcTemplate.queryForObject("select MAX(user_id) MAX from filmorate_user", new Object[]{}, Integer.class);
        if (max == null) {
            return 1;
        } else return ++max;
    }

    private List<Integer> makeFriendsRequest(Integer userId) {
        String sql = "select f.USER_ID, f.FRIEND_ID " +
                     "from FILMORATE_FRIENDSHIP as f " +
                     "join FILMORATE_FRIENDSHIP as g on (f.USER_ID = g.FRIEND_ID and f.FRIEND_ID = g.USER_ID)" +
                     "where f.USER_ID = " + userId;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id"));
    }

    private List<Integer> makeFriends(Integer userId) {
        String sql = "select USER_ID, FRIEND_ID from FILMORATE_FRIENDSHIP where USER_ID = " + userId;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id"));
    }

    public void dropData(String tableName) {
        String sql = "delete from " + tableName;
        jdbcTemplate.update(sql);

    }
}
