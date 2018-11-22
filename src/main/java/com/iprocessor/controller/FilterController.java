package com.iprocessor.controller;

import com.iprocessor.DTO.ImageDTO;
import com.iprocessor.DTO.User;
import com.iprocessor.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLConnection;

@RestController
@RequestMapping("/iprocessor")
public class FilterController {

    @Autowired
    FilterService filterService;
    @RequestMapping(value = "/filter",method = RequestMethod.POST)
     public @ResponseBody
    ImageDTO applyFilter(@RequestParam(value = "file") MultipartFile file, ImageDTO imageObj, HttpSession session){
        User userObject = (User) session.getAttribute("userObject");
        if(userObject!=null) {
              return filterService.applyFilter(file, imageObj, userObject);
        }else{
            imageObj.getErrorMessage().add("User is not logged in");
            return  imageObj;

        }

     }


}
