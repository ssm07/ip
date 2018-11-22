CREATE TABLE `ip`.`admin_user_profile` (
  `admin_id` INT NOT NULL,
  `created_by` INT NOT NULL,
  `admin_created_date` DATETIME NOT NULL,
  `username` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`admin_id`),
  INDEX `use_idx` (`username` ASC) VISIBLE,
  CONSTRAINT `adminusername`
    FOREIGN KEY (`username`)
    REFERENCES `ip`.`user_profile` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);