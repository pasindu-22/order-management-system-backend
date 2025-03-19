package com.ecommerce.inventory_service.model;

import com.ecommerce.inventory_service.dto.StockCreateDTO;
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
    private int quantity=0;

    @Column(nullable = false)
    private int lowStockThreshold; // For alerts

    public static Stock stockDtoTransfer(StockCreateDTO stockCreateDTO) {
        Stock stock = new Stock();
        stock.productSku = stockCreateDTO.getProductSku();
        stock.quantity = stockCreateDTO.getQuantity();
        stock.lowStockThreshold = stockCreateDTO.getLowStockThreshold();
        return stock;
    }

    public Stock() {

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public String getProductSku() {
        return productSku;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }
}
