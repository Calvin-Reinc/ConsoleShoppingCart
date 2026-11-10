/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.aop.aopcart.enums;

/**
 *
 * @author squid
 */
public enum Category {
    MosibiBlack("Mosibi Series"),
    CarelBlack("Carel Black I"),
    Disguised("Disguised Editions");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
}
