/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oopcart.model;

/**
 *
 * @author squid
 */
public class Product {
    
    private String name;
    private int productID;
    private String desc;
    private Double price;
    private Boolean available;
    private int quantity;

    public Product() {
    }

    public Product(String name, int productID, String desc, Double price, int quantity) {
        this.name = name;
        this.productID = productID;
        this.desc = desc;
        this.price = price;
        this.available = true;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getAvailable() {

        if (quantity <= 0) {
            available = false;
        }

        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
        
        
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
    
    
    @Override
    public String toString() {
        String status = "Out of stock";
        if (quantity>0) {status = "In Stock";}
        return "ID: " + productID + " | " + name + " - R" + price + " (" + desc + ")" + " | "+status;
    }
    
}
