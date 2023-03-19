--liquibase formatted sql

--changeset dfetisov:1
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='image'
--onFail=MARK_RAN
CREATE TABLE image
(
    id         INTEGER PRIMARY KEY generated always as identity,
    path_image TEXT NOT NULL
);

--changeset dfetisov:2
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='avatar'
--onFail=MARK_RAN
CREATE TABLE avatar
(
    id          INTEGER PRIMARY KEY generated always as identity,
    path_avatar TEXT
);

--changeset dfetisov:3
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='users'
--onFail=MARK_RAN
CREATE TABLE users
(
    id         INTEGER PRIMARY KEY generated always as identity,
    first_name TEXT      NOT NULL,
    last_name  TEXT      NOT NULL,
    email      TEXT      ,
    phone      TEXT      ,
    city       TEXT      ,
    reg_date   TIMESTAMP NOT NULL,
    user_name  TEXT      NOT NULL,
    password   TEXT      NOT NULL,
    id_avatar  INTEGER   REFERENCES avatar (id),
    role       TEXT      NOT NULL
);

--changeset dfetisov:4
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='ads'
--onFail=MARK_RAN
CREATE TABLE ads
(
    id          INTEGER PRIMARY KEY generated always as identity,
    id_user     INTEGER NOT NULL REFERENCES users (id),
    id_image    INTEGER NOT NULL REFERENCES image (id),
    price       INTEGER NOT NULL,
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL
);

--changeset dfetisov:5
--precondition-sql-check expectedResult:0 SELECT count(*) FROM pg_tables WHERE tablename='comment'
--onFail=MARK_RAN
CREATE TABLE comment
(
    id         INTEGER PRIMARY KEY generated always as identity,
    id_user    INTEGER   NOT NULL REFERENCES users (id),
    id_ads     INTEGER   NOT NULL REFERENCES ads (id),
    text       TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL
);
--changeset dfetisov:6
INSERT INTO avatar (path_avatar) VALUES('zero');




