/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.bl;

import com.aop.aopcart.enums.PaymentType;
import com.aop.aopcart.model.CartItem;
import com.aop.aopcart.model.CartItemSummary;
import com.aop.aopcart.model.Customer;
import com.aop.aopcart.model.OrderRecord;
import com.aop.aopcart.model.Product;
import com.aop.aopcart.payment.PaymentManager;
import com.aop.aopcart.repos.OrderRepo;
import com.aop.aopcart.repos.ProductRepo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author squid
 */
public class CheckoutManager {
    private List<OrderRecord> sessionOrders = new ArrayList<>();
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final CustomerManager customerManager;
    private final ProductManager productManager;
    
    public CheckoutManager(OrderRepo orderRepository, ProductRepo productRepository, CustomerManager customerManager, ProductManager productManager) {
        this.orderRepo = orderRepository;
        this.productRepo = productRepository;
        // Load existing orders from disk on startup
        this.sessionOrders = orderRepository.loadAllOrders();
        this.customerManager = customerManager;
        this.productManager = productManager;
    }
    

    public OrderRecord processCheckout(ShoppingCart cart, PaymentType paymentType) {
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty shopping cart.");
        }

        // Ensure a customer is logged in during checkout
        Customer currentCustomer = customerManager.getCurrentLoggedInCustomer()
                .orElseThrow(() -> new IllegalStateException("You must be logged in to complete a checkout."));

        double subtotal = cart.calculateSubtotal();
        // (Optional: apply discount strategies here if implemented)
        double finalTotal = subtotal;

        // Reduce stock and prune zero-stock items
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.reduceStock(item.getQuantity());

            if (product.getStockQuantity() <= 0) {
                productManager.removeProduct(product.getProductId());
            }
        }
        
        productRepo.saveProducts(productManager.getCatalogue());

        // Create OrderRecord tied to the customer
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        
        // Convert cart items to summaries for permanent historical storage
        List<CartItemSummary> itemSummaries = cart.getItems().stream()
                .map(item -> new CartItemSummary(
                    item.getProduct().getProductId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getProduct().getPrice()
                ))
                .collect(Collectors.toList());
   
        OrderRecord orderRecord = new OrderRecord(
                orderId,
                currentCustomer.getCustomerId(),
                itemSummaries, 
                finalTotal,
                paymentType,
                LocalDateTime.now()
        );

        // Persist the order history
        orderRepo.saveOrder(orderRecord);

        // Clear the cart after successful checkout
        cart.clearCart();
        System.out.println("[Success] Checkout complete! Order ID: " + orderId);
        
        return orderRecord;
    }

    public List<OrderRecord> getSessionOrders() {
        return sessionOrders;
    }
}
