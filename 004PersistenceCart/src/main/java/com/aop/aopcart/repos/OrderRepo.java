/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.repos;

import com.aop.aopcart.exception.DataStorageException;
import com.aop.aopcart.model.OrderRecord;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author squid
 */
public class OrderRepo {
    private final Path filePath;

    public OrderRepo(String dataDir) {
        try {
            Path dir = Paths.get(dataDir);
            Files.createDirectories(dir);
            this.filePath = dir.resolve("orders.dat"); // Binary serialization for immutable order records
        } catch (IOException e) {
            throw new DataStorageException("Failed to initialize orders storage.", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<OrderRecord> loadOrders() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (List<OrderRecord>) ois.readObject();
        } catch (Exception e) {
            System.err.println("[Warning] Could not load previous orders. Starting fresh history.");
            return new ArrayList<>();
        }
    }

    public void saveOrders(List<OrderRecord> orders) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            throw new DataStorageException("Failed to save order history.", e);
        }
    }
}
