**## Inventory Service by Pasindu**

* Track stock levels for each product
* Update stock on purchase (reduce stock count)
* Restock management (increase stock count)
* Support for multiple warehouses (if applicable)
* Handle inventory reservations for pending orders
* Check stock availability before order confirmation
* Stock alerts (low-stock notifications to admins)
* Batch stock updates (e.g., bulk import from suppliers)
* Integration with order service for real-time stock updates
* Event-based stock updates (e.g., Kafka/RabbitMQ for consistency across services)

APIs :-
* getStockByProductSku [Synchronous API] :- Product service needs real-time stock info.

* updateStockOnPurchase [Event - Driven] :- Prevents order service from waiting. Stock updates can be eventual consistency.

* reserveInventory(orderId) [Event-Driven] :- Ensures inventory is reserved asynchronously without blocking the order process.

* checkStockAvailability() [ Synchronous API] :- Used for quick lookups, needs instant response.

* restock() [Event-Driven] :- Suppliers may send stock asynchronously; batch processing works better.

* newStock() [Event-Driven] :- Allows bulk stock additions without blocking admin operations.

* lowStockAlerts() [Event-Driven] :- Send event when stock is below threshold (notify admins, trigger restock).

* batchStockUpdates() [Event-Driven] :- High-volume updates should be processed asynchronously.

* releaseReservedInventory() [Event-Driven] :- If an order is canceled, event-driven rollback ensures stock is not lost.

* adjustStock(productSku, quantity, reason) [ Synchronous API] :- Admins may need to manually adjust stock due to discrepancies.

* Warehouse management APIs


Kafka Event Topics for Inventory Service
* order.placed → Trigger reserveInventory event
* order.canceled → Trigger releaseReservedInventory event
* payment.failed → Trigger releaseReservedInventory event
* stock.low → Trigger lowStockAlerts for admin notifications
* stock.updated → Trigger real-time stock updates in Product Service

Example Event Flow Using Kafka
* Order Flow (Asynchronous)
1. Order Service → Publishes order.placed event.
2. Inventory Service → Listens for order.placed, reserves inventory asynchronously. 
3. Payment Service → Processes payment, publishes payment.success or payment.failed.
   * If payment.success, Inventory updates stock. 
   * If payment.failed, Inventory releases reserved stock.

Kafka Setting up process :-
* Add kafka starter to dependency list.
* Configure kafka in application.yml
* Define Kafka topics for inventory service
* Implement Kafka Producer in I.S.
* Implement Kafka Consumer in I.S.
* Implement using of Kafka consumer/producer services. (reserveInventory & releaseReservedInventory in reservationService class in Service package).
* Run and Test Kafka & Inventory Service. (First install Kafka locally[Ver 3.6]).
* Start Kafka & Zookeeper
  cd C:\kafka
  bin\windows\zookeeper-server-start.bat config\zookeeper.properties
  cd C:\kafka
  bin\windows\kafka-server-start.bat config\server.properties

* Run Inventory Service
* Send Kafka Events (Navigate to C:\kafka first)
Run this command to start a producer that sends messages to the order.placed topic:-
>>
kafka-console-producer.sh --broker-list localhost:9092 --topic order.placed
>>
** Then Interactive mode :-
>{"orderId":"12345","productSku":"P001","quantity":2}
Run this command to view messages just sent above
>>
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic order.placed --from-beginning
>>

Run the command to view order message :-
>>
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic stock.updated --from-beginning
>>

* In our application stock reservation/release can be done via kafka and REST APIs.

* For local development:
Start Kafka locally before running your Spring Boot application:

Start ZooKeeper first
>>
cd C:\kafka
bin\windows\zookeeper-server-start.bat config\zookeeper.properties
Then start Kafka in a separate terminal
>>
cd C:\kafka
bin\windows\kafka-server-start.bat config\server.properties
Create required topics (if they don't exist):

>>
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic order.placed --partitions 1 --replication-factor 1
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic stock.updated --partitions 1 --replication-factor 1
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic order.canceled --partitions 1 --replication-factor 1
bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic payment.failed --partitions 1 --replication-factor 1


Cloud Deployment :-
* When deploying to the cloud:
* Managed Kafka Service: Use a managed Kafka service like:
* AWS MSK (Managed Streaming for Kafka)
* Azure Event Hubs for Kafka

Development Notes :-
* Uses SKU as the common identifier between services
* Keeps stock management in the inventory service
* Allows the product service to fetch stock information when needed
* There are KafkaListener services in our application that listens to various kafka topics and does the job accordingly.




To Do :-

1. [ ] Pending - Update the stock reservation logic to create records in the reservation table and implement release logic.
2. [ ] Update reservationRequestDTO to include other details about reservations and necessary event for expiry and so on.

	             

