/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.bl;

import com.aop.aopcart.enums.CustomerType;
import com.aop.aopcart.exception.AuthenticationException;
import com.aop.aopcart.model.Customer;
import com.aop.aopcart.repos.CustomerRepo;
import com.aop.aopcart.util.PasswordUtil;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author squid
 */
public class CustomerManager {
    private final CustomerRepo repository;
    private final Map<String, Customer> customers;
    private Customer currentLoggedInCustomer; // Tracks active session state

    public CustomerManager(CustomerRepo repository) {
        this.repository = repository;
        this.customers = repository.loadCustomers();
        this.currentLoggedInCustomer = null;
    }
    
    // Register a new customer
    public Customer register(String username, String email, String rawPassword) {
        // Validate uniqueness
        boolean usernameExists = customers.values().stream()
                .anyMatch(c -> c.getUsername().equalsIgnoreCase(username));
        
        if (usernameExists) {
            throw new IllegalArgumentException("Username is already taken: " + username);
        }

        String customerId = "CUST-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String passwordHash = PasswordUtil.hashPassword(rawPassword);

        Customer newCustomer = new Customer(customerId, username, email, passwordHash, CustomerType.STANDARD);
        
        customers.put(customerId, newCustomer);
        repository.saveCustomers(customers);

        System.out.println("[Success] Customer account created: " + username);
        return newCustomer;
    }
    
    // Authenticate and log in a customer
    public Customer login(String username, String rawPassword) {
        Optional<Customer> foundCustomer = customers.values().stream()
                .filter(c -> c.getUsername().equalsIgnoreCase(username))
                .findFirst();

        if (foundCustomer.isEmpty() || !PasswordUtil.verifyPassword(rawPassword, foundCustomer.get().getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password.");
        }

        this.currentLoggedInCustomer = foundCustomer.get();
        System.out.println("[Success] Welcome back, " + currentLoggedInCustomer.getUsername() + "!");
        return currentLoggedInCustomer;
    }
    
    // Log out current user
    public void logout() {
        if (this.currentLoggedInCustomer != null) {
            System.out.println("[Info] Logged out user: " + this.currentLoggedInCustomer.getUsername());
            this.currentLoggedInCustomer = null;
        } else {
            System.out.println("[Info] No user is currently logged in.");
        }
    }
    
    // Get current session user using Optional
    public Optional<Customer> getCurrentLoggedInCustomer() {
        return Optional.ofNullable(currentLoggedInCustomer);
    }

    public boolean isUserLoggedIn() {
        return currentLoggedInCustomer != null;
    }
    
    
    
    
}
