package com.iprocessor.service;

import com.google.common.hash.Hashing;
import com.iprocessor.DTO.Payment;
import com.iprocessor.DTO.PremiumUser;
import com.iprocessor.DTO.User;
import com.iprocessor.constants.IPConstants;
import com.iprocessor.repository.UserRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class LoginService {
    @Autowired
    UserRepository userRepository;


    @Value("${user_storage_path}")
    private String storagePath;


    public User login(User user) {
        String passwordHash= Hashing.sha256()
                .hashString(user.getPassword(), StandardCharsets.UTF_8)
                .toString();
        user.setPassword(passwordHash);
         user =userRepository.login(user);
         if( user !=null && user.getPremiumUser()==1){
          PremiumUser premiumUser= userRepository.getPremiumUser(user);
             BeanUtils.copyProperties(user,premiumUser);
             return premiumUser;
         }
         return   user;

    }




}
