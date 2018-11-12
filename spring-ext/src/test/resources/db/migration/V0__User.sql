CREATE TABLE user
(
  id              SERIAL NOT NULL,
  email           VARCHAR(500)     NOT NULL,
  first_name      VARCHAR(255)     NOT NULL,
  last_name       VARCHAR(255)     NOT NULL,


  CONSTRAINT user_pkey PRIMARY KEY (id),
  CONSTRAINT uk_email UNIQUE (email)
);