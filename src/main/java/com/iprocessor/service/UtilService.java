package com.iprocessor.service;

import com.iprocessor.DTO.EmailDTO;
import com.iprocessor.DTO.FileDTO;
import com.iprocessor.DTO.User;
import com.iprocessor.email.EmailSender;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class provide an API for various  utility function .</p>
 *
 * @author  Saurabh Moghe, Abhijeet Sathe
 *  */
@Service
public class UtilService {

    @Autowired
    EmailSender emailSender;


    @Value("${report_path}")
    private String reportPath;


    public List<FileDTO> getAllReportFile() {
        File reportFolder = new File(reportPath);
        File[] reportFiles = reportFolder.listFiles();
        List<FileDTO> fileDTOList = new ArrayList<>();
        for (File file : reportFiles) {
            if (file.isFile() && isReportFile(file)) {
                FileDTO fileDTO = new FileDTO();
                fileDTO.setFileName(file.getName());
                fileDTO.setFilePath(file.getAbsolutePath());
                fileDTO.setSize(file.length());
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                fileDTO.setMimeType(mimeType);
                fileDTOList.add(fileDTO);
            }
        }

        return fileDTOList;

    }

    private boolean isReportFile(File file) {

        String ext = FilenameUtils.getExtension(file.getName());
        if (ext.toLowerCase().equalsIgnoreCase("xlsx")) {
            return true;
        }
        return false;
    }

    public List<FileDTO> getAllUserFiles(User user) {

        File folder = new File(user.getPath());
        File[] listOfFiles = folder.listFiles();
        List<FileDTO> fileDTOList = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile() && this.isValidFile(file)) {
                FileDTO fileDTO = new FileDTO();
                fileDTO.setFileName(file.getName());
                fileDTO.setFilePath(file.getAbsolutePath());
                fileDTO.setSize(file.length());
                fileDTO.setImageAsBase64(getBase64(file));
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                fileDTO.setMimeType(mimeType);
                fileDTOList.add(fileDTO);

            }
        }
        return fileDTOList;
    }


    public boolean isValidFile(File file) {
        String ext = FilenameUtils.getExtension(file.getName());
        if (ext.toLowerCase().equalsIgnoreCase("png") || ext.toLowerCase().equalsIgnoreCase("jpg")) {
            return true;
        }
        return false;

    }

    public String getBase64(File file) {
        String encodedfile = null;
        FileInputStream fileInputStreamReader = null;
        try {
            fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedfile;
    }


    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }


    public EmailDTO constructEmail(File attachment, User user) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAttachment(new FileSystemResource(attachment));
        emailDTO.setTo(user.getEmailId());
        emailDTO.setBody("  Thanks for using iprocessor");
        emailDTO.setSubject(" Processed Image " + attachment.getName());
        emailDTO.setAttachmentName(attachment.getName());

        return emailDTO;
    }

    public void sendEmail(String filePath, User user) {
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println(" sending email ");
            EmailDTO emailDTO = constructEmail(file, user);
            emailSender.send(emailDTO);

        } else {
            throw new RuntimeException(" file does not exist");
        }

    }
}


