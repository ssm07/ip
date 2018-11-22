package com.iprocessor.controller;

import com.iprocessor.DTO.FileDTO;
import com.iprocessor.DTO.User;

import com.iprocessor.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLConnection;
import java.util.List;


/**
 * <p>
 *
 *
 *   This  controller provides an API  for utility function like
 *     download, email etc.
 *
 * </p>
 *
 *
 * @since  20-oct-2018
 * @version  1.0
 * @author  Saurabh Moghe, Abhijeet Sathe
 *
 * */
@Controller
public class UtilController {


    @Autowired
    UtilService utilService;

    @RequestMapping(value = "/iprocessor/user/getUserFiles" ,method = RequestMethod.POST)
     public  @ResponseBody List<FileDTO> getAllUserFiles(@RequestBody User user){
        System.out.println("in UtilController :: getAllUserFiles called");
        System.out.println(user.toString());
      return utilService.getAllUserFiles(user);
    }

    @RequestMapping(value = "/iprocessor/user/deleteFile", method = RequestMethod.DELETE)
    public @ResponseBody Boolean deleteFile(@RequestBody String filePath){
       return  utilService.deleteFile(filePath);
    }



    @RequestMapping(value = "/iprocessor/sendEmail", method = RequestMethod.GET)
    public void  sendEmail(@RequestParam String filePath, HttpSession session){
        try {
            User userObject = (User) session.getAttribute("userObject");
            utilService.sendEmail(filePath, userObject);
        }catch (Exception  e){
            e.printStackTrace();
        }
    }
    @RequestMapping(value="/iprocessor/downloadImage",method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public  void  downloadImage(@RequestParam String filePath, HttpServletResponse response) throws IOException {

        File file= new File(filePath);
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
        System.out.println(" MIME Type ="+mimeType);
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

}
