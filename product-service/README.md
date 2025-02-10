Product Service:
✅ Purpose of the service:-
    * Product catalog management (CRUD operations on products).
    ✅ Stock management (keeping track of inventory).
    ✅ Price management (setting, updating, and retrieving prices).
    ✅ Category management (grouping products into categories).
    ✅ Search and filtering (helping customers find products easily).

✅Public API Endpoints
    HTTP Method	Endpoint	Description
    GET	/products/{id}	Get product details by ID
    GET	/products?category={id}	Get products by category
    POST	/products	Create a new product
    PUT	/products/{id}	Update product details
    DELETE	/products/{id}	Remove a product
    PATCH	/products/{id}/stock	Update stock after an order

** Handle Performance and Scalability
* Use caching (Redis) for frequently accessed products.
* Optimize queries to prevent slow responses.
* Implement pagination for large product lists.
* Load balancing if handling high traffic