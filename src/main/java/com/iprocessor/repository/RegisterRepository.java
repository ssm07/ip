package com.iprocessor.repository;

import com.iprocessor.DTO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegisterRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean  isUserNameAvailable(String userName){
      Integer result=      jdbcTemplate.queryForObject(" SELECT COUNT(*) FROM  user_profile   WHERE user_profile.username= ? " ,Integer.class, userName);
        return   result==0?true:false;
    }

    public boolean updateUser(User user){
        Object[] params = {user.getFirstName(), user.getLastName(),user.getEmailId(),user.getEmailId()};
        String sql= "UPDATE user_profile SET  first_name= ? ,last_name= ? , email_id= ?  where username= ? ";
        int result= jdbcTemplate.update(sql,params);
         return  result ==1 ? true:false;
    }
}
