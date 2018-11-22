package com.iprocessor.service;

import com.iprocessor.DTO.EmailDTO;
import com.iprocessor.DTO.User;
import com.iprocessor.email.EmailSender;
import com.iprocessor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * <p>
 *    This class provides an API for pre expiration tasks such as sending notification to the use
 *    whose account is going to expired within one week.
 *    It is one of the command of {@link ProductionCycle}
 *   <p/>
 *
 *   @author  Saurabh Moghe, Abhijeet sathe
 * */
@Service
public class PreExpirationCommand  implements  Command {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailSender emailSender;
    @Override
    public void execute() {
       System.out.println(" PreExpirationCommand Started ");
        List<User> userList=this.getAllPremiumUserForWeekExpirationNotification();
        sendEmailNotification(userList);

    }

     public List<User> getAllPremiumUserForWeekExpirationNotification(){
      return  userRepository.getAllPremiumUserForWeekExpirationNotification();
     }

     public void sendEmailNotification(List<User> userList){
         for(User user: userList){
             EmailDTO emailDTO= new EmailDTO();
             emailDTO.setSubject("Your Premium account will expire within a week");
             emailDTO.setBody(constructEmailBody(user.getFirstName()));
             emailDTO.setTo(user.getEmailId());
             emailSender.send(emailDTO);
         }
     }



     public String  constructEmailBody(String firstName){
         StringBuilder str= new StringBuilder(" Hello "+firstName+",");
         str.append("\n Your subscription of iProcessor premium user account will be expired within week.");
         str.append("\n.  All your stored files will be deleted  and you no longer will be able to access all the feature of application.");
         str.append("\n  Please recharge in order to continue premium user account. ");
         str.append("\n\n Regards,");
         str.append("\n iProcessor Admin");
         return  str.toString();

     }

}
