CREATE TABLE service_provider (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
  , sc VARCHAR(20) NOT NULL
  , provider_name VARCHAR(30) NOT NULL
  , service_name VARCHAR(30) NOT NULL
  , billing_amount FLOAT NULL
  , description VARCHAR(200) NULL
  , disabled BOOL NOT NULL DEFAULT false
  , send_service_bean VARCHAR(30) NULL
  , PRIMARY KEY(id)
  , UNIQUE(sc, provider_name)
);

CREATE TABLE service_provider_keyword (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
  , keyword VARCHAR(20) NOT NULL
  , billing_amount FLOAT(8, 2) NULL
  , disabled BOOL NOT NULL DEFAULT false
  , service_provider_id INTEGER UNSIGNED NOT NULL
  , PRIMARY KEY(id)
  , FOREIGN KEY(service_provider_id) REFERENCES service_provider(id)
  , UNIQUE(keyword, service_provider_id)
);


CREATE TABLE operator_role (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
  , name VARCHAR(20) NOT NULL
  , description VARCHAR(100) NULL
  , PRIMARY KEY(id)
  , UNIQUE(name)
);


CREATE TABLE operator (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
  , operator_role_id INTEGER UNSIGNED NOT NULL
  , username VARCHAR(30) NOT NULL
  , pass VARCHAR(50) NOT NULL
  , name VARCHAR(20) NULL
  , surname VARCHAR(30) NULL
  , email VARCHAR(50) NULL
  , is_active BOOL NOT NULL DEFAULT false
  , is_external BOOL NOT NULL DEFAULT false
  , disabled BOOL NOT NULL DEFAULT false
  , PRIMARY KEY(id)
  , FOREIGN KEY(operator_role_id) REFERENCES operator_role(id)
);


CREATE TABLE nick (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
  , name VARCHAR(20) NOT NULL
  , description VARCHAR(300) NULL
  , is_keyword BOOL NOT NULL DEFAULT false
  , PRIMARY KEY(id)
  , UNIQUE(name)
);


CREATE TABLE keyword_conf (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
  , operator_id INTEGER UNSIGNED NOT NULL
  , nick_id INTEGER UNSIGNED NOT NULL
  , service_provider_id INTEGER UNSIGNED NOT NULL
  , PRIMARY KEY(id)
  , FOREIGN KEY(operator_id) REFERENCES operator(id)
  , FOREIGN KEY(service_provider_id) REFERENCES service_provider(id)
  , FOREIGN KEY(nick_id) REFERENCES nick(id)
  , UNIQUE(operator_id, service_provider_id, nick_id)
);


CREATE TABLE configuration (
  name VARCHAR(50) NOT NULL
  , value VARCHAR(100) NOT NULL
  , PRIMARY KEY(name)
);


CREATE TABLE picture (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
  , nick_id INTEGER UNSIGNED
  , name VARCHAR(50) NOT NULL
  , file_type VARCHAR(15) NOT NULL
  , file_size BIGINT NOT NULL
  , PRIMARY KEY(id)
  , FOREIGN KEY(nick_id) REFERENCES nick(id)
      ON DELETE SET NULL
      ON UPDATE NO ACTION
);


CREATE TABLE users (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
  , service_provider_id INTEGER UNSIGNED NOT NULL
  , operator_id INTEGER UNSIGNED NULL
  , nick_id INTEGER UNSIGNED NULL
  , msisdn VARCHAR(20) NOT NULL
  , name VARCHAR(20) NULL
  , surname VARCHAR(30) NULL
  , address VARCHAR(100) NULL
  , birth_date DATE NULL
  , notes TEXT NULL
  , joined_date DATETIME NOT NULL
  , last_message DATETIME NOT NULL
  , unread_message_count INTEGER UNSIGNED NOT NULL DEFAULT 0
  , deleted BOOL NOT NULL DEFAULT false
  , PRIMARY KEY(id)
  , FOREIGN KEY(operator_id) REFERENCES operator(id)
  , FOREIGN KEY(nick_id) REFERENCES nick(id)
	  ON DELETE SET NULL
      ON UPDATE NO ACTION
  , FOREIGN KEY(service_provider_id) REFERENCES service_provider(id)
  , UNIQUE(msisdn, service_provider_id)
);


CREATE TABLE sms_message (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
  , gateway_id VARCHAR(36) NULL
  , service_provider_id INTEGER UNSIGNED NOT NULL
  , service_provider_keyword_id INTEGER UNSIGNED NULL
  , operator_id INTEGER UNSIGNED NULL
  , nick_id INTEGER UNSIGNED NULL
  , user_id INTEGER UNSIGNED NOT NULL
  , time DATETIME NOT NULL
  , text VARCHAR(800) NOT NULL
  , sc VARCHAR(20) NOT NULL
  , direction ENUM('IN', 'OUT') NOT NULL
  , delivery_status VARCHAR(15) NULL
  , delivery_msg VARCHAR(200) NULL
  , PRIMARY KEY(id)
  , FOREIGN KEY(user_id) REFERENCES users(id)
  , FOREIGN KEY(operator_id) REFERENCES operator(id)
  , FOREIGN KEY(nick_id) REFERENCES nick(id)
  	  ON DELETE SET NULL
      ON UPDATE NO ACTION
  , FOREIGN KEY(service_provider_id) REFERENCES service_provider(id)
  	  ON DELETE NO ACTION
      ON UPDATE NO ACTION
  , FOREIGN KEY(service_provider_keyword_id) REFERENCES service_provider_keyword(id)
  	  ON DELETE NO ACTION
      ON UPDATE NO ACTION
);


CREATE TABLE users_picture (
  picture_id INTEGER UNSIGNED NOT NULL
  , user_id INTEGER UNSIGNED NOT NULL
  , PRIMARY KEY(picture_id, user_id)
  , FOREIGN KEY(picture_id) REFERENCES picture(id)
  , FOREIGN KEY(user_id) REFERENCES users(id)
);
