package com.iprocessor.email;

import com.iprocessor.DTO.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailSender  {


    @Autowired
    private JavaMailSender javaMailSender;

    public  void send(EmailDTO emailDTO){

        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=null;
        try {
             helper= new MimeMessageHelper(message,true);
             helper.setTo(emailDTO.getTo());
             helper.setSubject(emailDTO.getSubject());
             helper.setText(emailDTO.getBody());
             if(emailDTO.getAttachmentName() != null && emailDTO.getAttachment() != null ) {
                 helper.addAttachment(emailDTO.getAttachmentName(), emailDTO.getAttachment());
             }
             javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
