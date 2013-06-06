CREATE TABLE `language` (
	`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
	, `short_code` CHAR(2) NOT NULL
	, `name` VARCHAR(80) NOT NULL
	, UNIQUE INDEX `idx_unq_language_short_code` (`short_code`)
	, PRIMARY KEY (`id`)
)
ENGINE=InnoDB
CHARSET=UTF8;

INSERT INTO `language` VALUES (1, "en", "English");
INSERT INTO `language` VALUES (2, "de", "German");
INSERT INTO `language` VALUES (3, "hr", "Croatian");

CREATE TABLE `operator_language` (
	`operator_id` INTEGER UNSIGNED NOT NULL
	, `language_id` INTEGER UNSIGNED NOT NULL
	, CONSTRAINT `fk_operator_language_operator_id`
	    FOREIGN KEY (`operator_id`)
	    REFERENCES `operator` (`id`)
	, CONSTRAINT `fk_operator_language_language_id`
	    FOREIGN KEY (`language_id`)
	    REFERENCES `language` (`id`)
)
ENGINE=InnoDB
CHARSET=UTF8;

CREATE TABLE `language_provider` (
	`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
	, `prefix` VARCHAR(15) NOT NULL
	, `language_id` INTEGER UNSIGNED NOT NULL
	, `send_service_bean` VARCHAR(30) NULL
	, UNIQUE INDEX `idx_unq_language_provider_prefix` (`prefix`)
	, PRIMARY KEY (`id`)
	, CONSTRAINT `fk_language_provider_language_id`
	    FOREIGN KEY (`language_id`)
	    REFERENCES `language` (`id`)
)
ENGINE=InnoDB
CHARSET=UTF8;

ALTER TABLE `service_provider` 
	ADD COLUMN `language_provider_id` INTEGER UNSIGNED DEFAULT NULL
	, ADD COLUMN `auto_created` BOOL NULL
	, ADD CONSTRAINT `fk_service_provider_language_provider_id` FOREIGN KEY(`language_provider_id`) REFERENCES `language_provider`(`id`);

UPDATE `service_provider` SET `auto_created` = 0;
ALTER TABLE `service_provider` MODIFY `auto_created` BOOL NOT NULL;
