/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aop.aopcart.payment;

/**
 *
 * @author squid
 */
public class PayPalPayment implements PaymentManager{
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }
    

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Redirecting to PayPal API for account: " + email);
        System.out.println("Deducted $" + String.format("%.2f", amount) + " successfully.");
        return true;
    }

    @Override
    public String getPaymentDetails() {
        return "PayPal Account (" + email + ")";
    }
    
}
