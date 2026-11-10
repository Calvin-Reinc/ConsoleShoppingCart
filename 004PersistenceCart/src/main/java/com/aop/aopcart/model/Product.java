/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.model;

import com.aop.aopcart.enums.Category;

/**
 *
 * @author squid
 */
public abstract class Product {
    private String productId;
    private String name;
    private double price;
    private int stockQuantity;
    private Category collectionCategory;

    public Product(String productId, String name, double price, int stockQuantity, Category collectionCategory) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.collectionCategory = collectionCategory;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public Category getCollectionCategory() {
        return collectionCategory;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public void reduceStock(int quantity) {
        this.stockQuantity -= quantity;
    }
    
    public abstract String getProductDetails();
    
}
