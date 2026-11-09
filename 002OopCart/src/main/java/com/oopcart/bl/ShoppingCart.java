/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.oopcart.bl;

import com.oopcart.model.CartItem;
import com.oopcart.model.Product;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author squid
 */
public class ShoppingCart {
    
    private ArrayList<CartItem> cartItems;
    private double cartTotal;

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
        if (existingItem != null) {
            if ( product.getQuantity() > 0) {
            // If it exists, update the quantity
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                product.setQuantity(product.getQuantity()-1);
                return "Product ID "+existingItem.getProduct().getProductID()+" updated";
            } else {
                return "Error. Product out of stock.";
            }
                
        } else {
            if ( product.getQuantity() > 0) {
                // If it doesn't exist, create a new CartItem and add it to the list with 1 quantity
                CartItem newItem = new CartItem(product, 1);
                product.setQuantity(product.getQuantity()-1);
                cartItems.add(newItem);
            }
        }
        return "Product ID "+product.getProductID()+" added to cart";
    } 
    
    
    //this is not the inCart quantity but one by the user
    public String addToCart(Product product, int quantity) {
        
        // Check if the product is already in the cart
        CartItem existingItem = findCartItem(product);
        
        //if the product is still available then..
        if (existingItem != null) {
            // If it exists, update the quantity
            if (quantity <= product.getQuantity()) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                product.setQuantity(product.getQuantity()-quantity);
                return "Product ID "+existingItem.getProduct().getProductID()+" updated";
                
            } else { return "Error. Invalid quantity or product out of stock!";}
        } else {
            // If it doesn't exist, create a new CartItem and add it to the list
            if (quantity <= product.getQuantity()) {
                CartItem newItem = new CartItem(product, quantity);
                product.setQuantity(product.getQuantity()-quantity);
                cartItems.add(newItem);
                return "Product ID "+product.getProductID()+" added to cart";
            } else { return "Error. Invalid quantity or product out of stock!";}
        
        }  
        
        
    }
    
    
    public String removeFromCart(Product product) {
        
        CartItem toRemoveItem = findCartItem(product);
        
        if (toRemoveItem != null) {
            product.setQuantity(toRemoveItem.getQuantity());
            cartItems.remove(toRemoveItem);    
            return "Product removed from cart.";
        } else {
            return "Product not in cart.";
        }
  
    }
    
    
    public String updateCart(Product product, int quantity) {
        CartItem toUpdateItem = findCartItem(product);
        
        if (toUpdateItem != null) {
            System.out.println(quantity+" - "+product.getQuantity());
            if (quantity <= toUpdateItem.getQuantity() && quantity+product.getQuantity()>0) {
                toUpdateItem.setQuantity(toUpdateItem.getQuantity()-quantity);
            } else {
                return "Quantity over stock remaining.";
            }
        } else {
            return "Product not in cart.";
        }
        
        return "Product quantity updated.";
    }
    
    
    public double getCartTotal() {
        cartTotal=0;
        for (CartItem item:cartItems) {
            cartTotal += (item.getQuantity() * item.getProduct().getPrice());
        }
    
        return cartTotal;
        
    }
    
    
    public void displayCart() {
        DecimalFormat df = new DecimalFormat("R#,##0.00");
        
        if (!cartItems.isEmpty()) {
            System.out.println("\n\t==== In Stock ====\t\n");
            for (CartItem item:cartItems) {
                System.out.println(item.toString());
            }
            System.out.println("=== TOTAL ===  :  "+df.format(getCartTotal()));
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
