CREATE TABLE `ip`.`premium_user_profile` (
  `premium_user_id` INT NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(50) NOT NULL,
  `premium_user_created_date` DATETIME NOT NULL,
  `premium_user_expiration_date` DATETIME NOT NULL,
  PRIMARY KEY (`premium_user_id`),
  INDEX `username_idx` (`user_name` ASC) VISIBLE,
  CONSTRAINT `username`
    FOREIGN KEY (`user_name`)
    REFERENCES `ip`.`user_profile` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



ALTER TABLE `ip`.`premium_user_profile`
DROP FOREIGN KEY `username`;
ALTER TABLE `ip`.`premium_user_profile`
CHANGE COLUMN `user_name` `user_name` VARCHAR(100) NOT NULL ;
ALTER TABLE `ip`.`premium_user_profile`
ADD CONSTRAINT `username`
  FOREIGN KEY (`user_name`)
  REFERENCES `ip`.`user_profile` (`username`);



alter table premium_user_profile
add column  isactive       smallint

ALTER TABLE premium_user_profile CHANGE `user_name` `username`  VARCHAR(100);