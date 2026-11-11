/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.repos;

import com.aop.aopcart.enums.Category;
import com.aop.aopcart.exception.DataStorageException;
import com.aop.aopcart.model.ArtPiece;
import com.aop.aopcart.model.Product;
import com.aop.aopcart.model.Shirt;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author squid
 */
public class ProductRepo {
    private final Path filePath = Paths.get("data", "products.txt");
    private final Path backupPath = Paths.get("data", "backup.txt");

    public ProductRepo() {
    }
    
    
    
    public Map<String, Product> loadProducts() {
        Map<String, Product> products = new HashMap<>();
        if (!Files.exists(filePath)) {
            return products; // Empty initial state if file doesn't exist
        }

        // Create backup before reading/loading
        createBackup();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                Product product = parseProductLine(line);
                if (product != null) {
                    products.put(product.getProductId(), product);
                }
            }
        } catch (IOException e) {
            throw new DataStorageException("Failed to read product catalogue from disk.", e);
        }
        return products;
    }

    public void saveProducts(Map<String, Product> products) {
        Path tempFile = filePath.resolveSibling("products.tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Product p : products.values()) {
                writer.write(serializeProduct(p));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataStorageException("Failed to write temporary product file.", e);
        }

        // Atomic replacement to prevent data loss on partial writes
        try {
            Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            try {
                Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                throw new DataStorageException("Failed to update product data file.", ex);
            }
        }
    }

    private void createBackup() {
        try {
            if (Files.exists(filePath)) {
                Files.copy(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ignored) {
            // Non-fatal backup failure
        }
    }

    private String serializeProduct(Product p) {
        if (p instanceof Shirt s) {
            return String.join("|", "SHIRT", s.getProductId(), s.getName(), 
                    String.valueOf(s.getPrice()), String.valueOf(s.getStockQuantity()), 
                    s.getCollectionCategory().name(), s.getSize(), s.getFabric());
        } else if (p instanceof ArtPiece a) {
            return String.join("|", "ART", a.getProductId(), a.getName(), 
                    String.valueOf(a.getPrice()), String.valueOf(a.getStockQuantity()), 
                    a.getCollectionCategory().name(), String.valueOf(a.isDigital()), a.getFormatOrDimension());
        }
        throw new IllegalArgumentException("Unknown product type: " + p.getClass().getName());
    }

    private Product parseProductLine(String line) {
        try {
            String[] parts = line.split("\\|");
            String type = parts[0];
            String id = parts[1];
            String name = parts[2];
            double price = Double.parseDouble(parts[3]);
            int stock = Integer.parseInt(parts[4]);
            Category category = Category.valueOf(parts[5]);

            if ("SHIRT".equals(type)) {
                return new Shirt(id, name, price, stock, category, parts[6], parts[7]);
            } else if ("ART".equals(type)) {
                return new ArtPiece(id, name, price, stock, category, Boolean.parseBoolean(parts[6]), parts[7]);
            }
        } catch (Exception e) {
            System.err.println("[Warning] Skipping corrupted product record: " + line);
        }
        return null;
    }
    
}
