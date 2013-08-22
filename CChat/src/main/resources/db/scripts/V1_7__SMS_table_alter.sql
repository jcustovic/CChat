ALTER TABLE `sms_message` 
	ADD COLUMN used_send_bean VARCHAR(30) NULL
	, ADD COLUMN `response_to_id` INTEGER UNSIGNED DEFAULT NULL
	, ADD CONSTRAINT `fk_sms_message_response_to_id` FOREIGN KEY(`response_to_id`) REFERENCES `sms_message`(`id`);
