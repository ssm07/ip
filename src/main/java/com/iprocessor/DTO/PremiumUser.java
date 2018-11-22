package com.iprocessor.DTO;

import com.iprocessor.constants.SubscriptionType;

import java.util.Date;

public class PremiumUser extends User {
    private Long premiumUserId; //not null uniqure primary key
    private Date premiumUserCreatedDate; // not null
    private Date premiumUserExpirationDate; // not null
    private String userName; // not null foreign key referencing User
    private int subMonths;  //used in UI drop down

    //used in case of extending premium account
    private Date newPremiumUserExpirationDate;
    private int newSubMonths;


    public Date getNewPremiumUserExpirationDate() {
        return newPremiumUserExpirationDate;
    }

    public void setNewPremiumUserExpirationDate(Date newPremiumUserExpirationDate) {
        this.newPremiumUserExpirationDate = newPremiumUserExpirationDate;
    }

    public int getNewSubMonths() {
        return newSubMonths;
    }

    public void setNewSubMonths(int newSubMonths) {
        this.newSubMonths = newSubMonths;
    }

    public Long getPremiumUserId() {
        return premiumUserId;
    }

    public void setPremiumUserId(Long premiumUserId) {
        this.premiumUserId = premiumUserId;
    }

    public Date getPremiumUserCreatedDate() {
        return premiumUserCreatedDate;
    }

    public void setPremiumUserCreatedDate(Date premiumUserCreatedDate) {
        this.premiumUserCreatedDate = premiumUserCreatedDate;
    }

    public Date getPremiumUserExpirationDate() {
        return premiumUserExpirationDate;
    }

    public void setPremiumUserExpirationDate(Date premiumUserExpirationDate) {
        this.premiumUserExpirationDate = premiumUserExpirationDate;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getSubMonths() {
        return subMonths;
    }

    public void setSubMonths(int subMonths) {
        this.subMonths = subMonths;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder(" premiumUserId = " + premiumUserId + " premiumUserCreatedDate = " + premiumUserCreatedDate);
        stringBuilder.append(" premiumUserExpirationDate= " + premiumUserExpirationDate + " userName =" + userName);
        stringBuilder.append(" subMonths= " + subMonths);
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }


}
