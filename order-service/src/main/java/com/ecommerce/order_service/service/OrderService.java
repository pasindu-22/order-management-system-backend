package com.ecommerce.order_service.service;

import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderItem;
import com.ecommerce.order_service.model.OrderStatus;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order createOrder(Order order) {
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.CREATED);

        // Calculate total price based on order items
        double totalPrice = order.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * 0.0) // Replace 0.0 with actual price logic
                .sum();

        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        System.out.printf("Saved order details - ID: %d, Date: %s, Status: %s, Items: %d, Total Price: %.2f%n",
                         savedOrder.getId(), savedOrder.getOrderDate(), savedOrder.getStatus(),
                         savedOrder.getOrderItems().size(), savedOrder.getTotalPrice());

        // Set the order reference in each order item and save them
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> {
                item.setOrder(savedOrder);
                orderItemRepository.save(item);
            });
        }

        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}