package org.java_lab3.entities;

import java.time.LocalDateTime;

public class Product {
}

enum ProductType {
    POTION,
    WEAPON,
    ARMOR,
    ARTIFACT,
    SPELL_SCROLL
}

record Products(int id, String name, ProductType type, int rating, LocalDateTime created, LocalDateTime modified) {}
