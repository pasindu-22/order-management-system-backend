package com.ecommerce.product_service.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;

    // Constructors, Getters, Setters
}
