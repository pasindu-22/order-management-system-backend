package com.ecommerce.inventory_service.model;

import com.ecommerce.inventory_service.model.Warehouse;
import jakarta.persistence.*;

@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productSku;  // SKU to identify products

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int lowStockThreshold; // For alerts
}
