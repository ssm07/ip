CREATE TABLE `ip`.`user_profile` (
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  `first_name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `title` VARCHAR(6) NOT NULL,
  `email_id` VARCHAR(50) NOT NULL,
  `phone_no` VARCHAR(20) NULL,
  `is_premium_user` TINYINT(1) NOT NULL,
  `is_admin` TINYINT(1) NOT NULL,
  `is_email_verified` TINYINT(1) NOT NULL,
  `created_date` TIMESTAMP(6) NOT NULL,
  `expiration_date` TIMESTAMP(6) NULL,
  `resource_usage` BIGINT(15) NULL,
  `path` VARCHAR(100) NULL,
  PRIMARY KEY (`username`));



ALTER TABLE user_profile CHANGE `is_premium_user` `premium_user`  TINYINT(1);



ALTER TABLE user_profile CHANGE `is_admin` `admin`  TINYINT(1);