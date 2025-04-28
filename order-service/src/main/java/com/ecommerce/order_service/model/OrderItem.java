package com.ecommerce.order_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * This class creates the OrderItem entity.
 */

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference // Use this to ignore the property during the serialization (Java object to JSON).
    private Order order;   // Deserializtion means that JSON to java objects.

    @Getter
    @Setter
    private Long productId;

    @Getter
    @Setter
    private String productName;

    @Getter
    @Setter
    private int quantity;

    @Getter
    @Setter
    private String productSku;


}



