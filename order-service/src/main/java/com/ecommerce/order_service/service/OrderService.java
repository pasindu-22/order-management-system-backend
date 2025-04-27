package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderCreateRequest;
import com.ecommerce.order_service.dto.OrderItemRequest;
import com.ecommerce.order_service.dto.ProductDto;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderItem;
import com.ecommerce.order_service.model.OrderStatus;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.restTemplate = restTemplate;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order createOrder(OrderCreateRequest orderRequest) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.CREATED);
        order.setUserId(orderRequest.getUserId());

        // Calculate total price based on order items
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            // Get product details from product service
            ProductDto product = restTemplate.getForObject(
                    productServiceUrl + "/api/products/" + itemRequest.getProductId(),
                    ProductDto.class);
            if (product == null) {
                throw new RuntimeException("product not found");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            orderItemRepository.save(orderItem);

            totalPrice += product.getPrice() * itemRequest.getQuantity();

        }
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        System.out.printf("Saved order details - ID: %d, Date: %s, Status: %s, Items: %d, Total Price: %.2f%n",
                         savedOrder.getId(), savedOrder.getOrderDate(), savedOrder.getStatus(),
                         savedOrder.getOrderItems().size(), savedOrder.getTotalPrice());
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