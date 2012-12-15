CREATE TABLE bot (
	id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
	, name VARCHAR(50) NOT NULL
	, description VARCHAR(1000)
	, online BOOL NOT NULL DEFAULT true
	, PRIMARY KEY(id)
	, UNIQUE (name)
)
ENGINE=InnoDB
CHARSET=UTF8;

ALTER TABLE `users` ADD COLUMN `bot_id` INTEGER UNSIGNED DEFAULT NULL;
ALTER TABLE `users` ADD CONSTRAINT `bot_id_fk` FOREIGN KEY(`bot_id`) REFERENCES `bot`(`id`);

ALTER TABLE `sms_message` ADD COLUMN `bot_response` BOOL NOT NULL DEFAULT false;

INSERT INTO `bot`(name, description, online) VALUES ("SimpleBot", "First simple bot", true);
