CREATE TABLE auth_client
(
  id          BIGINT AUTO_INCREMENT,
  client_host VARCHAR(255) NOT NULL,
  client_id   CHAR(127)    NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT auth_client_client_host_uindex
  UNIQUE (client_host),
  CONSTRAINT auth_client_client_id_uindex
  UNIQUE (client_id)
)
  ENGINE = InnoDB;

CREATE TABLE user
(
  id        BIGINT AUTO_INCREMENT,
  email     VARCHAR(255) NOT NULL,
  user_name VARCHAR(31)  NOT NULL,
  secret  CHAR(65)     NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT user_email_uindex
  UNIQUE (email)
)
  ENGINE = InnoDB;
