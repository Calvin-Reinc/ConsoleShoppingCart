/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.model;

/**
 *
 * @author squid
 */
public class CartItemSummary {
    private final String productId;
    private final String productName;
    private final int quantity;
    private final double price;

    public CartItemSummary(String productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    // Serialize a single item into a colon-delimited string
    public String serialize() {
        return productId + ":" + productName + ":" + quantity + ":" + price;
    }

    // Deserialize a single item from string
    public static CartItemSummary deserialize(String str) {
        String[] parts = str.split(":");
        return new CartItemSummary(
            parts[0],
            parts[1],
            Integer.parseInt(parts[2]),
            Double.parseDouble(parts[3])
        );
    }
    
}
