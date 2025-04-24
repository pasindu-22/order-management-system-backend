# Order Service

## Purpose of the Service
- Manage customer orders and their statuses.
- Handle order items and calculate total order prices.
- Provide CRUD operations for orders.
- Integration with other services for product and inventory management.

## Architecture
- Spring Boot REST API (port 8082).
- MySQL database for order persistence.
- Transactional support for consistent order and order item management.

## Public API Endpoints
| HTTP Method | Endpoint | Description |
|-------------|----------|-------------|
| GET | /api/orders | Get all orders |
| GET | /api/orders/{id} | Get order details by ID |
| POST | /api/orders | Create a new order |
| PUT | /api/orders/{id}/status?status={status} | Update the status of an order |
| DELETE | /api/orders/{id} | Delete an order |

## Data Model
The `Order` entity contains:
- `id`: Unique identifier (auto-generated).
- `status`: Current status of the order (e.g., CREATED, COMPLETED).
- `orderItems`: List of items in the order.
- `totalPrice`: Total price of the order.
- `orderDate`: Timestamp of when the order was created.

The `OrderItem` entity contains:
- `id`: Unique identifier (auto-generated).
- `order`: Reference to the parent order.
- `productId`: ID of the product in the order.
- `productName`: Name of the product.
- `quantity`: Quantity of the product in the order.

## Service Integration
- Can integrate with Product Service to fetch product details.
- Can integrate with Inventory Service to check stock availability.

## Future Enhancements
- Add support for order history and tracking.
- Implement caching for frequently accessed orders.
- Add pagination for large order lists.
- Enhance security with role-based access control.
- Integrate with payment services for order payment processing.

## How to Run
- Navigate to the `order-service` directory.
- Run the following command:
```bash
mvn spring-boot:run