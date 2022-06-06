-- INSERT INTO film (name, description, release_date, duration, mpa_name)
-- VALUES ( 'Кое-кто', '80932334444', '2022-01-01','10','1');

merge INTO filmorate_mpa (mpa_id, mpa_name)
VALUES ('1', 'G'),
       ('2', 'PG'),
       ('3', 'PG_13'),
       ('4', 'R'),
       ('5', 'NC_17');