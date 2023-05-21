1. ```sql
   INSERT INTO categories (name) VALUES
   ('Category 1'),
   ('Category 2'),
   ('Category 3'),
   ('Category 4'),
   ('Category 5');
   ```
2. ```sql
   INSERT INTO products (name, price, category_id) VALUES
    ('Product 1', 10, 1),
    ('Product 2', 20, 2),
    ('Product 3', 30, 1),
    ('Product 4', 15, 3),
    ('Product 5', 25, 2),
    ('Product 6', 40, 1),
    ('Product 7', 12, 4),
    ('Product 8', 18, 5),
    ('Product 9', 22, 4),
    ('Product 10', 35, 3);
   ```

# API Endpoints
## Get Product by ID

- **Endpoint**: `/products/:id`
- **Method**: `GET`
- **Description**: Retrieve a product by its ID.
- **Example**: `GET /products/1`
- **Response**:
  ```json
  {
    "ID": 1,
    "Name": "Product 1",
    "Price": 10
  }
  ```

## Get All Products

- **Endpoint**: `/products`
- **Method**: `GET`
- **Description**: Retrieve all products.
- **Example**: `GET /products`
- **Response**:
  ```json
  [
    {
      "ID": 1,
      "Name": "Product 1",
      "Price": 10
    },
    {
      "ID": 2,
      "Name": "Product 2",
      "Price": 20
    },
    ...
  ]
  ```

## Create Product

- **Endpoint**: `/products`
- **Method**: `POST`
- **Description**: Create a new product.
- **Example**: `POST /products`
  - Request Body:
    ```json
    {
      "Name": "New Product",
      "Price": 15
    }
    ```
- **Response**:
  ```json
  {
    "ID": 11,
    "Name": "New Product",
    "Price": 15
  }
  ```

## Update Product

- **Endpoint**: `/products/:id`
- **Method**: `PUT`
- **Description**: Update an existing product.
- **Example**: `PUT /products/1`
  - Request Body:
    ```json
    {
      "Name": "Updated Product",
      "Price": 12
    }
    ```
- **Response**:
  ```json
  {
    "ID": 1,
    "Name": "Updated Product",
    "Price": 12
  }
  ```

## Delete Product

- **Endpoint**: `/products/:id`
- **Method**: `DELETE`
- **Description**: Delete a product.
- **Example**: `DELETE /products/1`
- **Response**: `204 No Content`

Apologies for the oversight. Here's the updated Markdown file that includes the missing endpoints for the cart's products:

```markdown
# API Endpoints

## Get Cart by ID

- **Endpoint**: `/carts/:id`
- **Method**: `GET`
- **Description**: Retrieve a cart by its ID.
- **Example**: `GET /carts/1`
- **Response**:
  ```json
  {
    "ID": 1,
    "Name": "Cart 1",
    "Description": "Example cart",
    "Products": [
      {
        "ID": 1,
        "Name": "Product 1",
        "Price": 10
      },
      {
        "ID": 2,
        "Name": "Product 2",
        "Price": 20
      }
    ]
  }
  ```

## Create Cart

- **Endpoint**: `/carts`
- **Method**: `POST`
- **Description**: Create a new cart.
- **Example**: `POST /carts`
  - Request Body:
    ```json
    {
      "Name": "New Cart",
      "Description": "Example cart",
      "Products": [
        {
          "ID": 1,
          "Name": "Product 1",
          "Price": 10
        },
        {
          "ID": 2,
          "Name": "Product 2",
          "Price": 20
        }
      ]
    }
    ```
- **Response**:
  ```json
  {
    "ID": 2,
    "Name": "New Cart",
    "Description": "Example cart",
    "Products": [
      {
        "ID": 1,
        "Name": "Product 1",
        "Price": 10
      },
      {
        "ID": 2,
        "Name": "Product 2",
        "Price": 20
      }
    ]
  }
  ```

## Update Cart

- **Endpoint**: `/carts/:id`
- **Method**: `PUT`
- **Description**: Update an existing cart.
- **Example**: `PUT /carts/1`
  - Request Body:
    ```json
    {
      "Name": "Updated Cart",
      "Description": "Updated example cart",
      "Products": [
        {
          "ID": 1,
          "Name": "Product 1",
          "Price": 10
        }
      ]
    }
    ```
- **Response**:
  ```json
  {
    "ID": 1,
    "Name": "Updated Cart",
    "Description": "Updated example cart",
    "Products": [
      {
        "ID": 1,
        "Name": "Product 1",
        "Price": 10
      }
    ]
  }
  ```

## Delete Cart

- **Endpoint**: `/carts/:id`
- **Method**: `DELETE`
- **Description**: Delete a cart.
- **Example**: `DELETE /carts/1`
- **Response**: `204 No Content`

