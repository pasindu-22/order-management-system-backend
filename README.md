# E-Commerce Platform Backend

This monorepo contains multiple Spring Boot microservices for an e-commerce system.

## Microservices:
- `product-service`: Manages products.
- `user-service`: Handles user authentication and profiles.
- `order-service`: Processes orders.

## How to Run
Each service has its own `pom.xml`. Navigate to the service and run:
  mvn spring-boot:run

## How to run eureka server
   http://localhost:8761/

Order of server start
1. eureka-server
2. product-service/Other services
3. API Gateway

## We use eureka server to find the services and API Gateway to route the request to the respective services dynamically without hardcoding URLs.
## We use API Gateway to provide a single entry point for clients and secure, route, and balance requests
