package com.codegym.model;

public class Cart {
    private int id;
    private String name;
    private double price;
    private int quantity;

    public Cart() {
    }

    public Cart(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        return this.getId() == ((Cart)o).getId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void ToCart(Smartphone smartphone) {
        this.id = smartphone.getId();
        this.name = smartphone.getModel();
        this.price =  smartphone.getPrice();
        this.quantity = 1;
    }
}
