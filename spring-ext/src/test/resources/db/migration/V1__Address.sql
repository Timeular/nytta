CREATE TABLE address
(
  id                SERIAL        NOT NULL,
  activity_id       BIGINT        NOT NULL,
  activity_name     VARCHAR(255)  NOT NULL,
  user_id           BIGINT        NOT NULL,
  monday            BIGINT        NOT NULL,
  tuesday           BIGINT        NOT NULL,
  wednesday         BIGINT        NOT NULL,
  thursday          BIGINT        NOT NULL,
  friday            BIGINT        NOT NULL,
  saturday          BIGINT        NOT NULL,
  sunday            BIGINT        NOT NULL,

  CONSTRAINT address_pkey PRIMARY KEY (id),
  CONSTRAINT address_user_fk FOREIGN KEY (user_id)
    REFERENCES user (id)
);
