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
public class ArtPiece extends Product {
    private boolean isDigital;
    private String formatOrDimension;

    public ArtPiece(String productId, String name, double price, int stockQuantity, Category collectionCategory,boolean isDigital, String formatOrDimension) {
        super(productId, name, price, stockQuantity, collectionCategory);
        this.isDigital = isDigital;
        this.formatOrDimension = formatOrDimension;
    }

    public boolean isDigital() {
        return isDigital;
    }

    public String getFormatOrDimension() {
        return formatOrDimension;
    }
    
   
    @Override
    public String getProductDetails() {
        String type = isDigital ? "Digital Art" : "Physical Print";
        return String.format("Art [%s]: %s | %s | Spec: %s | Price: $%.2f | Stock: %d",
                type, getName(), getCollectionCategory().getDisplayName(), formatOrDimension, getPrice(), getStockQuantity());
    }
    
}
