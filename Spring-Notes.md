```
# Spring Boot Notes

## Table of Contents
1. [JPA and Database](#jpa-and-database)
   - [Entity Relationships](#entity-relationships)
   - [SQL Considerations](#sql-considerations)
2. [Microservices Architecture](#microservices-architecture)
3. [REST API Design](#rest-api-design)
4. [Security](#security)
5. [Testing](#testing)
6. [Performance Optimization](#performance-optimization)
7. [Troubleshooting](#troubleshooting)

## JPA and Database

### Entity Relationships

#### One-to-Many and Many-to-One Relationships

When working with JPA entity relationships, it's important to understand how they map to database structures:

For example, in an Order Management System:

```java
// In Order.java
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
private List<OrderItem> orderItems = new ArrayList<>();

// In OrderItem.java
@ManyToOne
@JoinColumn(name = "order_id")
private Order order;
```

This creates a bidirectional relationship between Order and OrderItem entities, but not in the way you might initially expect:


There is no orderitems column in the orders table
There is no separate orderitems table created
Instead, JPA creates:


An orders table (from @Table(name = "orders") in the Order class)
An order_item table (from @Table(name = "order_item") in the OrderItem class)
An order_id foreign key column in the order_item table
How It Works at Runtime:


When you load an Order entity, Hibernate fetches the record from the orders table.
It then queries for related items with: SELECT * FROM order_item WHERE order_id = ?.
It populates the orderItems list with these related OrderItem objects.
The orderItems field exists only in Java memory as a collection that Hibernate populates - not as a database column.

Benefits of Bidirectional Relationships:


Navigate from parent to children: order.getOrderItems()
Navigate from child to parent: orderItem.getOrder()
Maintain database normalization while providing convenient object navigation organize this markdown
```