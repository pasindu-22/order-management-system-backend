package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.model.Reservation;
import com.ecommerce.inventory_service.model.Stock;
import com.ecommerce.inventory_service.repository.ReservationRepository;
import com.ecommerce.inventory_service.repository.StockRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final KafkaProducerService kafkaProducerService;
    private final StockRepository stockRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(KafkaProducerService kafkaProducerService, StockRepository stockRepository, ReservationRepository reservationRepository) {
        this.kafkaProducerService = kafkaProducerService;
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public boolean reserveInventory(String orderId, String productSku, int quantity) {
        System.out.println("Reserving stock for Product: " + productSku + ", Quantity: " + quantity);

        // Find stock for the product
        Stock stock = stockRepository.findByProductSku(productSku)
            .orElse(null);

        if (stock == null || stock.getQuantity() < quantity) {
            // Not enough stock available
            return false;
        }

        //Create reservation record
        Reservation reservation = new Reservation();
        reservation.setProductSku(productSku);
        reservation.setOrderId(orderId);
        reservation.setReservedQuantity(quantity);
        reservation.setStatus(Reservation.ReservationStatus.PENDING);
        reservation.setReservationExpiry(LocalDateTime.now().plusMinutes(30));
        reservationRepository.save(reservation);
        // Update stock in database
        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.save(stock);

        // Publish stock updated event
        kafkaProducerService.sendMessage("stock.updated",
            "{ \"productSku\": \"" + productSku + "\", \"quantity\": " + quantity +
            ", \"status\": \"reserved\", \"orderId\": \"" + orderId + "\" }");

        return true;
    }

    @Transactional
    public boolean releaseReservedInventory(String orderId) {
        System.out.println("Releasing reserved stock for Order ID: " + orderId);

        // Find reservation for the order
        Reservation reservation = reservationRepository.findByOrderId(orderId).get(0);
        // Find stock for the product
        Stock stock = stockRepository.findByProductSku(reservation.getProductSku())
            .orElse(null);

        if (stock == null) {
            return false;
        }

        // Update stock in database - add the quantity back
        stock.setQuantity(stock.getQuantity() + reservation.getReservedQuantity());
        stockRepository.save(stock);

        //update reservation status to cancelled
        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Publish stock updated event
        kafkaProducerService.sendMessage("stock.updated",
            "{ \"productSku\": \"" + reservation.getProductSku() + "\", \"quantity\": " + reservation.getReservedQuantity() +
            ", \"status\": \"released\", \"orderId\": \"" + orderId + "\" }");

        return true;
    }

    @Transactional
    public boolean completeReservation(String orderId) {
        List<Reservation> reservations = reservationRepository.findByOrderId(orderId);
        if (reservations.isEmpty()) {
            return false;
        }
        for (Reservation reservation : reservations) {
            reservation.setStatus(Reservation.ReservationStatus.COMPLETED);
            reservationRepository.save(reservation);
        }

        kafkaProducerService.sendMessage("stock.confirmed",
                "{ \"orderId\": \"" + orderId + "\", \"status\": \"confirmed\" }");

        return true;
    }

    @Scheduled(fixedRate = 60000) //Run every minute
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> expiredReservations = reservationRepository.findByStatusAndReservationExpiryBefore(Reservation.ReservationStatus.PENDING,now);
        for (Reservation reservation : expiredReservations) {
            // Release the reserved stock
            releaseReservedInventory(reservation.getOrderId());
            reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        }
    }
}