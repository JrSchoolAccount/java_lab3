package org.java_lab3.service;

import org.java_lab3.entities.Product;
import org.java_lab3.entities.ProductType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private final List<Product> products = new ArrayList<>();

    public void newProduct(int id, String name, ProductType type, int rating, LocalDateTime created, LocalDateTime modified) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        try {
            Product product = new Product(id, name, type, rating, created, modified);
            products.add(product);
        } catch (Exception e) {
            throw new IllegalArgumentException("Product creation failed");
        }
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}
