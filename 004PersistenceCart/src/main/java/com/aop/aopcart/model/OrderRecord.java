/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.model;

import com.aop.aopcart.enums.PaymentType;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author squid
 */
public class OrderRecord {
    private String orderId;
    private List<CartItem> purchasedItems;
    private double subtotal;
    private double discountAmount;
    private double finalTotal;
    private PaymentType paymentType;
    private LocalDateTime orderTimestamp;

    public OrderRecord(String orderId, List<CartItem> purchasedItems, double subtotal, double discountAmount, double finalTotal, PaymentType paymentType) {
        this.orderId = orderId;
        this.purchasedItems = List.copyOf(purchasedItems);
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.finalTotal = finalTotal;
        this.paymentType = paymentType;
        this.orderTimestamp = LocalDateTime.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public List<CartItem> getPurchasedItems() {
        return purchasedItems;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public LocalDateTime getOrderTimestamp() {
        return orderTimestamp;
    }
    
    public String getOrderReceipt() {
        return String.format("Receipt [ID: %s] | Date: %s | Subtotal: $%.2f | Discount: -$%.2f | Final: $%.2f | Paid via: %s",
                orderId, orderTimestamp, subtotal, discountAmount, finalTotal, paymentType.getDisplayName());
    }
    
}
