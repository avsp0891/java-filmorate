package ru.yandex.practicum.filmorate.model;

public class IdGenerator {
    private Integer idCount;

    public IdGenerator() {
        this.idCount = 0;
    }

    public Integer getNextIdCount() {
        return idCount++;
    }
}
