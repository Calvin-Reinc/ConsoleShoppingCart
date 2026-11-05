package com.basiccart;

import java.text.DecimalFormat;
import java.util.Scanner;



public class App {

    public static void menu(int[] productId, String[] productName, Double[] productPrice){
        //displays the items in stock
        
        System.out.println("\n\t==== In Stock ====\t\n");
        System.out.println("   Product ID:\t  Product Name:   Product Price:");
        for (int x=0;x<productId.length;x++) {
            
            System.out.print("\t"+productId[x]+ "\t|    ");
            System.out.print(productName[x] + "\t|    R");
            System.out.println(productPrice[x]+ "\t|");
        }
    }
      
    
    public static int[] addProduct(int[] productId, String[] productName, Double[] productPrice, int cartSize, int[] cartId, int[] cartQuantity, Scanner key) {
        
        menu(productId, productName,  productPrice);
        System.out.print("\n == Select a product,(Using ID) == \nID: ");
        
        int selectedId = -1, quantity=0;
        boolean added = false;

        selectedId = key.nextInt();
        System.out.print("Please specify quantity: ");
        quantity = key.nextInt();
        /*
            Adds the selected item from menu to cart
            uses cartSize to determine next posiotion
            returns an updated array
        */

        for (int x=0;x<productId.length;x++) {
            if (selectedId == productId[x]) {
                cartId[cartSize] = selectedId;
                cartQuantity[cartSize] = quantity;
                added = true;
            }
        }

        if (selectedId != -1 && added) {
            System.out.println("Product successfully added.");
        } else {
            System.out.println("Invalid ID selection.\n");
        }
            
        
        return cartId;
        
    }
    
    
    public static void displayCart(int[] productId, String[] productName, Double[] productPrice, int[] cartId, int cartSize) {
        
        double cartTotal = 0;
        
        if (cartSize>0) {
            System.out.println("\nCart:\n"
                + "   Product ID:\t  Product Name:   Product Price:");
            for (int x=0;x<cartSize;x++) {          
                System.out.println("\t"+cartId[x]+"\t|    "+productName[cartId[x]]+"\t|    R"+productPrice[cartId[x]]+"\t|");
                cartTotal += productPrice[productId[x]];
            } 
            System.out.println("Total R"+cartTotal+"\t|");
        
        } else {System.out.println("Cart Empty.\n");}
    }
    
    
    public static int removeItem(int[] productId, String[] productName, Double[] productPrice, int[] cartId, int cartSize, int[] cartQuantity, Scanner key) {
        
        int targetIndex = -1;
        boolean found = false;
        
        if (cartSize > 0) {
            displayCart(productId, productName, productPrice, cartId, cartSize);
            System.out.print("\nPlease specify ID: ");
            targetIndex = key.nextInt();


            //removing an item in cart
            for (int x=0;x<cartId.length;x++) {
                if (targetIndex == cartId[x]) {
                    targetIndex = x;
                    found = true;
                    break;
                }
            }

            if (targetIndex != -1 && found) {
            // Shift all elements after targetIndex one slot to the left
                for (int i = targetIndex; i < cartSize - 1; i++) {
                    cartId[i] = cartId[i + 1];
                    cartQuantity[i] = cartQuantity[i + 1];
                }

                    // Crucial: decrease your cart counter by 1
                    cartSize--; 
                    System.out.println("Product removed from cart.");
            } else {
                System.out.println("Product not found in your cart.");
            }
        } else {
            System.out.println("Your cart is empty.\n");    
        }
        
        
        return cartSize;
    
    }
    
    
    public static void main(String[] args) {
        //init all variables
        int[] productId = {0, 1, 2, 3};
        String[] productName = {"shirt01","sock01", "cap01", "pants01"};
        Double[] productPrice = {349.99, 89.99, 239.99, 699.99};
        
        int[] cartId = new int[50];
        int[] cartQuantity = new int[50];
        int cartSize = 0;
        
        boolean status = true;
        Scanner key = new Scanner(System.in);
        
        //start app here
        System.out.println("=== Welcome to CarelStore ===");
        menu(productId, productName,  productPrice);
        int userOption = 0;
        
        //loops the program until status false
        while (status) {
            
            System.out.print("Select an option below:\n"
                + "1. Add a product to cart(Using ID).\n"
                + "2. View shopping cart.\n"
                + "3. Remove cart item.\n"
                + "4. Clear cart.\n"
                + "5. Exit."
                + "\nOption: ");
           
            userOption = key.nextInt();
            
            if (userOption == 1) {
                cartId = addProduct(productId, productName, productPrice, cartSize, cartId, cartQuantity, key);
                if (cartSize != cartId.length) {
                    cartSize++;
                }
                
            } else if (userOption == 2) {
                displayCart(productId, productName, productPrice, cartId, cartSize);
            } else if (userOption == 3) {
                cartSize = removeItem(productId, productName, productPrice, cartId, cartSize, cartQuantity, key);
            } else if (userOption == 4) {
                cartSize=0;
                System.out.println("Cart cleared.\n");
            } else if (userOption == 5) {
                status=false;
            } else {System.out.println("Invalid menu option.\n");}
            
        }

    }
}
