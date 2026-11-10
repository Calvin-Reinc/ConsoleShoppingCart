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
public class Shirt extends Product{
    private String size; // e.g., "S", "M", "L", "XL"
    private String fabric;

    public Shirt(String productId, String name, double price, int stockQuantity, Category collectionCategory,String size, String fabric) {
        super(productId, name, price, stockQuantity, collectionCategory);
        this.size = size;
        this.fabric = fabric;
    }

    public String getSize() {
        return size;
    }

    public String getFabric() {
        return fabric;
    }

    
    @Override
    public String getProductDetails() {
         return String.format("Shirt: %s | %s | Size: %s | Fabric: %s | Price: $%.2f | Stock: %d",
                getName(), getCollectionCategory().getDisplayName(), size, fabric, getPrice(), getStockQuantity());
    }
    
    
    
    
}
