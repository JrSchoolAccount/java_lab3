package org.java_lab3.entities;

import java.time.LocalDateTime;

public record Product(int id, String name, ProductType type, int rating, LocalDateTime created, LocalDateTime modified) {}
