/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package com.aop.aopcart.exception;

/**
 *
 * @author squid
 */
public class OutOfStockException extends RuntimeException {

    /**
     * Creates a new instance of <code>OutOfStockException</code> without detail
     * message.
     */
    public OutOfStockException() {
    }

    /**
     * Constructs an instance of <code>OutOfStockException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OutOfStockException(String productName, int requested, int available) {
        super(String.format("Stock Error: Cannot purchase %d of '%s'. Only %d available.", requested, productName, available));
    }
}
