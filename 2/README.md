## docker compose
Docker Compose requires setting the `NGROK_AUTHTOKEN` variable with a token from your account.
## Endpoints

### Categories
- GET /category: Retrieve all categories
- GET /category/{itemId}: Retrieve a category by its ID
- PUT /category: Update a category by its ID
- DELETE /category/{itemId}: Delete a category by its ID
- POST /category: Add a new category

### Orders
- GET /order/user/{userId}: Retrieve all orders for a given user ID
- GET /order/{itemId}: Retrieve an order by its ID
- PUT /order: Update an order by its ID
- DELETE /order/{itemId}: Delete an order by its ID
- POST /order: Add a new order

### Products
- GET /product: Retrieve all products
- GET /product/{itemId}: Retrieve a product by its ID
- PUT /product: Update a product by its ID
- DELETE /product/{itemId}: Delete a product by its ID
- POST /product: Add a new product
