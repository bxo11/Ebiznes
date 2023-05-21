![example](https://github.com/bxo11/Ebiznes/blob/e5734814dead79af38c0ba0f7cdbe228a9679fdf/3/example.png)

# Discord Bot Documentation

## Requirements
- discord.py
- requests

## Token and Configuration

Set your bot token in the `TOKEN` variable.

```python
TOKEN = 'YOUR_BOT_TOKEN'
```

## Commands

### 1. Categories

List all categories.



```
.categories
```

### 2. New Category

Add a new category.



```
.new-category <name> <description>
```

### 3. Products

List all products in a category.



```
.products <category>
```

### 4. New Product

Add a new product to a category.



```
.new-product <name> <category>
```

## Events

### on_message

This event is triggered when a message is sent in the server. The bot logs the user ID and message content to the backend.

# Ktor Backend API Documentation


Endpoints:

1. Messages
   - POST /message: Add a new message
   - GET /message: Get all messages
2. Categories
   - POST /category: Add a new category
   - DELETE /category/{id}: Delete a category by ID
   - GET /category/{id}: Get a category by ID
   - GET /category: Get all categories
3. Products
   - POST /product: Add a new product
   - DELETE /product/{id}: Delete a product by ID
   - GET /product/{id}: Get a product by ID
   - GET /product: Get all products
4. Products by Category
   - GET /productByCategory/{id}: Get all products in a category by ID

