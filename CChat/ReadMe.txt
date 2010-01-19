package_hr:
"%JAVA_HOME%\bin\native2ascii.exe" -encoding UTF8 "D:\Java workspaces\Projekti\CChat\src\hr\chus\cchat\struts2\action\package_hr-WORK.properties" "D:\Java workspaces\Projekti\CChat\src\hr\chus\cchat\struts2\action\package_hr.properties"


MYSQL


--- CREATE DB ---

CREATE DATABASE cchat DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER 'cchat_user'@'localhost' IDENTIFIED BY 'a54Oa#22?3fsF';
GRANT ALL PRIVILEGES ON *.* TO 'cchat_user'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;

--- DB SHEMA CREATE ---

CREATE TABLE Service_Provider (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  sc VARCHAR(20) NULL,
  provider_name VARCHAR(30) NULL,
  description VARCHAR(200) NULL,
  disabled BOOL NULL,
  PRIMARY KEY(id),
  UNIQUE(sc, provider_name)
);

CREATE TABLE Operator_Roles (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  description VARCHAR(100) NULL,
  PRIMARY KEY(id),
  UNIQUE(name)
);

CREATE TABLE Nicks (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  description VARCHAR(300) NULL,
  PRIMARY KEY(id),
  UNIQUE(name)
);

CREATE TABLE Configuration (
  name VARCHAR(50) NOT NULL,
  value VARCHAR(100) NOT NULL,
  PRIMARY KEY(name)
);

CREATE TABLE Pictures (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  nick_id INTEGER UNSIGNED,
  name VARCHAR(50) NOT NULL,
  file_type VARCHAR(15) NOT NULL,
  file_size BIGINT NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(nick_id) REFERENCES Nicks(id)
      ON DELETE SET NULL
      ON UPDATE NO ACTION
);

CREATE TABLE Operators (
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
  FOREIGN KEY(operator_role_id) REFERENCES Operator_Roles(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE Users (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  service_provider_id INTEGER UNSIGNED NOT NULL,
  nick_id INTEGER UNSIGNED NOT NULL,
  operator_id INTEGER UNSIGNED NOT NULL,
  msisdn VARCHAR(20) NOT NULL,
  name VARCHAR(20) NULL,
  surname VARCHAR(30) NULL,
  address VARCHAR(100) NULL,
  birth_date DATE NULL,
  notes TEXT NULL,
  joined_date DATE NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(operator_id) REFERENCES Operators(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(nick_id) REFERENCES Nicks(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(service_provider_id) REFERENCES Service_Provider(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE SMS_Messages (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  service_provider_id INTEGER UNSIGNED NOT NULL,
  operator_id INTEGER UNSIGNED NOT NULL,
  user_id INTEGER UNSIGNED NOT NULL,
  time TIME NOT NULL,
  text VARCHAR(800) NOT NULL,
  sc VARCHAR(20) NOT NULL,
  direction VARCHAR(6) NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(user_id) REFERENCES Users(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(operator_id) REFERENCES Operators(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(service_provider_id) REFERENCES Service_Provider(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
);

CREATE TABLE Users_Pictures (
  picture_id INTEGER UNSIGNED NOT NULL,
  user_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(picture_id, user_id),
  FOREIGN KEY(picture_id) REFERENCES Pictures(id)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(user_id) REFERENCES Users(id)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);


--- DB SHEMA DROP ---

DROP TABLE Users_Pictures;

DROP TABLE SMS_Messages;

DROP TABLE Users;

DROP TABLE Operators;

DROP TABLE Pictures;

DROP TABLE Configuration;

DROP TABLE Nicks;

DROP TABLE Operator_Roles;

DROP TABLE Service_Provider;