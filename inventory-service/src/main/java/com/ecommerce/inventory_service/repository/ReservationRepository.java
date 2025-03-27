package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    List<Reservation> findByOrderId(String orderId);
    List<Reservation> findByStatusAndReservationExpiryBefore(
            Reservation.ReservationStatus status, LocalDateTime time);
}
