/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.repos;

import com.aop.aopcart.exception.DataStorageException;
import com.aop.aopcart.model.Customer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author squid
 */
public class CustomerRepo {
    private final Path filePath = Paths.get("data", "customers.txt");

    // Load all customer records into a Map (Key: customerId, Value: Customer)
    public Map<String, Customer> loadCustomers() {
        Map<String, Customer> customers = new HashMap<>();

        if (!Files.exists(filePath)) {
            return customers; // Returns empty map if file doesn't exist yet
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    Customer customer = Customer.deserialize(line);
                    customers.put(customer.getCustomerId(), customer);
                } catch (Exception e) {
                    System.err.println("[Warning] Skipping malformed customer record: " + line);
                }
            }
        } catch (IOException e) {
            throw new DataStorageException("Failed to load customer data.", e);
        }

        return customers;
    }

    // Save all customers using atomic replacement to prevent data loss
    public void saveCustomers(Map<String, Customer> customers) {
        try {
            // Ensure parent directory (e.g., "data/") exists
            Path parent = filePath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            Path tempFile = filePath.resolveSibling("customers.tmp");

            try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                for (Customer c : customers.values()) {
                    writer.write(c.serialize());
                    writer.newLine();
                }
            }

            // Atomic move replacement
            try {
                Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new DataStorageException("Failed to update customer data file.", e);
        }
    }
    
}
