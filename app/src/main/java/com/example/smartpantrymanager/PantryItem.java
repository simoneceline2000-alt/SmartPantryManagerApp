package com.example.smartpantrymanager;

public class PantryItem {
    private String name;
    private int quantity;
    private String unit;
    private String expiry;

    public PantryItem(String name, int quantity, String unit, String expiry) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiry = expiry;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getExpiry() { return expiry; }
}
