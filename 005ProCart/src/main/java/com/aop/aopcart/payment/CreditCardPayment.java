/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aop.aopcart.payment;

/**
 *
 * @author squid
 */
public class CreditCardPayment implements PaymentManager {
    private String cardNumber;
    private String cardHolder;

    public CreditCardPayment(String cardNumber, String cardHolder) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }
    
 
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Connecting to secure Credit Card gateway...");
        System.out.println("Charging $" + String.format("%.2f", amount) + " to cardholder " + cardHolder);
        return true; 
    }

    @Override
    public String getPaymentDetails() {
        String masked = cardNumber.length() > 4 ? "****-****-****-" + cardNumber.substring(cardNumber.length() - 4) : "****";
        return "Credit Card (" + masked + ")";
    }
    
}
