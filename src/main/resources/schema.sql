create table IF NOT EXISTS USERS
(
    USER_ID       INTEGER auto_increment,
    USER_EMAIL    CHARACTER VARYING(200) not null,
    USER_LOGIN    CHARACTER VARYING(200),
    USER_NAME     CHARACTER VARYING(200),
    USER_BIRTHDAY DATE,
    constraint "uses_PK"
        primary key (USER_ID)
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
    FILM_ID           INTEGER auto_increment,
    FILM_NAME         CHARACTER VARYING(200) not null,
    FILM_DESCRIPTION  CHARACTER VARYING(200),
    FILM_RELEASE_DATE DATE,
    FILM_DURATION     INTEGER,
    RATING_ID        INTEGER,
    constraint "films_PK"
        primary key (FILM_ID),
    constraint FILMS_MPA_FK
        foreign key (RATING_ID) references MPA
            on delete cascade
);



create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME INTEGER not null,
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

create table IF NOT EXISTS FRIENDSHIP
(
    FRIENDSHIP_ID INTEGER auto_increment,
    USER_ID       INTEGER not null,
    FRIEND_ID     INTEGER not null,
    constraint FRIENDSHIP_USERS_FRIEND_FK
        foreign key (FRIEND_ID) references USERS,
    constraint USERS_FRIENDSHIP_USER_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade
);




