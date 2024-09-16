package org.java_lab3.entities;

import java.time.LocalDate;

public record Product(int id, String name, ProductType type, int rating, LocalDate created, LocalDate modified) {}
