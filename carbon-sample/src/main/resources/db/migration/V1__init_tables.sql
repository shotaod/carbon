CREATE TABLE asset
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    asset_path VARCHAR(255) NOT NULL,
    asset_type VARCHAR(3) NOT NULL
);
CREATE TABLE user
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX user_username_uindex ON user (username);
CREATE TABLE student
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX student_email_uindex ON student (email);
CREATE UNIQUE INDEX student_username_uindex ON student (username);
CREATE TABLE role
(
    id BIGINT(20) PRIMARY KEY NOT NULL
);
CREATE TABLE product
(
    id BIGINT(20) PRIMARY KEY NOT NULL
);
CREATE TABLE lecturer
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    thumbnail_id BIGINT(20),
    pr_text TEXT,
    password VARCHAR(255) NOT NULL,
    pr_text_short VARCHAR(255),
    CONSTRAINT lecturer_asset_id_fk FOREIGN KEY (thumbnail_id) REFERENCES asset (id)
);
CREATE INDEX lecturer_asset_id_fk ON lecturer (thumbnail_id);
CREATE UNIQUE INDEX lecturer_email_uindex ON lecturer (email);
CREATE TABLE lecturer_apply_history
(
    id BIGINT(20) PRIMARY KEY NOT NULL
);
CREATE TABLE lecturer_schedule
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    begin_datetime DATETIME NOT NULL,
    end_datetime DATETIME NOT NULL,
    lecturer_id BIGINT(20) NOT NULL,
    CONSTRAINT lecturer_schedule_lecturer_id_fk FOREIGN KEY (lecturer_id) REFERENCES lecturer (id)
);
CREATE INDEX lecturer_schedule_lecturer_id_fk ON lecturer_schedule (lecturer_id);
CREATE TABLE lecturer_room
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    lecturer_id BIGINT(20) NOT NULL,
    room_name VARCHAR(127) NOT NULL,
    room_detail TEXT NOT NULL,
    begin_datetime DATETIME NOT NULL,
    end_datetime DATETIME NOT NULL,
    CONSTRAINT lecturer_room_lecturer_id_fk FOREIGN KEY (lecturer_id) REFERENCES lecturer (id)
);
CREATE INDEX lecturer_room_lecturer_id_fk ON lecturer_room (lecturer_id);