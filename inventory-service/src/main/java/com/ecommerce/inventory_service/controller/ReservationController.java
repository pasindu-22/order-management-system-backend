package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.ReservationRequestDTO;
import com.ecommerce.inventory_service.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveInventory(@RequestBody ReservationRequestDTO request) {
        boolean success = reservationService.reserveInventory(
            request.getOrderId(),
            request.getProductSku(),
            request.getQuantity()
        );

        if (success) {
            return ResponseEntity.ok("Inventory reserved successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to reserve inventory");
        }
    }

    @PostMapping("/release/{orderId}")
    public ResponseEntity<String> releaseInventory(@PathVariable String orderId) {
        boolean success = reservationService.releaseReservedInventory(orderId);

        if (success) {
            return ResponseEntity.ok("Reserved inventory released successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to release inventory");
        }
    }
}