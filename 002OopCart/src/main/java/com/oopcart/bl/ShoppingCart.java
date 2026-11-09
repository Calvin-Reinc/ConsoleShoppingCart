/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oopcart.bl;

import com.oopcart.model.CartItem;
import com.oopcart.model.Product;
import java.util.ArrayList;

/**
 *
 * @author squid
 */
public class ShoppingCart {
    
    private ArrayList<CartItem> cartItems;
    private int cartTotal;

    public ShoppingCart() {
        this.cartItems = new ArrayList<>();
        this.cartTotal = cartTotal;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    
    
    public String addToCart(Product product) {
        // Check if the product is already in the cart
        CartItem existingItem = findCartItem(product);
        
        //if the product is still available then..
        if (product.getAvailable()) {
            if (existingItem != null) {
                if (existingItem.getQuantity() + 1 > 0 && product.getAvailable()) {
                    // If it exists, update the quantity
                    existingItem.setQuantity(existingItem.getQuantity() + 1);
                    return "Product ID "+existingItem.getProduct().getProductID()+" updated";
                } else { return "Error. Invalid quantity";}
                
            } else {
                // If it doesn't exist, create a new CartItem and add it to the list with 1 quantity
                CartItem newItem = new CartItem(product, 1);
                cartItems.add(newItem);
            }
            return "Product ID "+product.getProductID()+" added to cart";
        } else {
            return "Product not available.";
        }
    }
    
    
    //this is not the inCart quantity but one by the user
    public String addToCart(Product product, int quantity) {
        
        // Check if the product is already in the cart
        CartItem existingItem = findCartItem(product);
        
        //if the product is still available then..
        if (existingItem != null) {
            // If it exists, update the quantity
            if (existingItem.getQuantity() + quantity > 0 && product.getAvailable()) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                return "Product ID "+existingItem.getProduct().getProductID()+" updated";
            } else { return "Error. Invalid quantity";}
        } else {
            // If it doesn't exist, create a new CartItem and add it to the list
            CartItem newItem = new CartItem(product, quantity);
            cartItems.add(newItem);
            return "Product ID "+product.getProductID()+" added to cart";
        }  
        
        
    }
    
    
    public String removeFromCart(Product product) {
        
        CartItem toRemoveItem = findCartItem(product);
        
        if (toRemoveItem != null) {
            cartItems.remove(toRemoveItem);
            
        } else {
            return "Product not in cart.";
        }
        
        
        return "Product removed from cart.";
    }
    
    
    public String updateCart(Product product, int quantity) {
        CartItem toUpdateItem = findCartItem(product);
        
        if (toUpdateItem != null) {
            if (toUpdateItem.getQuantity() + quantity > 0) {
                toUpdateItem.setQuantity(toUpdateItem.getQuantity()+quantity);
            } else {
                return "Quantity cannot be below 0.";
            }
        } else {
            return "Product not in cart.";
        }
        
        return "Product quantity updated.";
    }
    
    
    public int getCartTotal() {
        for (CartItem item:cartItems) {
            cartTotal += (item.getQuantity() + item.getProduct().getPrice());
        }
    
        return cartTotal;
        
    }
    
    
    public void displayCart() {
        if (!cartItems.isEmpty()) {
            for (CartItem item:cartItems) {
                System.out.println(item.getProduct().toString()+" | Quantity: "+item.getQuantity());
            }
        } else {System.out.println("Cart empty.");}
        
    
    }
    
    
    // Helper method to locate an existing cart item by product
    private CartItem findCartItem(Product product) {
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                // Assuming Product has a unique ID or equals() method
                if (item.getProduct().getProductID() == product.getProductID()) {
                    return item;
                }
            }
        }
        return null;
    }
    
    
    
}
