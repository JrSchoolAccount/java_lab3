package org.java_lab3.service;

import org.java_lab3.entities.Product;
import org.java_lab3.entities.ProductType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Warehouse {
    private final List<Product> products = new ArrayList<>();

    private void checkIfProductsEmpty() {
        if (products.isEmpty()) {
            throw new IllegalStateException("No products available!");
        }
    }

    public void newProduct(int id, String name, ProductType type, int rating, LocalDate created, LocalDate modified) {
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
        checkIfProductsEmpty();

        return new ArrayList<>(products);
    }

    public Product getProductById(int id) {

            return products.stream()
                    .filter(product -> product.id() == id)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Product with id: " + id + ", does not exist"));
    }

    public List<Product> getProductsByTypeSortedAtoZ(ProductType type) {
        checkIfProductsEmpty();

        List<Product> productsByType = products.stream()
                .filter(product -> product.type().equals(type))
                .sorted(Comparator.comparing(Product::name))
                .toList();

        if (productsByType.isEmpty()) {
            throw new IllegalArgumentException("No products with type: "+ type + " found!");
        }

        return productsByType;
    }

    private void checkDate(int year, int month, int day) {
        LocalDate inputDate = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();

        if (inputDate.isAfter(now)) {
            throw new IllegalArgumentException("Wrong date format!");
        }
    }

    public List<Product> getProductsCreatedAfter(int year, int month, int day) {
        checkDate(year, month, day);
        checkIfProductsEmpty();

        LocalDate targetDate = LocalDate.of(year, month, day);

        List<Product> productsCreatedAfter = products.stream()
                        .filter(product -> product.created()
                        .isAfter(targetDate))
                        .toList();

        if (productsCreatedAfter.isEmpty()) {
            throw new IllegalArgumentException("No products created after: " + targetDate + " found!");
        }

        return productsCreatedAfter;
    }

    public void modifyProduct(int id, String newName, ProductType newType, int newRating) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty!");
        }

        if (newRating < 0 || newRating > 10) {
            throw new IllegalArgumentException("Invalid rating value: " + newRating + ". Rating must be between 0 and 10");
        }

        try {
            Product oldProduct = getProductById(id);

            Product updatedProduct = new Product(
                    oldProduct.id(),
                    newName,
                    newType,
                    newRating,
                    oldProduct.created(),
                    LocalDate.now()
            );

            products.remove(oldProduct);
            products.add(updatedProduct);

        } catch (Exception e){
            throw new IllegalArgumentException("Product creation failed");
        }
    }

    public List<Product> getAllModifiedProducts() {
        checkIfProductsEmpty();

        List<Product> modified = products.stream()
                .filter(product -> !product.created().equals(product.modified()))
                .toList();

        if (modified.isEmpty()) {
            throw new IllegalArgumentException("No modified products found!");
        }

        return modified;
    }

    public List<ProductType> getTypesWithAtLeastOneProduct() {
        checkIfProductsEmpty();

        return products.stream()
                .map(Product::type)
                .distinct()
                .toList();
    }
}
