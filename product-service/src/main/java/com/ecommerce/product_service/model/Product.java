package com.ecommerce.product_service.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity  // Marks this class as a JPA entity (table)
@Table(name = "products")  // Defines the table name
public class Product {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Automatically generates a unique ID
    private UUID productId;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")  // Store large HTML content
    private String fullDescription;

    @Column(nullable = false)
    private BigDecimal price;

    private int stock;

    private String imageUrl;  // Store image URL (not actual image)

    private LocalDateTime createdAt;

    @PrePersist  // Automatically sets createdAt before saving
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescriptions) {
        this.fullDescription = fullDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
