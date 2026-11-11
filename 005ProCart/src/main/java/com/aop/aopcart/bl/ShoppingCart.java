/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.bl;

import com.aop.aopcart.exception.OutOfStockException;
import com.aop.aopcart.model.CartItem;
import com.aop.aopcart.model.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author squid
 */
public class ShoppingCart {
    // Maps customerId to their specific ShoppingCart
    private final Map<String, ShoppingCart> customerCarts = new HashMap<>();
    private List<CartItem> items = new ArrayList<>();
    
    public List<CartItem> getItems() {
        return items;
    }
    
    
    public void addProduct(Product product, int quantity) {
        if (product.getStockQuantity() < quantity) {
            throw new OutOfStockException(product.getName(), quantity, product.getStockQuantity());
        }
        
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            int newTotalQty = existingItem.get().getQuantity() + quantity;
            if (product.getStockQuantity() < newTotalQty) {
                throw new OutOfStockException(product.getName(), newTotalQty, product.getStockQuantity());
            }
            existingItem.get().setQuantity(newTotalQty);
        } else {
            items.add(new CartItem(product, quantity));
        }
    }

    public double calculateSubtotal() {
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
    
    public double calculateDiscount(double subtotal) {
        
        if (subtotal > 150.00) {
            return subtotal * 0.10;
        }
        return 0.0;
    }
    
    public void clearCart() {
        items.clear();
    }
    
    public void clearCart(String customerId) {
        customerCarts.remove(customerId);
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public ShoppingCart getCartForCustomer(String customerId) {
        // Compute if absent: creates a new cart automatically if the customer doesn't have one yet
        return customerCarts.computeIfAbsent(customerId, id -> new ShoppingCart());
    }
    
    
    
}

