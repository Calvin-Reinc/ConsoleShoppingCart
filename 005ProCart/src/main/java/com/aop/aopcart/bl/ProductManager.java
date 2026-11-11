/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aop.aopcart.bl;

import com.aop.aopcart.enums.Category;
import com.aop.aopcart.exception.ProductNotFoundException;
import com.aop.aopcart.model.Product;
import com.aop.aopcart.repos.ProductRepo;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author squid
 */
public class ProductManager {
    private final Map<String, Product> catalogue;
    private final Set<Category> categories;
    private final ProductRepo repository;
    // HashMap for O(1) product lookup by ID
    private final Map<String, Product> productMap = new HashMap<>();
    
    // HashSet to keep track of unique active categories present in the store
    private final Set<Category> activeCategories = new HashSet<>();
    
    public ProductManager(ProductRepo repository) {
        this.repository = repository;
        // Load persisted products on startup; falls back to an empty HashMap if file doesn't exist
        this.catalogue = repository.loadProducts();
        
        // Populate category set based on loaded products
        this.categories = catalogue.values().stream()
                .map(Product::getCollectionCategory)
                .collect(Collectors.toCollection(HashSet::new));
    }
    
    public Set<Category> getActiveCategories() {
        return activeCategories;
    }

    public void addProduct(Product product) {
        productMap.put(product.getProductId(), product);
        activeCategories.add(product.getCollectionCategory());
    }

    public Product getProduct(String productId) {
        return catalogue.get(productId);
    }

    public Collection<Product> getAllProducts() {
        return productMap.values();
    }
    
    // 1. Search products by name (Requirement #2) using Stream API
    public List<Product> searchByName(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return catalogue.values().stream()
                .filter(p -> p.getName().toLowerCase().equalsIgnoreCase(lowerKeyword))
                .collect(Collectors.toList());
    }
    
    // 2. Filter products by category (Requirement #3) using Stream API
    public List<Product> filterByCategory(Category category) {
        return catalogue.values().stream()
                .filter(p -> p.getCollectionCategory() == category)
                .collect(Collectors.toList());
    }
    
    
    // 3. Sort products by price (Requirement #4) using Stream API
    public List<Product> sortByPrice(boolean ascending) {
        Comparator<Product> comparator = Comparator.comparingDouble(Product::getPrice);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return catalogue.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    public void registerProduct(Product product) {
        if (catalogue.containsKey(product.getProductId())) {
            throw new IllegalArgumentException("Product ID already exists: " + product.getProductId());
        }
        catalogue.put(product.getProductId(), product);
        categories.add(product.getCollectionCategory());
        repository.saveProducts(catalogue);
    }

    public void updateProductStock(String productId, int newStock) {
        Product p = catalogue.get(productId);
        if (p == null) throw new ProductNotFoundException("Product not found: " + productId);
        // Use package-internal or setter method to update stock
        if (newStock <= 0) {
            // Automatically prune if stock drops to zero or below
            removeProduct(productId);
        } else {
            // 1. Update the stock value on the product object itself
            p.setStockQuantity(newStock); // Ensure this matches your Product class setter name

            // 2. Put back into map and trigger file persistence
            catalogue.put(productId, p);
            repository.saveProducts(catalogue);
        }
    }

    public void removeProduct(String productId) {
        if (catalogue.remove(productId) != null) {
            repository.saveProducts(catalogue);
        }
    }
    
    public Map<String, Product> getCatalogue() {
        return Collections.unmodifiableMap(catalogue);
    }

    public Set<Category> getCategories() {
        return Collections.unmodifiableSet(categories);
    }
    
}
