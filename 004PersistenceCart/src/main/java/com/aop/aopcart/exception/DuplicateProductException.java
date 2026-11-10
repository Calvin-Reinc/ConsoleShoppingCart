/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package com.aop.aopcart.exception;

/**
 *
 * @author squid
 */
public class DuplicateProductException extends RuntimeException {

    /**
     * Creates a new instance of <code>DuplicateProductException</code> without
     * detail message.
     */
    public DuplicateProductException() {
    }

    /**
     * Constructs an instance of <code>DuplicateProductException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DuplicateProductException(String sku) {
        super("Product with SKU '" + sku + "' already exists.");
    }
}
