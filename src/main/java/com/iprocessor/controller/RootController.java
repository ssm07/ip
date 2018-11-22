package com.iprocessor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

     @RequestMapping("/iprocessor")
       public String root(){
         System.out.println(" *********** Root controller ***************");
           return "index";
       }
}
