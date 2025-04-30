# Inventory Service

## Overview
A microservice for inventory management developed by Pasindu.

## Features
- Track stock levels for each product
- Update stock on purchase (reduce stock count)
- Restock management (increase stock count)
- Support for multiple warehouses
- Handle inventory reservations for pending orders
- Check stock availability before order confirmation
- Stock alerts (low-stock notifications to admins)
- Batch stock updates (e.g., bulk import from suppliers)
- Integration with order service for real-time stock updates
- Event-based stock updates (e.g., Kafka for consistency across services)

## API Endpoints

### Synchronous APIs
- `getStockByProductSku` - Product service needs real-time stock info
- `checkStockAvailability()` - Used for quick lookups, needs instant response
- `adjustStock(productSku, quantity, reason)` - Admins may need to manually adjust stock due to discrepancies
- Various warehouse management APIs

### Event-Driven APIs
- `updateStockOnPurchase` - Prevents order service from waiting. Stock updates can be eventual consistency
- `reserveInventory(orderId)` - Ensures inventory is reserved asynchronously without blocking the order process
- `restock()` - Suppliers may send stock asynchronously; batch processing works better
- `newStock()` - Allows bulk stock additions without blocking admin operations
- `lowStockAlerts()` - Send event when stock is below threshold (notify admins, trigger restock)
- `batchStockUpdates()` - High-volume updates should be processed asynchronously
- `releaseReservedInventory()` - If an order is canceled, event-driven rollback ensures stock is not lost

## Kafka Integration

### Event Topics
- `order.placed` → Trigger reserveInventory event
- `order.canceled` → Trigger releaseReservedInventory event
- `stock.low` → Trigger lowStockAlerts for admin notifications
- `stock.updated` → Trigger real-time stock updates in Product Service
- `order.completed` → Trigger stock reservation complete.

### Example Event Flow
**Order Flow (Asynchronous)**
1. Order Service → Publishes order.placed event
2. Inventory Service → Listens for order.placed, reserves inventory asynchronously

## Setup Instructions

### Local Kafka Setup
1. **Install Kafka locally** (Version 3.6 recommended)
2. **Add configuration**
  - Add kafka starter to dependency list
  - Configure kafka in application.yml
  - Define Kafka topics for inventory service
  - Implement Kafka Producer in Inventory Service
  - Implement Kafka Consumer in Inventory Service
  - Implement using of Kafka consumer/producer services (reserveInventory & releaseReservedInventory in reservationService class in Service package)

3. **Start Kafka & Zookeeper**
   ```
   cd C:\kafka
   bin\windows\zookeeper-server-start.bat config\zookeeper.properties
   
   cd C:\kafka
   bin\windows\kafka-server-start.bat config\server.properties
   ```

4. **Create required topics**
   ```
   bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic order.placed --partitions 1 --replication-factor 1
   bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic stock.updated --partitions 1 --replication-factor 1
   bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic order.canceled --partitions 1 --replication-factor 1
   bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic payment.failed --partitions 1 --replication-factor 1
   ```

### Docker Setup
1. **First time start**:
   ```
   docker run -d --name=kafka -p 9092:9092 apache/kafka
   ```

2. **Subsequent starts**:
   ```
   docker start kafka
   ```

3. **Verify cluster**:
   ```
   docker exec -ti kafka /opt/kafka/bin/kafka-cluster.sh cluster-id --bootstrap-server :9092
   ```

## Testing Kafka Integration

### Sending Test Messages

1. **Start a producer for order.placed topic**:
  - Local installation:
    ```
    kafka-console-producer.sh --broker-list localhost:9092 --topic order.placed
    ```
  - With Docker:
    ```
    docker exec -ti kafka /opt/kafka/bin/kafka-console-producer.sh --bootstrap-server :9092 --topic order.placed
    ```

2. **Send a sample message in interactive mode**:
   ```
   {"orderId":"12345","productSku":"SKU12345","quantity":2}
   ```

3. **View sent messages**:
  - Local installation:
    ```
    bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic order.placed --from-beginning
    ```
  - With Docker:
    ```
    docker exec -ti kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server :9092 --topic order.placed --from-beginning
    ```

4. **View stock update messages**:
  - Local installation:
    ```
    bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic stock.updated --from-beginning
    ```
  - With Docker:
    ```
    docker exec -ti kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server :9092 --topic stock.updated --from-beginning
    ```

## Cloud Deployment

When deploying to the cloud, use a managed Kafka service like:
- AWS MSK (Managed Streaming for Kafka)
- Azure Event Hubs for Kafka

## Architecture Notes
- Uses SKU as the common identifier between services
- Keeps stock management in the inventory service
- Allows the product service to fetch stock information when needed
- Implements KafkaListener services that listen to various kafka topics and perform operations accordingly
- Both Kafka and REST APIs can be used for stock reservation/release operations

## Redis Implementation

The inventory service uses Redis for temporary reservation storage with TTL (Time-To-Live) functionality.

### Reservation Flow
1. When a product is reserved, the reservation is stored in Redis with a 10-minute TTL
2. If payment is completed within the TTL, the reservation is committed to the database
3. If payment fails or TTL expires, the reservation is automatically discarded

### Redis Structure
- Key pattern: `reservation:{orderId}`
- Type: Hash map
- Fields: `{productSku}` → `{quantity}`
- TTL: 10 minutes

### Redis Setup

#### Docker Setup
```bash
# Start Redis container
docker run --name redis -p 6379:6379 -d redis

# Stop Redis
docker stop redis

# Start existing Redis container
docker start redis
```
### Monitoring Reservations
### Connect to Redis CLI
```
docker exec -it redis redis-cli
```

### View active reservations
```
KEYS reservation:*
```

### View details of a specific reservation
```
HGETALL reservation:{orderId}
```

### Check remaining TTL in seconds
```
TTL reservation:{orderId}
```

### Benefits of Redis implementation
- No database writes until payment is confirmed
- Automatic expiration through Redis TTL
- Multiple products can be reserved with one order ID
- Better concurrency handling for high-traffic scenarios
- Reduced database load