package com.iprocessor.service;

import com.iprocessor.DTO.PremiumUser;
import com.iprocessor.repository.UserRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
/**
 * <p>
 *    This class provides an API for post expiration tasks such clean up of the resources.
 *    It is one of the command of {@link ProductionCycle}
 *   <p/>
 *
 *   @author  Saurabh Moghe, Abhijeet sathe
 * */
@Service
public class PostExpirationCommand implements Command {

    @Autowired
    UserRepository userRepository;

    @Value("${user_storage_path}")
    private String storagePath;

    @Override
    public void execute() {
        List<PremiumUser> premiumUserList = this.getExpiredPremiumUsers();
        userRepository.deletePremiumUser(premiumUserList);
        //free up the space
        this.freeUpSpace(premiumUserList);
    }


    public List<PremiumUser> getExpiredPremiumUsers() {

        return userRepository.getExpiredPremiumUsers();

    }

    public void freeUpSpace(List<PremiumUser> premiumUserList) {

        for (PremiumUser premiumUser : premiumUserList) {
            File file = new File(storagePath + File.separator + premiumUser.getUserName());
            if (file.exists()) {
                try {
                    FileUtils.cleanDirectory(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(" folder does not exist " + file.getName());
            }

        }

    }
}
