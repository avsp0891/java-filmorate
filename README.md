# java-filmorate
Template repository for Filmorate project.

## Схема БД
![Схема](files\Схема.png)

##Запросы для основных операций на примере работы с фильмами:

###Получение списка всех фильмов

SELECT   
f.film_id,  
f.name,  
f.description,  
f.release_date,  
f.duration,  
r.rating_name  
FROM film AS f  
INNER JOIN rating AS r ON f.rating_name = r.rating_name;

###Получение списка всех фильмов и соответствующих жанров

SELECT   
f.film_id,  
f.name,  
g.name  
FROM film AS f  
INNER JOIN film_genre AS fg ON f.film_id = fg.film_id  
INNER JOIN genre AS g ON fg.genre_id = g.genre_id;

###Получение количества лайков фильма 'Назад в будущее'

SELECT   
COUNT(film_id)
FROM like AS l  
INNER JOIN film AS f ON l.film_id = f.film_id   
WHERE f.name = 'Назад в будущее'  
GROUP BY film_id;
