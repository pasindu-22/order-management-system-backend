package com.ecommerce.inventory_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productSku;

    @Column(nullable = false)
    private int reservedQuantity;

    @Column(nullable = false)
    private LocalDateTime reservationExpiry; // Auto-release if expired

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public enum ReservationStatus {
        PENDING, COMPLETED, CANCELLED
    }
}
