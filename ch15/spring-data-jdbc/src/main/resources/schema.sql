DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    ID                INTEGER AUTO_INCREMENT PRIMARY KEY,
    USERNAME          VARCHAR(30),
    REGISTRATION_DATE DATE
);