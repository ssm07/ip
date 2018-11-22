package com.iprocessor.DTO;


import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.io.Serializable;
import java.util.Date;


@SessionScope
@Component
public class User implements Serializable {
     private String userName;//unique, not null, primary key
     private String password; // not null
     private String firstName; //not null
     private String lastName; // not null
     private String emailId; //not null unique
     private int premiumUser; //not null either y or n
     private int admin;  // not null either y or n
     private Date createdDate;// not null
     private Long resourceUsage; //  not null 1GB for premium user/admin, 10 Mb for normal user
     private  String path; // not null
     private String errorMessage;


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getResourceUsage() {
        return resourceUsage;
    }

    public void setResourceUsage(Long resourceUsage) {
        this.resourceUsage = resourceUsage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPremiumUser() {
        return premiumUser;
    }

    public void setPremiumUser(int premiumUser) {
        this.premiumUser = premiumUser;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }



    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder= new StringBuilder(" \tuserName ="+userName + "\t password= "+password);
        stringBuilder.append(" \tfirstName ="+firstName+"\tlastName="+lastName);
        stringBuilder.append("\temailId="+emailId+"\t premiumUser="+ premiumUser +"\tisAdmin="+admin);
        stringBuilder.append("\tcreatedDate="+createdDate+"\t resourceUsage="+resourceUsage);
        stringBuilder.append("\t path"+path);
        return stringBuilder.toString();
    }
}
