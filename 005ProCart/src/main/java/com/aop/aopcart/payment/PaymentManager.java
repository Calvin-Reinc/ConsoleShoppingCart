/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aop.aopcart.payment;

/**
 *
 * @author squid
 */
public interface PaymentManager {
    boolean processPayment(double amount);
    String getPaymentDetails();
    
}
