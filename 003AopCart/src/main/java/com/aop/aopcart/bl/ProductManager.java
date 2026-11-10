/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.bl;

import com.aop.aopcart.enums.Category;
import com.aop.aopcart.model.Product;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author squid
 */
public class ProductManager {
    // HashMap for O(1) product lookup by ID
    private final Map<String, Product> productMap = new HashMap<>();
    
    // HashSet to keep track of unique active categories present in the store
    private final Set<Category> activeCategories = new HashSet<>();
    
    public Set<Category> getActiveCategories() {
        return activeCategories;
    }

    public void addProduct(Product product) {
        productMap.put(product.getProductId(), product);
        activeCategories.add(product.getCollectionCategory());
    }

    public Product getProduct(String productId) {
        return productMap.get(productId);
    }

    public Collection<Product> getAllProducts() {
        return productMap.values();
    }
    
    // 1. Search products by name (Requirement #2) using Stream API
    public List<Product> searchByName(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return productMap.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
    
    // 2. Filter products by category (Requirement #3) using Stream API
    public List<Product> filterByCategory(Category category) {
        return productMap.values().stream()
                .filter(p -> p.getCollectionCategory() == category)
                .collect(Collectors.toList());
    }
    
    
    // 3. Sort products by price (Requirement #4) using Stream API
    public List<Product> sortByPrice(boolean ascending) {
        Comparator<Product> comparator = Comparator.comparingDouble(Product::getPrice);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return productMap.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    
}
