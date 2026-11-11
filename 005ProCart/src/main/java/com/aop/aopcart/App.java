/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.aop.aopcart;

import com.aop.aopcart.bl.*;
import com.aop.aopcart.enums.Category;
import com.aop.aopcart.enums.PaymentType;
import com.aop.aopcart.exception.OutOfStockException;
import com.aop.aopcart.model.*;
import com.aop.aopcart.payment.CreditCardPayment;
import com.aop.aopcart.payment.PayPalPayment;
import com.aop.aopcart.payment.PaymentManager;
import com.aop.aopcart.repos.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 *
 * @author squid
 */
public class App {

    public static void main(String[] args) {
        // 1. Initialize Repositories
        OrderRepo orderRepo = new OrderRepo();
        ProductRepo productRepo = new ProductRepo();   
        CustomerRepo customerRepo= new CustomerRepo();
        

        // 2. Initialize Services
        ProductManager catalogueManager = new ProductManager(productRepo);
        CustomerManager customerManager = new CustomerManager(customerRepo);
        ShoppingCart cartManager = new ShoppingCart();
        CheckoutManager checkoutService = new CheckoutManager(orderRepo, productRepo, customerManager,catalogueManager);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================");
        System.out.println("  Welcome to the Enterprise Store (Iter 5)");
        System.out.println("=========================================");
        
        List<OrderRecord> orderHistory = orderRepo.loadAllOrders();
        
        
        while (running) {
            Optional<Customer> activeUser = customerManager.getCurrentLoggedInCustomer();

            if (activeUser.isEmpty()) {
                // --- GUEST / AUTHENTICATION MENU ---
                System.out.println("\n--- Authentication Menu ---");
                System.out.println("1. Log In");
                System.out.println("2. Register New Account");
                System.out.println("3. Browse Catalogue (Guest View)");
                System.out.println("0. Exit Application");
                System.out.print("Select an option: ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        handleLogin(scanner, customerManager);
                        break;
                    case "2":
                        handleRegistration(scanner, customerManager);
                        break;
                    case "3":
                        showCatalogue(catalogueManager);
                        break;
                    case "0":
                        running = false;
                        System.out.println("Exiting application. Goodbye!");
                        break;
                    default:
                        System.out.println("[Error] Invalid option. Please try again.");
                }
            } else {
                // --- LOGGED-IN USER MENU ---
                Customer user = activeUser.get();
                System.out.println("\n--- Main Menu [Logged in as: " + user.getUsername() + " (" + user.getCustomerType() + ")] ---");
                System.out.println("1. View Product Catalogue");
                System.out.println("2. View Shopping Cart");
                System.out.println("3. Checkout Cart");
                System.out.println("4. View My Order History");
                System.out.println("5. Log Out");
                System.out.println("0. Exit Application");
                System.out.print("Select an option: ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        handleShoppingMenu(scanner, catalogueManager, cartManager, user.getCustomerId());
                        break;
                    case "2":
                        viewCartAndTotals(cartManager.getCartForCustomer(user.getCustomerId()));
                        break;
                    case "3":
                        processCheckoutPrompt(cartManager.getCartForCustomer(user.getCustomerId()), checkoutService,scanner, catalogueManager);
                        break;
                    case "4":
                        viewOrderHistory(orderRepo, user.getCustomerId());
                        break;
                    case "5":
                        customerManager.logout();
                        break;
                    case "0":
                        running = false;
                        System.out.println("Exiting application. Goodbye!");
                        break;
                    default:
                        System.out.println("[Error] Invalid option. Please try again.");
                }
            }
        }
        scanner.close();
    }
    
    private static void handleLogin(Scanner scanner, CustomerManager customerManager) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        try {
            customerManager.login(username, password);
        } catch (Exception e) {
            System.out.println("[Login Failed] " + e.getMessage());
        }
    }

    private static void handleRegistration(Scanner scanner, CustomerManager customerManager) {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter email address: ");
        String email = scanner.nextLine().trim();
        System.out.print("Choose a password: ");
        String password = scanner.nextLine().trim();

        try {
            customerManager.register(username, email, password);
            System.out.println("[Success] You can now log in with your new credentials.");
        } catch (Exception e) {
            System.out.println("[Registration Failed] " + e.getMessage());
        }
    }

    private static void showCatalogue(ProductManager catalogueManager) {
        System.out.println("\n--- Current Product Catalogue ---");
        List<Product> products = (List<Product>) catalogueManager.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("The catalogue is currently empty.");
            return;
        }
        for (Product p : products) {
            System.out.printf("[%s] %s - $%.2f (Stock: %d)%n",
                    p.getProductId(), p.getName(), p.getPrice(), p.getStockQuantity());
        }
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
        

        PaymentManager manager;
        PaymentType type= PaymentType.CREDIT_CARD;;

        try {
            String choice = scanner.nextLine().trim();
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
        } catch (NumberFormatException e) {
            System.out.println("[Error] Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("[Checkout Failed] " + e.getMessage());
        }
        OrderRecord record = checkoutService.processCheckout(cart,type);
        System.out.println("\nCheckout Successful!");
        System.out.println(record.getOrderReceipt());
    }
    
    private static void viewOrderHistory(OrderRepo orderRepository, String customerId) {
        System.out.println("\n--- Your Past Order History ---");
        List<OrderRecord> orders = orderRepository.getOrdersForCustomer(customerId);
        if (orders.isEmpty()) {
            System.out.println("You have no past orders.");
            return;
        }

        for (OrderRecord order : orders) {
            System.out.printf("Order ID: %s | Date: %s | Total: $%.2f | Payment: %s%n",
                    order.getOrderId(), order.getOrderDate().toLocalDate(), order.getFinalTotal(), order.getPaymentType());
            for (CartItemSummary item : order.getItems()) {
                System.out.printf("   -> %s (x%d) @ $%.2f%n", item.getProductName(), item.getQuantity(), item.getPrice());
            }
            System.out.println("--------------------------------------------------");
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
    
    private static void handleShoppingMenu(Scanner scanner, ProductManager catalogueManager, ShoppingCart cartManager, String customerId) {
        showCatalogue(catalogueManager);
        System.out.print("\nEnter Product ID to add to cart (or press Enter to return): ");
        String productId = scanner.nextLine().trim();
        if (productId.isEmpty()) return;

        Optional<Product> productOpt = catalogueManager.findProductById(productId);
        if (productOpt.isEmpty()) {
            System.out.println("[Error] Product not found.");
            return;
        }

        System.out.print("Enter quantity: ");
        try {
            int qty = Integer.parseInt(scanner.nextLine().trim());
            ShoppingCart cart = cartManager.getCartForCustomer(customerId);
            cart.addProduct(productOpt.get(), qty);
            System.out.println("[Success] Added item to your shopping cart.");
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid quantity number.");
        } catch (Exception e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }
    
    
}
