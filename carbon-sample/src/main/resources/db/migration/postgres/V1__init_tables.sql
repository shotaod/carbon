CREATE TABLE asset
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    asset_path VARCHAR(255) NOT NULL,
    asset_type VARCHAR(3) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    identity VARCHAR(255) NOT NULL,
    secret VARCHAR(255) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE UNIQUE INDEX user_username_uindex ON user (identity);

CREATE TABLE student
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    secret VARCHAR(255) NOT NULL,
    identity VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE UNIQUE INDEX student_email_uindex ON student (email);
CREATE UNIQUE INDEX student_username_uindex ON student (identity);

CREATE TABLE role
(
    id BIGINT(20) PRIMARY KEY NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE product
(
    id BIGINT(20) PRIMARY KEY NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE lecturer
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    identity VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    thumbnail_id BIGINT(20),
    pr_text TEXT,
    secret VARCHAR(255) NOT NULL,
    pr_text_short VARCHAR(255),
    CONSTRAINT lecturer_asset_id_fk FOREIGN KEY (thumbnail_id) REFERENCES asset (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX lecturer_asset_id_fk ON lecturer (thumbnail_id);
CREATE UNIQUE INDEX lecturer_email_uindex ON lecturer (email);

CREATE TABLE lecturer_apply_history
(
    id BIGINT(20) PRIMARY KEY NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE lecturer_schedule
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    begin_datetime DATETIME NOT NULL,
    end_datetime DATETIME NOT NULL,
    lecturer_id BIGINT(20) NOT NULL,
    CONSTRAINT lecturer_schedule_lecturer_id_fk FOREIGN KEY (lecturer_id) REFERENCES lecturer (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX lecturer_room_lecturer_id_fk ON lecturer_room (lecturer_id);