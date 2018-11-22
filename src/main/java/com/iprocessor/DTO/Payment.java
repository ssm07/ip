package com.iprocessor.DTO;

import org.springframework.transaction.TransactionDefinition;

public class Payment {
    private Long paymentId; //   primary key
    private Long premiumUserId; // foreign key
    private String userName; // foreign key
    private Long paymentDate;  // not null
    // card details and other bank details
    //
    private Long cardNumber;
    private int cvv;
    private String expirationDateAsString;
    private String cardHolderNumber;
    private int paymentAmount;

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getPremiumUserId() {
        return premiumUserId;
    }

    public void setPremiumUserId(Long premiumUserId) {
        this.premiumUserId = premiumUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Long paymentDate) {
        this.paymentDate = paymentDate;
    }


    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }


    public String getExpirationDateAsString() {
        return expirationDateAsString;
    }

    public void setExpirationDateAsString(String expirationDateAsString) {
        this.expirationDateAsString = expirationDateAsString;
    }

    public String getCardHolderNumber() {
        return cardHolderNumber;
    }

    public void setCardHolderNumber(String cardHolderNumber) {
        this.cardHolderNumber = cardHolderNumber;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("\t paymentId= " + paymentId + "\t premiumUserId= " + premiumUserId + "\t userName=" + userName);
        stringBuilder.append("\t paymentDate= " + paymentDate + "\t cardNumber=" + cardNumber + "\t cvv=" + cvv + "\t expirationDateAsString" + expirationDateAsString);
        stringBuilder.append("\t cardHolderNumber " + cardHolderNumber);
        return stringBuilder.toString();
    }
}
