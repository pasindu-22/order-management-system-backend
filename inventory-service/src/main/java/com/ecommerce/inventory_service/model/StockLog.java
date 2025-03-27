package com.ecommerce.inventory_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_log")
public class StockLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false)
    private int quantityBefore;

    @Column(nullable = false)
    private int quantityAfter;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String actionType; // e.g., "STOCK_IN", "STOCK_OUT", "RESERVATION", "ADJUSTMENT"

    @Column(nullable = false)
    private String inspectorCode;

    // Constructors, getters, and setters
    public StockLog() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public int getQuantityBefore() {
        return quantityBefore;
    }

    public int getQuantityAfter() {
        return quantityAfter;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setQuantityBefore(int quantity) {
        this.quantityBefore = quantity;
    }

    public void setQuantityAfter(int quantity) {
        this.quantityAfter = quantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getInspectorCode() {
        return inspectorCode;
    }

    public void setInspectorCode(String inspectorCode) {
        this.inspectorCode = inspectorCode;
    }
}