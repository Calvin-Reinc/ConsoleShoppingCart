/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.oopcart;

import com.oopcart.bl.ShoppingCart;
import com.oopcart.model.CartItem;
import com.oopcart.model.Product;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author squid
 */
public class App {

    private ArrayList<Product> products;
    private ArrayList<CartItem> cartItems;
    private boolean status;
    private ShoppingCart cart;

    public App() {
        this.products = new ArrayList<>();
        this.status = true;
        this.cartItems = new ArrayList<>();
        this.cart = new ShoppingCart();
        loadProducts();
    }
    
    
    private void loadProducts() {
        products.add(new Product("Shirt01", 1, "High streetwear fashion", 399.99, 15));
        products.add(new Product("Shirt02", 2, "High streetwear fashion", 499.99, 15));
        products.add(new Product("Shirt03", 3, "High streetwear fashion", 599.99, 15));
        products.add(new Product("Shirt04", 4, "High streetwear fashion", 699.99, 15));
        products.add(new Product("Shirt05", 5, "High streetwear fashion", 799.99, 0));
        
    }
    
    
    public void displayProducts() {
        System.out.println("\n\t==== In Stock ====\t\n");
        for (Product product : products) {
            System.out.println(product.toString());
        }
        
    }
    
    
    public void displayMenu() {
        System.out.print("\nSelect an option below:\n"
                + "1. Add a product to cart(Using ID).\n"
                + "2. View shopping cart.\n"
                + "3. Remove cart item.\n"
                + "4. Update cart.\n"
                + "5. Exit."
                + "\nOption: ");
    
    }
    
    /*
    public void displayCart() {
        for(CartItem item:cart.getCartItems()) {
            System.out.println(item.getProduct().toString()+item.getProduct().getQuantity());
        }
        
        System.out.println(cart.getCartItems());
        
        /*if (cartItems == null) {
            System.out.println("something simethin");
        } else {
            for (CartItem item: cartItems) {
                System.out.print("Cart Empty.\n");
            }
        }
    
        System.out.println("something simethin222222222");*/
    
    
    public String mainAddToCart(int userID) {
        String msg="Invalid product ID.";

        
        for (Product item:products) {
            if(item.getProductID()==userID) {
                msg = cart.addToCart(item);
            }    
        }
        
        return msg;
    }
    
    
    public String mainAddToCart(int userID, int quantity) {
        
        String msg = "Invalid product ID.";
        
        for (Product item:products) {
            if(item.getProductID()==userID) {
                msg = cart.addToCart(item, quantity);
            }    
        }
        
        return msg;
    }
        
    
    public Product verifyID(int userID) {
        for (Product item:products) {
            if(item.getProductID()==userID) {
                return item;
            } 
        }
        
        return null;
    }
    
    
    public void run() {
        
        int userOption = -1, quantity=1,userID;
        Scanner key = new Scanner(System.in);
        Product product = new Product();
        
        System.out.println("OOP Shopping cart.");
        
        while (status) {
            displayMenu();

            try {
                userOption = key.nextInt();
                
                if ( userOption ==1 ) {
                    displayProducts();
                    System.out.print("=== Add To Cart ===\nEnter the ID of the product you want to add to cart: ");
                    userID = key.nextInt();
                    
                    System.out.print("\nEnter quantity, leave empty for 1: ");
                    quantity = key.nextInt();
                    
                    if (quantity == 1){
                        System.out.println(mainAddToCart(userID));
                    } else {
                        System.out.println(mainAddToCart(userID, quantity));
                    }
                    
                } else if (userOption ==2 ) {
                    cart.displayCart();
                } else if (userOption ==3 ) {
                    cart.displayCart();
                    if (cartItems.isEmpty()) {continue;}
                    System.out.print("=== Remove From Cart ===\nEnter the ID of the product you want to remove from cart: ");
                    userID = key.nextInt();
                    
                    if (verifyID(userID)!=null) {
                        cart.removeFromCart(verifyID(userID));
                        System.out.println(verifyID(userID).toString()+" === REMOVED!");
                    } else {System.out.println("Product ID not found.");}
                } else if (userOption ==4 ) {
                    cart.displayCart();
                    System.out.print("=== Update Cart ===\nEnter the ID of the product you want to update: ");
                    userID = key.nextInt();            
                    System.out.print("\nEnter quantity: ");
                    quantity = key.nextInt();
                    if (verifyID(userID)!=null) {
                        cart.updateCart(verifyID(userID), quantity);
                    } else {System.out.println("Product ID not found.");}
                } else if (userOption ==5 ) {
                    System.out.println("Clearing cart, Gooodbye.");
                    status=false;
                }
            } catch (Exception e) {
                System.out.println("An error occured please try again.");
                key.next();
                continue;
            }

            
        }
    }
    
    
    
    public static void main(String[] args) {
        
        App myApp = new App();
        
        myApp.run();
        
    }
}

