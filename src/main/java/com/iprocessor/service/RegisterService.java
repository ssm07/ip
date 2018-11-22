package com.iprocessor.service;

import com.google.common.hash.Hashing;
import com.iprocessor.DTO.Payment;
import com.iprocessor.DTO.PremiumUser;
import com.iprocessor.DTO.User;
import com.iprocessor.constants.IPConstants;
import com.iprocessor.repository.RegisterRepository;
import com.iprocessor.repository.UserRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * <p> This class provides an  API for various  user registration  processes.</p>
 * @author  Saurabh Moghe, Abhijeet Sathe
 * */
@Service
public class RegisterService {

    @Autowired
    RegisterRepository registerRepository;

    @Autowired
    UserRepository userRepository;


    @Value("${user_storage_path}")
    private String storagePath;

    public boolean isUserNameAvailable(String userName) {
        return registerRepository.isUserNameAvailable(userName);
    }


    public boolean updateUser(User user) {
        return registerRepository.updateUser(user);
    }


    /**
     * <p> This method  creates standard user
     * </p>
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User addUser(User user) {
        System.out.println(" in addUser ");

        if (this.createUserFolder(user)) {
            user.setCreatedDate(new Date());
            if (user.getPremiumUser() == 1) {
                user.setResourceUsage(IPConstants.OneGB);
            } else if (user.getAdmin() == 0) {
                user.setResourceUsage(IPConstants.TENMB);
            }
            String passwordHash=Hashing.sha256()
                    .hashString(user.getPassword(), StandardCharsets.UTF_8)
                    .toString();
            user.setPassword(passwordHash);
            userRepository.addUser(user);
        } else {
            System.out.println(" Error occurred");
        }


        return user;
    }

    private Date calculateExpiraryDate(int month) {
        Date expiraryDate = DateUtils.addMonths(new Date(), month);
        return expiraryDate;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PremiumUser addPremiumUser(PremiumUser premiumUser) {

        premiumUser.setPremiumUserCreatedDate(new Date());
        premiumUser.setPremiumUserExpirationDate(this.calculateExpiraryDate(premiumUser.getSubMonths()));
        return userRepository.addPremiumUSer(premiumUser);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PremiumUser addPremiumUser(PremiumUser premiumUser, Payment payment, boolean isExistingStandardUser) {
        //check for existing standard  user
        if (!isExistingStandardUser) {
            addUser(premiumUser);
        } else {
            //update existing user flag
            userRepository.upgradeUser(premiumUser);
        }
        premiumUser = addPremiumUser(premiumUser);
        addPayment(payment, premiumUser.getPremiumUserId(), premiumUser.getUserName());
        return premiumUser;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addPayment(Payment payment, long premiumUserId, String userName) {
        userRepository.addPayment(payment, premiumUserId, userName);
    }

    private boolean createUserFolder(User user) {
        boolean result = false;
        boolean isUserFolder = false;
        boolean isTempFolder = false;
        final String userFolderPath = storagePath + File.separator + user.getUserName();
        final String tempFolderPath = userFolderPath + File.separator + IPConstants.TEMP;
        File userFolder = new File(userFolderPath);
        File tempFolder = new File(tempFolderPath);
        //logic to create user folder
        if (userFolder.exists()) {
            isUserFolder = true;
            user.setPath(userFolderPath);
        } else {
            isUserFolder = userFolder.mkdirs();
            user.setPath(userFolderPath);
        }
        //logic to create temp folder
        if (tempFolder.exists()) {
            isTempFolder = true;
        } else {
            isTempFolder = tempFolder.mkdirs();
        }
        result = isTempFolder && isUserFolder;

        return result;
    }

    /**
     * <p>
     * This method updates  premium user expiration date.
     *
     * </p>
     */

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void extendExpirationDate(PremiumUser premiumUser) {
        System.out.println(" in  extendExpirationDate service");
        Date newPremiumExpirationDate = DateUtils.addMonths(premiumUser.getPremiumUserExpirationDate(), premiumUser.getNewSubMonths());
        System.out.println("\n  newPremiumExpirationDate " + newPremiumExpirationDate + "\t new  sub months" + premiumUser.getNewSubMonths());
        premiumUser.setNewPremiumUserExpirationDate(newPremiumExpirationDate);
        userRepository.extendExpirationDate(premiumUser);
    }
}
