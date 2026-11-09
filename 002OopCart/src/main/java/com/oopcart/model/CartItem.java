/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oopcart.model;

/**
 *
 * @author squid
 */
public class CartItem {
    
    
    private Product product;
    private int quantity;

    public CartItem() {
    }
    
    
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ID: " + product.getProductID() + " | " + product.getName() +
                " - R" + product.getPrice() + " (" + product.getDesc() + ")" +
                " | Quantity: "+quantity;
    }
    
    
    
    
}
