package com.iprocessor.service;


import com.iprocessor.DTO.ImageDTO;
import com.iprocessor.DTO.User;
import com.iprocessor.constants.IPConstants;
import com.iprocessor.email.EmailSender;
import com.iprocessor.repository.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

/**
 * <p>
 *     This class is gateway for all the filters  in the system.
 *     It provides an API to save uploaded and processed image.
 * </p>
 * */

@Service
public class FilterService {


    @Value("${user_storage_path}")
    private String storagePath;

    @Autowired
    User user;

    @Autowired
    @Qualifier("gaussian")
    Chain chain;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSender emailSender;

    /**
     * <p> This method applies filter on uploaded image
     *  Internally it uses chain of responsibility design pattern to call appropriate Filter.
     * </p>
     *  */
    public ImageDTO applyFilter(MultipartFile file, ImageDTO imageDTO, User user) {
        boolean isFileSavedToUserFolder = this.saveFileToUserFolder(file, imageDTO, user);
        if (isFileSavedToUserFolder) {
            final String inputImagePath = user.getPath() + File.separator + IPConstants.TEMP + File.separator + file.getOriginalFilename();
            final String outputImagePath = user.getPath() + File.separator + file.getOriginalFilename();
            imageDTO.setImagePath(user.getPath() + File.separator + IPConstants.TEMP + File.separator + file.getOriginalFilename());
            imageDTO.setDestinationPath(outputImagePath);
            //Start the chaining process
            chain.applyFilter(imageDTO);
            // if no error message , good to go to create base64 of image
            if (imageDTO.getErrorMessage().isEmpty()) {
                imageDTO.setFileName(file.getOriginalFilename());
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                imageDTO.setMimeType(mimeType);
                this.encodeFileToBase64Binary(imageDTO);
            }
        } else {
            StringBuilder errorMsg = new StringBuilder(" Unable to save file = " + file.getOriginalFilename());
            imageDTO.getErrorMessage().add(errorMsg.toString());
        }

        return imageDTO;


    }

    /**
     * <p>This method encode image as base64 string</p>
     * */
    public void encodeFileToBase64Binary(ImageDTO imageDTO) {
        File file = new File(imageDTO.getDestinationPath());
        FileInputStream fileInputStreamReader = null;
        String encodedfile;
        try {
            fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
            imageDTO.setImageAsBase64(encodedfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * <p> This method saves uploaded file to temp folder.
     *     It also check whether user folder has enough  space available  for processing.
     * </p>
     * */
    public boolean saveFileToUserFolder(MultipartFile file, ImageDTO imageDTO, User user) {

        boolean result = false;
        File tempDirectory;
        try {
            long availableSpace = user.getResourceUsage() - file.getSize();
            if (availableSpace > 0) {
                //check if temp folder exists
                tempDirectory = new File(user.getPath() + File.separator + IPConstants.TEMP);
                if (tempDirectory.exists() && tempDirectory.isDirectory()) {
                    String sourceFilePath = user.getPath() + File.separator + IPConstants.TEMP + File.separator + file.getOriginalFilename();
                    File sourceFile = new File(sourceFilePath);
                    sourceFile.createNewFile();
                    // writing  uploaded file to temp location
                    file.transferTo(sourceFile);
                    //update usage  resource usage
                    if (userRepository.updateResourceUsage(availableSpace, user.getUserName()))
                        result = true;
                } else {
                    if (tempDirectory.mkdirs()) {
                        String sourceFilePath = user.getPath() + File.separator + IPConstants.TEMP + File.separator + file.getOriginalFilename();
                        File sourceFile = new File(sourceFilePath);
                        sourceFile.createNewFile();
                        // writing  uploaded file to temp location
                        file.transferTo(sourceFile);
                        //create  empty file at destination so in filter applyFilter code we can write data to the file
                        File destinationFile = new File(user.getPath() + File.separator + file.getOriginalFilename());
                        destinationFile.createNewFile();
                        //update usage  resource usage
                        if (userRepository.updateResourceUsage(availableSpace, user.getUserName()))
                            result = true;
                    } else {
                        imageDTO.getErrorMessage().add("Unable to create temp directory");
                    }
                }

            } else {

                imageDTO.getErrorMessage().add("Does not have sufficient space");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
