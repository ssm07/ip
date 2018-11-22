package com.iprocessor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprocessor.DTO.Payment;
import com.iprocessor.DTO.PremiumUser;
import com.iprocessor.DTO.PremiumUserPaymentDTO;
import com.iprocessor.DTO.User;
import com.iprocessor.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    @Autowired
    RegisterService registerService;


    @RequestMapping(value = "/iprocessor/addUser", method = RequestMethod.POST)
    public @ResponseBody
    User addUser(@RequestBody User user) {
        System.out.println(" in add user " + user.getFirstName());
        return registerService.addUser(user);

    }

    @RequestMapping(value = "/iprocessor/addPremiumUser", method = RequestMethod.POST)
    public @ResponseBody
    PremiumUser addPremiumUser(@RequestBody PremiumUserPaymentDTO premiumUserPaymentDTO) throws Exception {
        System.out.println(" in addPremiumUser");
        ObjectMapper mapper= new ObjectMapper();
        PremiumUser premiumUser=premiumUserPaymentDTO.getPremiumUser();
        Payment payment=premiumUserPaymentDTO.getPayment();
        System.out.println(" Premium user "+premiumUser.toString());
        System.out.println(" Payment "+payment.toString());
        return registerService.addPremiumUser(premiumUser, payment,false);
    }



    @RequestMapping(value = "/iprocessor/switchToPremiumAccount", method = RequestMethod.POST)
    public @ResponseBody PremiumUser switchToPremiumAccount(@RequestBody PremiumUserPaymentDTO premiumUserPaymentDTO) throws Exception {
        System.out.println(" in addPremiumUser");
        ObjectMapper mapper= new ObjectMapper();
        PremiumUser premiumUser=premiumUserPaymentDTO.getPremiumUser();
        Payment  payment=premiumUserPaymentDTO.getPayment();
        System.out.println(" Premium user "+premiumUser.toString());
        System.out.println(" Payment "+payment.toString());
        return registerService.addPremiumUser(premiumUser, payment,true);
    }

    @RequestMapping(value="/iprocessor/extendPremiumAccount", method = RequestMethod.POST)
    public void  extendPremiumAccount(@RequestBody PremiumUserPaymentDTO premiumUserPaymentDTO){
        Payment  payment=premiumUserPaymentDTO.getPayment();
        PremiumUser premiumUser=premiumUserPaymentDTO.getPremiumUser();
        registerService.addPayment(payment,premiumUser.getPremiumUserId(),premiumUser.getUserName());
        registerService.extendExpirationDate(premiumUser);
    }


    @RequestMapping(value = "/iprocessor/updateUser" ,method = RequestMethod.POST)
    public @ResponseBody boolean updateUser( User  user){
        return registerService.updateUser(user);
    }


}
