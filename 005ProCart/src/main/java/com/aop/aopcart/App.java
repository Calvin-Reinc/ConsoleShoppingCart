/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.aop.aopcart;

import com.aop.aopcart.bl.CheckoutManager;
import com.aop.aopcart.bl.ProductManager;
import com.aop.aopcart.bl.ShoppingCart;
import com.aop.aopcart.enums.Category;
import com.aop.aopcart.enums.PaymentType;
import com.aop.aopcart.exception.OutOfStockException;
import com.aop.aopcart.model.ArtPiece;
import com.aop.aopcart.model.CartItem;
import com.aop.aopcart.model.OrderRecord;
import com.aop.aopcart.model.Product;
import com.aop.aopcart.model.Shirt;
import com.aop.aopcart.payment.CreditCardPayment;
import com.aop.aopcart.payment.PayPalPayment;
import com.aop.aopcart.payment.PaymentManager;
import com.aop.aopcart.repos.OrderRepo;
import com.aop.aopcart.repos.ProductRepo;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author squid
 */
public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        ProductRepo productrepo = new ProductRepo("data");
        OrderRepo orderRepo = new OrderRepo("data");
        
        ProductManager catalogue = new ProductManager(productrepo);
        ShoppingCart cart = new ShoppingCart();
        CheckoutManager checkoutService = new CheckoutManager(orderRepo, productrepo);
        
        List<OrderRecord> orderHistory = orderRepo.loadOrders();
        boolean running = true;

        System.out.println("=== Welcome to the Smart Art & Apparel Store (Iteration 3) ===");

        
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> displayCatalogue(catalogue.getCatalogue().values());
                    case "2" -> filterByCategoryPrompt(catalogue, scanner);
                    case "3" -> searchByNamePrompt(catalogue, scanner);
                    case "4" -> sortCataloguePrompt(catalogue, scanner);
                    case "5" -> addProductToCartPrompt(catalogue, cart, scanner);
                    case "6" -> viewCartAndTotals(cart);
                    case "7" -> processCheckoutPrompt(cart, checkoutService, scanner, catalogue);
                    case "8" -> viewOrderHistory(checkoutService);
                    case "9" -> registerProduct(catalogue, scanner);
                    case "10" -> updateStock(catalogue, scanner);
                    case "11" -> removeProduct(catalogue, scanner);
                    case "12" -> {
                        running = false;
                        System.out.println("Thank you for shopping with us!");
                    }
                    default -> System.out.println("Invalid option. Please choose between 1 and 8.");
                }
            } catch (OutOfStockException e) {
                System.out.println("\n[ERROR] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n[ERROR] An unexpected error occurred: " + e.getMessage());
            }
            System.out.println();
        }
        scanner.close();
    }
    
    
    private static void seedCatalogue(ProductManager catalogue) {
        catalogue.addProduct(new Shirt("S001", "Urban Graphic Tee", 35.00, 10, Category.CARELBLACK, "L", "Cotton"));
        catalogue.addProduct(new Shirt("S002", "Minimalist Logo Hoodie", 65.00, 3, Category.MOSIBIBLACK, "XL", "Fleece"));
        catalogue.addProduct(new ArtPiece("A001", "Neon Dreams Print", 120.00, 2, Category.MOSIBIBLACK, false, "24x36 Canvas"));
        catalogue.addProduct(new ArtPiece("A002", "Cyberpunk Wallpaper Pack", 15.00, 100, Category.DISGUISED, true, "4K PNG Bundle"));
    }   
    
    private static void printMenu() {
        System.out.println("\n\t=== MAIN MENU ===");
        System.out.println("1. View Full Catalogue");
        System.out.println("2. Filter Products by Category");
        System.out.println("3. Search Products by name");
        System.out.println("4. Sort Products by Price");
        System.out.println("5. Add Product to Shopping Cart");
        System.out.println("6. View Cart & Totals (with Discounts)");
        System.out.println("7. Checkout & Pay");
        System.out.println("8. View Session Purchase History");
        System.out.println("9. Register New Product");
        System.out.println("10. Update Product Stock");
        System.out.println("11. Remove Product from Catalogue");
        System.out.println("12. Exit");
        System.out.print("Select an option: ");
    }
    
    private static void displayCatalogue(Collection<Product> products) {
        
        if (!products.isEmpty()) {
            System.out.println("\n\t=== PRODUCT CATALOGUE ===");
            for (Product p : products) {
                System.out.println("[" + p.getProductId() + "] " + p.getProductDetails());
            }
        } else {System.out.println("\n\t=== PRODUCT CATALOGUE EMPTY ===");}
    }
    
    private static void filterByCategoryPrompt(ProductManager catalogue, Scanner scanner) {
        System.out.println("\nAvailable Categories:");
        Category[] cats = Category.values();
        for (int i = 0; i < cats.length; i++) {
            System.out.println((i + 1) + ". " + cats[i].getDisplayName());
        }
        System.out.print("Select category number: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (index >= 0 && index < cats.length) {
            List<Product> filtered = catalogue.filterByCategory(cats[index]);
            displayCatalogue(filtered);
        } else {
            System.out.println("Invalid category selection.");
        }
    }
    
    private static void searchByNamePrompt(ProductManager catalogue, Scanner scanner) {
        System.out.print("Enter product name keyword to search: ");
        String keyword = scanner.nextLine().trim();

        List<Product> results = catalogue.searchByName(keyword);
        if (results.isEmpty()) {
            System.out.println("No products found matching '" + keyword + "'.");
        } else {
            displayCatalogue(results);
        }
    }
       
    private static void sortCataloguePrompt(ProductManager catalogue, Scanner scanner) {
        System.out.print("Sort ascending? (true/false): ");
        boolean asc = Boolean.parseBoolean(scanner.nextLine().toLowerCase());
        List<Product> sorted = catalogue.sortByPrice(asc);
        displayCatalogue(sorted);
    }
    
    private static void addProductToCartPrompt(ProductManager catalogue, ShoppingCart cart, Scanner scanner) {
        displayCatalogue(catalogue.getCatalogue().values());
        System.out.print("Enter Product ID to add: ");
        String id = scanner.nextLine().trim();
        Product product = catalogue.getProduct(id);

        if (product == null) {
            System.out.println("Product ID not found.");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());

        cart.addProduct(product, qty);
        System.out.println("Successfully added " + qty + " of " + product.getName() + " to your cart.");
    }
    
    private static void viewCartAndTotals(ShoppingCart cart) {
        System.out.println("\n\t=== YOUR SHOPPING CART ===");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        for (CartItem item : cart.getItems()) {
            System.out.printf("- %s x%d @ $%.2f each = $%.2f%n",
                    item.getProduct().getName(), item.getQuantity(), item.getProduct().getPrice(), item.getTotalPrice());
        }

        double subtotal = cart.calculateSubtotal();
        double discount = cart.calculateDiscount(subtotal);
        double finalTotal = subtotal - discount;

        System.out.println("=========================================");
        System.out.printf("Subtotal:       $%.2f%n", subtotal);
        System.out.printf("Discount Applied: -$%.2f%n", discount);
        System.out.printf("Final Total:    $%.2f%n", finalTotal);
    }
    
    private static void processCheckoutPrompt(ShoppingCart cart, CheckoutManager checkoutService, Scanner scanner, ProductManager pm) {
        if (cart.isEmpty()) {
            System.out.println("Cannot checkout. Your cart is empty.");
            return;
        }

        viewCartAndTotals(cart);
        System.out.println("\nSelect Payment Type:");
        System.out.println("1. Credit Card");
        System.out.println("2. PayPal");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        PaymentManager manager;
        PaymentType type;

        if (choice.equals("1")) {
            System.out.print("Enter Credit Card Number: ");
            String ccNum = scanner.nextLine();
            System.out.print("Enter Cardholder Name: ");
            String name = scanner.nextLine();
            manager = new CreditCardPayment(ccNum, name);
            type = PaymentType.CREDIT_CARD;
        } else {
            System.out.print("Enter PayPal Email: ");
            String email = scanner.nextLine();
            manager = new PayPalPayment(email);
            type = PaymentType.PAYPAL;
        }

        OrderRecord record = checkoutService.processCheckout(cart, manager, type, pm);
        System.out.println("\nCheckout Successful!");
        System.out.println(record.getOrderReceipt());
    }
    
    private static void viewOrderHistory(CheckoutManager checkoutService) {
        System.out.println("\n--- SESSION PURCHASE HISTORY ---");
        List<OrderRecord> orders = checkoutService.getSessionOrders();
        
        if (orders.isEmpty()) {
            System.out.println("No completed purchases during this session.");
            return;
        }

        for (OrderRecord order : orders) {
            System.out.println(order.getOrderReceipt());
        }
    }
    
    private static void registerProduct(ProductManager cm, Scanner scanner) {
        System.out.print("Enter type (1 for Shirt, 2 for Art Piece): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter Stock Quantity: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("CarelBlack, MosibiBlack, Disguised): ");
        Category category = Category.valueOf(scanner.nextLine().trim().toUpperCase());

        if ("1".equals(type)) {
            System.out.print("Enter Size (e.g., S, M, L, XL): ");
            String size = scanner.nextLine().trim();
            System.out.print("Enter Fabric Type: ");
            String fabric = scanner.nextLine().trim();
            cm.registerProduct(new Shirt(id, name, price, stock, category, size, fabric));
        } else {
            System.out.print("Is it digital? (true/false): ");
            boolean isDigital = Boolean.parseBoolean(scanner.nextLine().trim());
            System.out.print("Enter Dimensions: ");
            String dims = scanner.nextLine().trim();
            cm.registerProduct(new ArtPiece(id, name, price, stock, category, isDigital, dims));
        }
        System.out.println("[Success] Product registered and saved to disk.");
    }

    private static void updateStock(ProductManager pm, Scanner scanner) {
        System.out.print("Enter Product ID to update: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter new stock quantity: ");
        int newStock = Integer.parseInt(scanner.nextLine().trim());
        pm.updateProductStock(id, newStock);
        System.out.println("[Success] Stock level updated and persisted.");
    }

    private static void removeProduct(ProductManager pm, Scanner scanner) {
        if (pm.getCatalogue().isEmpty()) {
            System.out.println("[Info] The catalogue is currently empty. No products to remove.");
            return;
        }
        
        System.out.print("Enter Product ID to remove: ");
        String id = scanner.nextLine().trim();
        
        pm.removeProduct(id);
        System.out.println("[Success] Product removed from catalogue.");
    }
    
    
    
}
