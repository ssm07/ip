package com.iprocessor.service;

import com.iprocessor.DTO.PremiumUser;
import com.iprocessor.DTO.User;
import com.iprocessor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  <p> This service provides an  API for admin functionality</p>
 *
 * @author  Saurabh Moghe, Abhijeet Sathe
 * */
@Service
public class AdminDashBoardService {

    @Autowired
    ProductionCycle productionCycle;

    @Autowired
    UserRepository userRepository;

    /**<p>
     *   This function delete user from the system
     * </p>
     *  */
    public void deleteUser( User user){
        userRepository.deleteUserFromSystem(user);
    }

    /**
     * <p> This function downgrade premium user to standard user</p>
     * */
    public void downgradeUser(PremiumUser premiumUser){
       List<PremiumUser> premiumUsers=new ArrayList<>();
        premiumUsers.add(premiumUser);
        userRepository.deletePremiumUser(premiumUsers);
    }

    /**
     * <p>This function fetches list of users with given search string </p>
     * */
    public List<User> searchUsers(String searchString){
        return userRepository.searchUsers(searchString);
    }

}
