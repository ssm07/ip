package com.iprocessor.DTO;

public class PremiumUserPaymentDTO {
    private Payment payment;
    private PremiumUser premiumUser;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PremiumUser getPremiumUser() {
        return premiumUser;
    }

    public void setPremiumUser(PremiumUser premiumUser) {
        this.premiumUser = premiumUser;
    }
}
