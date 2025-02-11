# E-Commerce Platform Backend

This monorepo contains multiple Spring Boot microservices for an e-commerce system.

## Microservices:
- `product-service`: Manages products.
- `user-service`: Handles user authentication and profiles.
- `order-service`: Processes orders.

## How to Run
Each service has its own `pom.xml`. Navigate to the service and run:

```sh
mvn spring-boot:run

## How to run eureka server
```sh
http://localhost:8761/

