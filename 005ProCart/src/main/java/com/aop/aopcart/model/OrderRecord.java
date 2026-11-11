/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.model;

import com.aop.aopcart.enums.PaymentType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author squid
 */
public class OrderRecord {
    private String orderId;
    private final String customerId; // Links order directly to the customer
    private final LocalDateTime orderDate;
    private List<CartItem> purchasedItems;
    private List<CartItemSummary> items;
    private double subtotal;
    private double discountAmount;
    private double finalTotal;
    private PaymentType paymentType;

    public OrderRecord(String orderId, String customerId,List<CartItemSummary> purchasedItems, double finalTotal, PaymentType paymentType, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = List.copyOf(items);
        this.finalTotal = finalTotal;
        this.paymentType = paymentType;
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() { return customerId; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    
    public List<CartItem> getPurchasedItems() {
        return purchasedItems;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public List<CartItemSummary> getItems() {
        return items;
    }

    
    
    public String getOrderReceipt() {
        return String.format("Receipt [ID: %s] | Date: %s | Subtotal: $%.2f | Discount: -$%.2f | Final: $%.2f | Paid via: %s",
                orderId, orderDate, subtotal, discountAmount, finalTotal, paymentType.getDisplayName());
    }
    
    // Serialize order into a single pipe-delimited line for orders.txt
    // Format: orderId | customerId | orderDate | totalAmount | paymentMethod | item1;item2;item3
    public String serialize() {
        String itemsSerialized = items.stream()
                .map(CartItemSummary::serialize)
                .collect(Collectors.joining(";"));

        return String.join("|",
                orderId,
                customerId,
                orderDate.toString(),
                String.valueOf(finalTotal),
                paymentType.toString(),
                itemsSerialized
        );
    }
    
    // Deserialize order from a line in orders.txt
    public static OrderRecord deserialize(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid order record format: " + line);
        }

        String orderId = parts[0];
        String customerId = parts[1];
        LocalDateTime orderDate = LocalDateTime.parse(parts[2]);
        double totalAmount = Double.parseDouble(parts[3]);
        PaymentType paymentType = PaymentType.valueOf(parts[4].toUpperCase());

        List<CartItemSummary> items = Arrays.stream(parts[5].split(";"))
                .filter(s -> !s.trim().isEmpty())
                .map(CartItemSummary::deserialize)
                .collect(Collectors.toList());

        return new OrderRecord(orderId, customerId, items, totalAmount, paymentType, orderDate);
    }
    
}
