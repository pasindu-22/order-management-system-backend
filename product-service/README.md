# Product Service

## Purpose of the Service
- Product catalog management (CRUD operations on products)
- Integration with Inventory Service for stock information
- Price management (setting, updating, and retrieving prices)
- Search and filtering capabilities for product discovery
    
## Architecture
- Spring Boot REST API (port 8083)
- MySQL database for product persistence
- Integration with Inventory Service via RestTemplate
    
## Public API Endpoints
| HTTP Method | Endpoint | Description |
|-------------|----------|-------------|
| GET | /api/products | Get all products |
| GET | /api/products/{id} | Get product details by ID |
| POST | /api/products | Create a new product |
| DELETE | /api/products/{id} | Remove a product |
    
## Data Model
The Product entity contains:
- productId: Unique identifier (auto-generated)
- name: Product name
- sku: Stock Keeping Unit (unique identifier for inventory)
- shortDescription: Brief product description
- fullDescription: Detailed HTML product description
- price: Product pricing information
- imageUrl: Link to product image
- createdAt: Timestamp of product creation
    
## Service Integration
- Connects to Inventory Service to retrieve current stock levels
- Uses ProductStockDTO to combine product and inventory data
    
## Future Enhancements
- Category management
- Search and filtering optimization
- Caching with Redis for frequently accessed products
- Pagination for large product lists
- Load balancing for high traffic scenarios
- Migration to WebClient for reactive inventory service communication
    
## How to Run
- Navigate to the product-service directory
- Run the following command:
```bash
mvn spring-boot:run
```
## API Documentation
- Swagger UI available at: `http://localhost:8083/swagger-ui/index.html`
- `https://springdoc.org/` Visit for more information about Spring Documentation
