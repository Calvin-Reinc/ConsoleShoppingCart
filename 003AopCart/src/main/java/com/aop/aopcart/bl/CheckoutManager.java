/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.bl;

import com.aop.aopcart.enums.PaymentType;
import com.aop.aopcart.model.CartItem;
import com.aop.aopcart.model.OrderRecord;
import com.aop.aopcart.payment.PaymentManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author squid
 */
public class CheckoutManager {
    private List<OrderRecord> sessionOrders = new ArrayList<>();

    public OrderRecord processCheckout(ShoppingCart cart, PaymentManager paymentManager, PaymentType paymentType) {
        double subtotal = cart.calculateSubtotal();
        double discount = cart.calculateDiscount(subtotal);
        double finalTotal = subtotal - discount;

        // Execute polymorphism via paymentStrategy interface
        boolean paymentSuccess = paymentManager.processPayment(finalTotal);

        if (!paymentSuccess) {
            throw new RuntimeException("Payment processing failed.");
        }

        // Reduce stock quantities across purchased products
        for (CartItem item : cart.getItems()) {
            item.getProduct().reduceStock(item.getQuantity());
        }

        // Generate unique order ID
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        // Create and store order record
        OrderRecord record = new OrderRecord(orderId, cart.getItems(), subtotal, discount, finalTotal, paymentType);
        sessionOrders.add(record);

        // Clear active cart after successful purchase
        cart.clearCart();

        return record;
    }

    public List<OrderRecord> getSessionOrders() {
        return sessionOrders;
    }
}
