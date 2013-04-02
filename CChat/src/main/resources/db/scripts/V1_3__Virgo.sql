ALTER TABLE sms_message
	ADD COLUMN end_user_price FLOAT NULL,
	ADD COLUMN end_user_price_currency VARCHAR(10);
	

ALTER TABLE users ADD COLUMN mcc_mnc VARCHAR(6);
