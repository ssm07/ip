CREATE TABLE `ip`.`user_email_notification` (
  `iduser_email_notification` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NULL,
  `email_id` VARCHAR(50) NULL,
  `subject` VARCHAR(100) NULL,
  `body` VARCHAR(2000) NULL,
  `status` SMALLINT(2) NULL,
  `created_date` TIMESTAMP(6) NULL,
  `created_by` VARCHAR(100) NULL,
  PRIMARY KEY (`iduser_email_notification`));