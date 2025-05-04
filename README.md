# Order Management System Backend

This monorepo contains multiple Spring Boot microservices for an order management system.

## Microservices:
- `product-service`: Manages products.
- `user-service`: Handles user authentication and profiles.
- `order-service`: Processes orders.
- `inventory-service`: Manages inventory levels.

## How to Run
Each service has its own `pom.xml`. Navigate to the service and run:
  mvn spring-boot:run

## How to run eureka server
   http://localhost:8761/

Available services:
1. eureka-server : port 8761
2. API Gateway  : port 8080
3. Inventory Service : port 8081
4. Order Service : port 8082
5. Product Service : port 8083
6. User Service : port 8084

- Uses eureka server to find the services and API Gateway to route the request to the respective services dynamically without hardcoding URLs.
- Uses API Gateway to provide a single entry point for clients and secure, route, and balance requests
- Uses Spring Security to secure the services and JWT for authentication and authorization.


