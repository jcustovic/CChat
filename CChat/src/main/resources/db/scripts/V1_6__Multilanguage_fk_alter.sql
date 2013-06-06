ALTER TABLE `users` 
	ADD CONSTRAINT `fk_users_language_provider_id` FOREIGN KEY(`language_provider_id`) REFERENCES `language_provider`(`id`);
