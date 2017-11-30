CREATE SEQUENCE invitation_transaction_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 3;

CREATE TABLE invitation_transaction
(

  id integer NOT NULL DEFAULT nextval('invitation_transaction_id_seq'),
  from_id varchar(255) NOT NULL,
  to_id varchar(255) NOT NULL,
  amount integer NOT NULL,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  CONSTRAINT invitation_transaction_pk PRIMARY KEY (id)
);

CREATE TABLE invitation_code
(
  code varchar(255) NOT NULL,
  owner_id varchar(255) NOT NULL,
  owner_name varchar(255) NOT NULL,
  owner_email varchar(255) NOT NULL,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  CONSTRAINT invitation_code_pk PRIMARY KEY (code)
);
