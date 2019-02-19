CREATE TABLE mail_storage(
  id            SERIAL        NOT NULL,
  sender        VARCHAR(255)  NOT NULL,
  subject       VARCHAR(255)  NOT NULL,
  receiver      VARCHAR(255),
  cc            VARCHAR(255),
  bcc           VARCHAR(255),
  rawText       TEXT,
  html          TEXT,
  deliveryTime  TIMESTAMP,
  tag           VARCHAR(200),
  creationTime  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT mail_storage_pkey PRIMARY KEY (id)
);


CREATE TABLE mail_storage_attachment(
  id            SERIAL        NOT NULL,
  mail_Id       BIGINT        NOT NULL,
  name          VARCHAR(255)  NOT NULL,
  mime_type     VARCHAR(255)  NOT NULL,
  data          BYTEA         NOT NULL,

  CONSTRAINT mail_storage_attachment_pkey PRIMARY KEY (id),
  CONSTRAINT mail_storage_attachment_mail_storage_fk FOREIGN KEY (mail_id)
  REFERENCES mail_storage (id)
);