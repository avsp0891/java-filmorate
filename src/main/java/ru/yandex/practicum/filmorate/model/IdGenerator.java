package ru.yandex.practicum.filmorate.model;

public class IdGenerator {
    private Integer idCount;

    public IdGenerator() {
        this.idCount = 1;
    }

    public Integer getNextIdCount() {
        return idCount++;
    }
}
