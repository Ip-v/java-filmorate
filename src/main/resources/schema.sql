DROP TABLE IF EXISTS film_genres, likes, friendship, films, film_genres, mpa, USERS, GENRES;

create table IF NOT EXISTS USERS
(
    USER_ID       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    USER_EMAIL    CHARACTER VARYING(200) not null,
    USER_LOGIN    CHARACTER VARYING(200),
    USER_NAME     CHARACTER VARYING(200),
    USER_BIRTHDAY DATE

);

create unique index if not exists USER_EMAIL_UNQ
    on USERS (USER_EMAIL);

create table IF NOT EXISTS MPA
(
    RATING_ID INTEGER auto_increment,
    MPA_NAME   CHARACTER VARYING(10) not null,
    constraint "mpa_PK"
        primary key (RATING_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    FILM_NAME         CHARACTER VARYING(200) not null,
    FILM_DESCRIPTION  CHARACTER VARYING(200),
    FILM_RELEASE_DATE DATE,
    FILM_DURATION     INTEGER,
    RATING_ID         INTEGER,
    constraint FILMS_MPA_FK
        foreign key (RATING_ID) references MPA
            on delete cascade
);



create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(200) not null,
    constraint GENRES_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS FILM_GENRES
(
    ID       INTEGER auto_increment,
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint "film_genres_films_FK"
        foreign key (FILM_ID) references FILMS,
    constraint "film_genres_genres_FK"
        foreign key (GENRE_ID) references GENRES
            on update cascade on delete cascade
);

create table IF NOT EXISTS LIKES
(
    LIKE_ID INTEGER auto_increment,
    USER_ID INTEGER not null,
    FILM_ID INTEGER not null,
    constraint LIKES_FILM_FK
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint LIKES_USERS_FK
        foreign key (USER_ID) references USERS
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP
(
    user_id   INTEGER,
    friend_id INTEGER,
    CONSTRAINT friendship_pk
        PRIMARY KEY (user_id, friend_id),
    CONSTRAINT users
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT friend
        FOREIGN KEY (friend_id) REFERENCES users (user_id)
);




