package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderCreateRequest;
import com.ecommerce.order_service.dto.OrderItemRequest;
import com.ecommerce.order_service.dto.ProductDto;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderStatus;
import com.ecommerce.order_service.repository.OrderItemRepository;
import com.ecommerce.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(orderService, "productServiceUrl", "http://product-service");
    }

    @Test
    void createOrder_whenValidRequest_shouldReturnSavedOrder() {
        // Arrange
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(1L);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(101L);
        itemRequest.setQuantity(2);
        request.setItems(Collections.singletonList(itemRequest));

        ProductDto productDto = new ProductDto();
        productDto.setId(101L);
        productDto.setName("Test Product");
        productDto.setPrice(10.0);

        when(restTemplate.getForObject(anyString(), eq(ProductDto.class)))
                .thenReturn(productDto);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // just return the order passed in

        // Act
        Order result = orderService.createOrder(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getOrderItems().size());
        assertEquals(20.0, result.getTotalPrice());
        assertEquals(OrderStatus.CREATED, result.getStatus());

        verify(restTemplate).getForObject(contains("/api/products/101"), eq(ProductDto.class));
        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).save(any());
    }

    @Test
    void createOrder_whenProductNotFound_shouldThrowRuntimeException() {
        // Arrange
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(1L);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(999L);
        itemRequest.setQuantity(1);
        request.setItems(Collections.singletonList(itemRequest));

        when(restTemplate.getForObject(anyString(), eq(ProductDto.class)))
                .thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(request));
        assertEquals("product not found", exception.getMessage());

        verify(orderRepository, never()).save(any());
        verify(orderItemRepository, never()).save(any());
    }
}
