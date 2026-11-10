/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package com.aop.aopcart.exception;

/**
 *
 * @author squid
 */
public class ProductNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ProductNotFoundException</code> without
     * detail message.
     */
    public ProductNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ProductNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ProductNotFoundException(String productName) {
        super(String.format("Product missing: Cannot find '%s' in catalogue.", productName));
    }
}
