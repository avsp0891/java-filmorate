package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Integer id;
    private String email;
    private String userLogin;
    private String displayName;
    private LocalDate dateOfBirth;
}
