/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.model;

import com.aop.aopcart.enums.CustomerType;
import java.util.Objects;

/**
 *
 * @author squid
 */
public class Customer {
    private final String customerId;
    private String username;
    private String email;
    private String passwordHash; // Stores securely hashed password, never plaintext
    private CustomerType customerType;

    public Customer(String customerId, String username, String email, String passwordHash, CustomerType customerType) {
        this.customerId = customerId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.customerType = customerType;
    }
    
    public Customer(String customerId, String username, String email, String passwordHash) {
        this(customerId, username, email, passwordHash, CustomerType.STANDARD);
    }
    
    public String getCustomerId() {
        return customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
    
    public String serialize() {
        return String.join("|", 
            customerId, 
            username, 
            email, 
            passwordHash, 
            customerType.name()
        );
    }
    
    public static Customer deserialize(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid customer record format: " + line);
        }
        return new Customer(
            parts[0], 
            parts[1], 
            parts[2], 
            parts[3], 
            CustomerType.valueOf(parts[4].toUpperCase())
        );
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return String.format("Customer{id='%s', username='%s', type=%s}", customerId, username, customerType);
    }
}
