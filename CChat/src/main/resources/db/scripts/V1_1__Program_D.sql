CREATE TABLE programd_user (
	id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
	, user_id VARCHAR(128)
	, password VARCHAR(128)
	, bot_id VARCHAR(128)
	, PRIMARY KEY(id)
	, INDEX programd_user_user_id (user_id)
	, UNIQUE (user_id, bot_id)
);


CREATE TABLE programd_predicate (
	id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
	, user_id VARCHAR(128)
	, bot_id VARCHAR(128)
	, name VARCHAR(128)
	, `value` LONGTEXT
	, PRIMARY KEY(id)
	, INDEX programd_predicate_user_id (user_id)
);
