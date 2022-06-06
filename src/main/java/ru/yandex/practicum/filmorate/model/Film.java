package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Integer id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer likeCount;
    private Set<Integer> usersWhoLikedTheMovie;
    private Set<Integer> genres;
    private Mpa mpa;

    @JsonCreator
    public Film(String name, String description, LocalDate releaseDate, Integer duration, @JsonProperty("mpa") Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likeCount = 0;
        this.usersWhoLikedTheMovie = new HashSet<>();
        this.genres = new HashSet<>();
        this.mpa = mpa;
    }
}
