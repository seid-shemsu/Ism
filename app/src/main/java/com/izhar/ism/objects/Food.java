package com.izhar.ism.objects;

import java.io.Serializable;

public class Food implements Serializable {
    String name, price, quantity;
    long id;
    public Food(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Food() {
    }

    public Food(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Food(String name, String price, long id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
