package com.iprocessor.DTO;



import java.util.Date;

public class ReportDTO {

    private String firstName;
    private String lastName;
    private Date createdDate;
    private Date expirationDate;
    private Long resourceUsage;
    private boolean isPremiumUser;
    private Integer paymentAmount;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getResourceUsage() {
        return resourceUsage;
    }

    public void setResourceUsage(Long resourceUsage) {
        this.resourceUsage = resourceUsage;
    }

    public boolean getIsPremiumUser() {
        return isPremiumUser;
    }

    public void setIsPremiumUser(boolean isPremiumUser) {
        this.isPremiumUser = isPremiumUser;
    }

    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
