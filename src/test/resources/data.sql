
merge INTO filmorate_mpa (mpa_id, mpa_name)
    VALUES ('1', 'G'),
           ('2', 'PG'),
           ('3', 'PG_13'),
           ('4', 'R'),
           ('5', 'NC_17');

insert INTO FILMORATE_USER (EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES ('test@test.ru', 'userLogin1', 'displayName', '1950-12-12'),
       ('test@test.ru', 'userLogin2', 'displayName', '1950-12-12'),
       ('test@test.ru', 'userLogin3', 'displayName', '1950-12-12');

insert INTO FILMORATE_FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
VALUES ('Фильм1', 'Описание', '2000-12-12', '120', 1),
       ('Фильм2', 'Описание', '2000-12-12', '120', 1),
       ('Фильм3', 'Описание', '2000-12-12', '120', 1);

