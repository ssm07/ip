CREATE TABLE `ip`.`payment` (
  `payment_id` INT NOT NULL,
  `premium_user_id` INT(11) NOT NULL,
  `username` VARCHAR(100) NOT NULL,
  `payment_date` DATETIME NOT NULL,
  PRIMARY KEY (`payment_id`),
  INDEX `paymentusername_idx` (`username` ASC) VISIBLE,
  INDEX `paymentpremiumuser_idx` (`premium_user_id` ASC) VISIBLE,
  CONSTRAINT `paymentusername`
    FOREIGN KEY (`username`)
    REFERENCES `ip`.`user_profile` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `paymentpremiumuser`
    FOREIGN KEY (`premium_user_id`)
    REFERENCES `ip`.`premium_user_profile` (`premium_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

alter table payment
add column card_expiration_date varchar(6),
add column card_holder_name varchar(100),
add column  card_number  bigint  ,
add column  cvv       smallint


alter table payment
add column  payment_amount       smallint