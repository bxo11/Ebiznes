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


## Endpoints

### 1. Messages

#### 1.1. POST `/message`

Add a new message.

Request body: JSON object containing `userId` and `message`.

Response: `"Added"`.

#### 1.2. GET `/message`

Get all messages.

Response: JSON array of messages.

### 2. Categories

#### 2.1. POST `/category`

Add a new category.

Request body: JSON object containing `name` and `description`.

Response: `"Added"`.

#### 2.2. DELETE `/category/{id}`

Delete a category by ID.

Response: JSON object of the deleted category.

#### 2.3. GET `/category/{id}`

Get a category by ID.

Response: JSON object of the requested category.

#### 2.4. GET `/category`

Get all categories.

Response: JSON array of categories.

### 3. Products

#### 3.1. POST `/product`

Add a new product.

Request body: JSON object containing `name` and `category`.

Response: `"Added"`.

#### 3.2. DELETE `/product/{id}`

Delete a product by ID.

Response: JSON object of the deleted product.

#### 3.3. GET `/product/{id}`

Get a product by ID.

Response: JSON object of the requested product.

#### 3.4. GET `/product`

Get all products.

Response: JSON array of products.

### 4. Products by Category

#### 4.1. GET `/productByCategory/{id}`

Get all products in a category by ID.

Response: JSON array of products in the specified category.
