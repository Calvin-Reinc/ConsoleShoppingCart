/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.bl;

import com.aop.aopcart.enums.PaymentType;
import com.aop.aopcart.model.CartItem;
import com.aop.aopcart.model.OrderRecord;
import com.aop.aopcart.model.Product;
import com.aop.aopcart.payment.PaymentManager;
import com.aop.aopcart.repos.OrderRepo;
import com.aop.aopcart.repos.ProductRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author squid
 */
public class CheckoutManager {
    private List<OrderRecord> sessionOrders = new ArrayList<>();
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    
    public CheckoutManager(OrderRepo orderRepository, ProductRepo productRepository) {
        this.orderRepo = orderRepository;
        this.productRepo = productRepository;
        // Load existing orders from disk on startup
        this.sessionOrders = orderRepository.loadOrders();
    }

    public OrderRecord processCheckout(ShoppingCart cart, PaymentManager paymentManager, PaymentType paymentType, ProductManager productManager) {
        double subtotal = cart.calculateSubtotal();
        double discount = cart.calculateDiscount(subtotal);
        double finalTotal = subtotal - discount;

        // Execute polymorphism via paymentStrategy interface
        boolean paymentSuccess = paymentManager.processPayment(finalTotal);

        if (!paymentSuccess) {
            throw new RuntimeException("Payment processing failed.");
        }

        // Reduce stock quantities and remove items if stock hits zero
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.reduceStock(item.getQuantity());

            if (product.getStockQuantity() <= 0) {
                productManager.removeProduct(product.getProductId());               
            }
        }
        
        productRepo.saveProducts(productManager.getCatalogue());

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
