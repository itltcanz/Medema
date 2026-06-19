--liquibase formatted sql

--changeset itltcanz:1
CREATE TABLE Location
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL
);

--changeset itltcanz:2
CREATE TABLE Detector
(
    id          VARCHAR(255) NOT NULL,
    ip          VARCHAR(45)  NOT NULL,
    port        VARCHAR(10)  NOT NULL,
    location_id INTEGER,
    CONSTRAINT pk_detector PRIMARY KEY (id),
    CONSTRAINT fk_detector_location FOREIGN KEY (location_id)
        REFERENCES Location (id) ON DELETE SET NULL
);

--changeset itltcanz:3
CREATE TABLE Scan
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    metal       TINYINT   NOT NULL,
    time        TIMESTAMP NOT NULL,
    detector_id VARCHAR(255),
    CONSTRAINT fk_scan_detector FOREIGN KEY (detector_id)
        REFERENCES Detector (id) ON DELETE CASCADE
);