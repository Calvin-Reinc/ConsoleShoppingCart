/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.repos;

import com.aop.aopcart.exception.DataStorageException;
import com.aop.aopcart.model.OrderRecord;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author squid
 */
public class OrderRepo {
    private final Path filePath = Paths.get("data", "orders.txt");

    public OrderRepo() {
    }

    public List<OrderRecord> loadAllOrders() {
        List<OrderRecord> orders = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return orders;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    OrderRecord order = OrderRecord.deserialize(line);
                    orders.add(order);
                } catch (Exception e) {
                    System.err.println("[Warning] Skipping malformed order record: " + line);
                }
            }
        } catch (IOException e) {
            throw new DataStorageException("Failed to load order history.", e);
        }
        return orders;
    }
    
    // Filter orders specifically for the logged-in customer using Stream API
    public List<OrderRecord> getOrdersForCustomer(String customerId) {
        return loadAllOrders().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public void saveOrder(OrderRecord newOrder) {
        List<OrderRecord> orders = loadAllOrders();
        orders.add(newOrder);

        try {
            Path parent = filePath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            Path tempFile = filePath.resolveSibling("orders.tmp");

            try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                for (OrderRecord order : orders) {
                    writer.write(order.serialize());
                    writer.newLine();
                }
            }

            try {
                Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new DataStorageException("Failed to save order history.", e);
        }
    }
}
