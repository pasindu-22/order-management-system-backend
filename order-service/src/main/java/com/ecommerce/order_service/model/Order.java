package com.ecommerce.order_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Getter
    @Setter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)  // CascadeType.ALL means that all operations (persist, remove, etc.) will be cascaded to the order items.(If remove all the order items will beremoved.
    @JsonManagedReference               // Use this to include the property during the serialization(Java object to JSON).
    private List<OrderItem> orderItems = new ArrayList<>();

    @Getter
    @Setter
    private Double totalPrice;

    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private Date orderDate;
}
