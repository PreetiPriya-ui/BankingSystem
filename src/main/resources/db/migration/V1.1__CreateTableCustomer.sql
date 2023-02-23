CREATE TABLE CUSTOMER
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(255) NOT NULL,
    EMAIL VARCHAR(255) NOT NULL UNIQUE,
    MOBILE_NUMBER VARCHAR(10) NOT NULL,
    IDENTITY_CARD VARCHAR NOT NULL UNIQUE,
    ADDRESS VARCHAR(255) NOT NULL,
    PASSWORD VARCHAR NOT NULL,
    PRIMARY KEY (ID)
);