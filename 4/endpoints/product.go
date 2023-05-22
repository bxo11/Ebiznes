package endpoints

import (
	"awesomeProject/models"
	"fmt"
	"github.com/labstack/echo/v4"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

const productEndpoint = "products"
const invalidProductMessage = "Invalid product ID"

var db *gorm.DB

// Init initializes the endpoints with the given database connection
func InitProductEndpoints(database *gorm.DB, e *echo.Echo) {
	db = database

	// Define the routes
	e.GET(fmt.Sprintf("/%s", productEndpoint), getAllProducts) // New route to get all products
	e.GET(fmt.Sprintf("/%s/:id", productEndpoint), getProduct)
	e.POST(fmt.Sprintf("/%s", productEndpoint), createProduct)
	e.PUT(fmt.Sprintf("/%s/:id", productEndpoint), updateProduct)
	e.DELETE(fmt.Sprintf("/%s/:id", productEndpoint), deleteProduct)
}

// Get a product by ID
func getProduct(c echo.Context) error {
	// Get the product ID from the request parameters
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, invalidProductMessage)
	}

	// Find the product in the database
	var product models.Product
	result := db.Preload("Category").First(&product, id)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusNotFound, "Product not found")
	}

	// Return the product as JSON response
	return c.JSON(http.StatusOK, product)
}

// Get all products
func getAllProducts(c echo.Context) error {
	// Find all products in the database
	var products []models.Product
	result := db.Preload("Category").Find(&products)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to retrieve products")
	}

	// Return the products as JSON response
	return c.JSON(http.StatusOK, products)
}

// Create a new product
func createProduct(c echo.Context) error {
	// Bind the request body to a new Product instance
	product := new(models.Product)
	if err := c.Bind(product); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid request payload")
	}

	// Create the product in the database
	result := db.Create(product)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to create product")
	}

	// Return the created product as JSON response
	return c.JSON(http.StatusCreated, product)
}

// Update an existing product
func updateProduct(c echo.Context) error {
	// Get the product ID from the request parameters
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, invalidProductMessage)
	}

	// Find the product in the database
	var product models.Product
	result := db.First(&product, id)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusNotFound, "Product not found")
	}

	// Bind the request body to the existing product
	if err := c.Bind(&product); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid request payload")
	}

	// Save the updated product in the database
	result = db.Save(&product)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to update product")
	}

	// Return the updated product as JSON response
	return c.JSON(http.StatusOK, product)
}

// Delete a product
func deleteProduct(c echo.Context) error {
	// Get the product ID from the request parameters
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, invalidProductMessage)
	}

	// Delete the product from the database
	result := db.Delete(&models.Product{}, id)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to delete product")
	}

	// Return a success message
	return c.NoContent(http.StatusOK)
}
