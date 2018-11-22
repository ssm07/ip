package com.iprocessor.controller;


import com.iprocessor.DTO.*;
import com.iprocessor.service.LoginService;
import com.iprocessor.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
public class LoginController {


    @Autowired

    User userObject;
    @Autowired
    LoginService loginService;



    @Autowired
    RegisterService registerService;

    @RequestMapping(value = "/iprocessor/isUserNameAvailable", method = RequestMethod.POST)
    public @ResponseBody boolean isUserNameAvailable(@RequestBody String userName) {
        return registerService.isUserNameAvailable(userName);
    }


    @RequestMapping(value = "/iprocessor/isLoggedIn", method = RequestMethod.GET)
    public @ResponseBody User isUserLoggedIn(HttpSession session) {

        User user = (User) session.getAttribute("userObject");
        return user;

    }

    @RequestMapping("/iprocessor/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @RequestMapping(value = "/iprocessor/login" , method = RequestMethod.POST)
    public @ResponseBody User doLogin(@RequestBody User user, HttpSession session) {
        userObject = loginService.login(user);
        session.setAttribute("userObject", userObject);
        if (userObject == null) {
            // invalid user name or password
            return null;
        } else {
            return userObject;
        }


    }


}
