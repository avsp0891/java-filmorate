package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Friendship {


    private final Integer userId;
    private final Integer friendId;

    public Friendship(Integer userId, Integer friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(userId, that.userId) && Objects.equals(friendId, that.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }
}
