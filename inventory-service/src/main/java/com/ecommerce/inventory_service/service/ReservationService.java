package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.model.Reservation;
import com.ecommerce.inventory_service.model.Stock;
import com.ecommerce.inventory_service.repository.ReservationRepository;
import com.ecommerce.inventory_service.repository.StockRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import java.util.Map;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ReservationService {

    private final KafkaProducerService kafkaProducerService;
    private final StockRepository stockRepository;
    private final ReservationRepository reservationRepository;
    private final RedissonClient redissonClient;

    public ReservationService(KafkaProducerService kafkaProducerService, StockRepository stockRepository, ReservationRepository reservationRepository, RedissonClient redissonClient) {
        this.kafkaProducerService = kafkaProducerService;
        this.stockRepository = stockRepository;
        this.reservationRepository = reservationRepository;
        this.redissonClient = redissonClient;
    }

//    @Transactional
    public boolean reserveInventory(String orderId, String productSku, int quantity) {
        System.out.println("Reserving stock for Product: " + productSku + ", Quantity: " + quantity);

        // Find stock for the product
        Stock stock = stockRepository.findByProductSku(productSku)
            .orElse(null);
        if (stock == null || stock.getQuantity() < quantity) {
            // Not enough stock available
            return false;
        }

        // Create Redis key for reservation
        String redisKey = "reservation:" + orderId;

        // Use a hash structure to store multiple products for the same order
        RMap<String, String> reservationMap = redissonClient.getMap(redisKey);

        // Add this product to the reservation
        reservationMap.put(productSku, String.valueOf(quantity));

        // Set TTL for the entire hash
        reservationMap.expire(10, TimeUnit.MINUTES);


//        //Create reservation record
//        Reservation reservation = new Reservation();
//        reservation.setProductSku(productSku);
//        reservation.setOrderId(orderId);
//        reservation.setReservedQuantity(quantity);
//        reservation.setStatus(Reservation.ReservationStatus.PENDING);
//        reservation.setReservationExpiry(LocalDateTime.now().plusMinutes(30));
//        reservationRepository.save(reservation);
//        // Update stock in database
//        stock.setQuantity(stock.getQuantity() - quantity);
//        stockRepository.save(stock);

        // Publish stock updated event
        kafkaProducerService.sendMessage("stock.updated",
            "{ \"productSku\": \"" + productSku + "\", \"quantity\": " + quantity +
            ", \"status\": \"reserved\", \"orderId\": \"" + orderId + "\" }");

        return true;
    }

//    @Transactional
    public boolean releaseReservedInventory(String orderId) {
        System.out.println("Releasing reserved stock for Order ID: " + orderId);

        // Delete the Redis key
        String redisKey = "reservation:" + orderId;
        boolean deleted = redissonClient.getMap(redisKey).delete();


//        // Find reservation for the order
//        Reservation reservation = reservationRepository.findByOrderId(orderId).get(0);
//        // Find stock for the product
//        Stock stock = stockRepository.findByProductSku(reservation.getProductSku())
//            .orElse(null);
//
//        if (stock == null) {
//            return false;
//        }
//
//        // Update stock in database - add the quantity back
//        stock.setQuantity(stock.getQuantity() + reservation.getReservedQuantity());
//        stockRepository.save(stock);
//
//        //update reservation status to cancelled
//        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
//        reservationRepository.save(reservation);

        if (deleted) {
            // Publish stock released event
            kafkaProducerService.sendMessage("stock.updated",
                    "{ \"orderId\": \"" + orderId + "\", \"status\": \"released\" }");
        }

        return deleted;
    }

    @Transactional
    public boolean completeReservation(String orderId) {

        String redisKey = "reservation:" + orderId;
        RMap<String, String> reservationMap = redissonClient.getMap(redisKey);

        if (reservationMap.isEmpty()) {
            return false;  // Reservation expired or does not exist
        }

        // Process each product in the reservation
        for (Map.Entry<String, String> entry : reservationMap.entrySet()) {
            String productSku = entry.getKey();
            int quantity = Integer.parseInt(entry.getValue());

            // Update DB stock (actual reduction)
            Stock stock = stockRepository.findByProductSku(productSku).orElse(null);
            if (stock == null) {
                continue;  // Skip this product if not found
            }

            // Update stock in database
            stock.setQuantity(stock.getQuantity() - quantity);
            stockRepository.save(stock);

            // Create permanent reservation record
            Reservation reservation = new Reservation();
            reservation.setProductSku(productSku);
            reservation.setOrderId(orderId);
            reservation.setReservedQuantity(quantity);
            reservation.setStatus(Reservation.ReservationStatus.COMPLETED);
            reservation.setReservationExpiry(LocalDateTime.now().plusDays(1));
            reservationRepository.save(reservation);
        }

        // Delete Redis key after successful DB update
        reservationMap.delete();

        // Publish confirmation event
        kafkaProducerService.sendMessage("stock.confirmed",
                "{ \"orderId\": \"" + orderId + "\", \"status\": \"confirmed\" }");

        return true;

//        List<Reservation> reservations = reservationRepository.findByOrderId(orderId);
//        if (reservations.isEmpty()) {
//            return false;
//        }
//        for (Reservation reservation : reservations) {
//            reservation.setStatus(Reservation.ReservationStatus.COMPLETED);
//            reservationRepository.save(reservation);
//        }
//
//        kafkaProducerService.sendMessage("stock.confirmed",
//                "{ \"orderId\": \"" + orderId + "\", \"status\": \"confirmed\" }");
//
//        return true;
    }


//    @Scheduled(fixedRate = 60000) //Run every minute
//    public void releaseExpiredReservations() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Reservation> expiredReservations = reservationRepository.findByStatusAndReservationExpiryBefore(Reservation.ReservationStatus.PENDING,now);
//        for (Reservation reservation : expiredReservations) {
//            // Release the reserved stock
//            releaseReservedInventory(reservation.getOrderId());
//            reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
//            reservationRepository.save(reservation);
//        }
//    }
}