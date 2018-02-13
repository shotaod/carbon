CREATE TABLE rocketty_ranking
(
  id               BIGINT AUTO_INCREMENT,
  internal_user_id CHAR(127)    NOT NULL,
  score            INT          NULL,
  display_name     VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT rocketty_ranking_internal_user_id__index
  UNIQUE (internal_user_id)
)
  ENGINE = InnoDB;

CREATE INDEX rocketty_ranking_score__index
  ON rocketty_ranking (score);

