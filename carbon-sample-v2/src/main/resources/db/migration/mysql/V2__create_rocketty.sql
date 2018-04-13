# ==================================================
# ==================================================
CREATE TABLE rocketty_auth_client
(
  id            BIGINT AUTO_INCREMENT,
  client_id     CHAR(255)              NOT NULL,
  client_secret CHAR(255)              NOT NULL,
  valid         TINYINT(1) DEFAULT '0' NOT NULL,
  expire_at     DATETIME               NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE UNIQUE INDEX rocketty_auth_client_client_id__uindex
  ON rocketty_auth_client (client_id);

# ==================================================
# ==================================================
CREATE TABLE rocketty_user
(
  id                      BIGINT AUTO_INCREMENT,
  rocketty_auth_client_id BIGINT       NOT NULL,
  display_name            VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE UNIQUE INDEX rocketty_user_display_name_uindex
  ON rocketty_user (display_name);

CREATE INDEX rocketty_user_rocketty_auth_client_id_fk
  ON rocketty_user (rocketty_auth_client_id);

# ==================================================
# ==================================================
CREATE TABLE rocketty_ranking
(
  id               BIGINT AUTO_INCREMENT,
  rocketty_user_id BIGINT NOT NULL,
  score            INT    NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB;

CREATE UNIQUE INDEX rocketty_ranking_rocketty_user_id_uk
  ON rocketty_ranking (rocketty_user_id);

CREATE INDEX rocketty_ranking_rocketty_user_id_fk
  ON rocketty_ranking (rocketty_user_id);

CREATE INDEX rocketty_ranking_score__index
  ON rocketty_ranking (score);

