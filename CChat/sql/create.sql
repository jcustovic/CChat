create table service_provider (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  sc VARCHAR(20) NULL,
  provider_name VARCHAR(30) NULL,
  description VARCHAR(200) NULL,
  disabled BOOL NULL,
  PRIMARY KEY(id),
  UNIQUE(sc, provider_name)
);

CREATE TABLE operator_roles (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  description VARCHAR(100) NULL,
  PRIMARY KEY(id),
  UNIQUE(name)
);

CREATE TABLE nicks (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  description VARCHAR(300) NULL,
  PRIMARY KEY(id),
  UNIQUE(name)
);

CREATE TABLE configuration (
  name VARCHAR(50) NOT NULL,
  value VARCHAR(100) NOT NULL,
  PRIMARY KEY(name)
);

CREATE TABLE pictures (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  nick_id INTEGER UNSIGNED,
  name VARCHAR(50) NOT NULL,
  file_type VARCHAR(15) NOT NULL,
  file_size BIGINT NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(nick_id) REFERENCES nicks(id)
      ON DELETE SET NULL
      ON UPDATE NO ACTION
);

CREATE TABLE operators (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  operator_role_id INTEGER UNSIGNED NOT NULL,
  username VARCHAR(30) NOT NULL,
  pass VARCHAR(50) NOT NULL,
  name VARCHAR(20) NULL,
  surname VARCHAR(30) NULL,
  email VARCHAR(50) NULL,
  is_active BOOL NOT NULL DEFAULT false,
  disabled BOOL NOT NULL DEFAULT false,
  PRIMARY KEY(id),
  FOREIGN KEY(operator_role_id) REFERENCES operator_roles(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE users (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  service_provider_id INTEGER UNSIGNED NOT NULL,
  operator_id INTEGER UNSIGNED NULL,
  nick_id INTEGER UNSIGNED NULL,
  msisdn VARCHAR(20) NOT NULL,
  name VARCHAR(20) NULL,
  surname VARCHAR(30) NULL,
  address VARCHAR(100) NULL,
  birth_date DATE NULL,
  notes TEXT NULL,
  joined_date DATETIME NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(operator_id)
    REFERENCES operators(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(nick_id)
    REFERENCES nicks(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(service_provider_id)
    REFERENCES service_provider(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE sms_messages (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  service_provider_id INTEGER UNSIGNED NOT NULL,
  operator_id INTEGER UNSIGNED NOT NULL,
  user_id INTEGER UNSIGNED NOT NULL,
  time DATETIME NOT NULL,
  text VARCHAR(800) NOT NULL,
  sc VARCHAR(20) NOT NULL,
  direction VARCHAR(6) NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(user_id) REFERENCES users(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(operator_id) REFERENCES operators(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(service_provider_id) REFERENCES service_provider(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE users_pictures (
  picture_id INTEGER UNSIGNED NOT NULL,
  user_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(picture_id, user_id),
  FOREIGN KEY(picture_id) REFERENCES pictures(id)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(user_id) REFERENCES users(id)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);