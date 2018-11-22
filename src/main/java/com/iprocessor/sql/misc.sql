INSERT INTO user_profile (username,password,first_name,last_name,email_id,is_premium_user,is_admin,created_date,expiration_date,resource_usage,path,is_email_verified)
values ('admin1'  ,'admin', 'Saurabh', 'Moghe', 'ipprocessorad@gmail.com',0,1,CURDATE(),null,null,null,0)


ALTER TABLE admin_user_profile MODIFY admin_id INTEGER NOT NULL AUTO_INCREMENT;

INSERT INTO admin_user_profile  values(null,-1,CURDATE(),'admin1')


select * from admin_user_profile;payment



ALTER TABLE payment  MODIFY payment_id INTEGER NOT NULL AUTO_INCREMENT;