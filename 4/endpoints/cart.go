package endpoints

import (
	"awesomeProject/models"
	"github.com/labstack/echo/v4"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

const cartEndpoint = "/carts"
const cartIdEndpoint = "/carts/:id"
const invalidCartMessage = "Invalid cart ID"

// Get a cart by ID
func getCart(c echo.Context) error {
	// Get the cart ID from the request parameters
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, invalidCartMessage)
	}

	// Find the cart in the database
	var cart models.Cart
	result := db.Preload("Products").First(&cart, id)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusNotFound, "Cart not found")
	}

	// Return the cart as JSON response
	return c.JSON(http.StatusOK, cart)
}

// Create a new cart
func createCart(c echo.Context) error {
	// Bind the request body to a new Cart instance
	cart := new(models.Cart)
	if err := c.Bind(cart); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid request payload")
	}

	// Create the cart in the database
	result := db.Create(cart)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to create cart")
	}

	// Return the created cart as JSON response
	return c.JSON(http.StatusCreated, cart)
}

// Update an existing cart
func updateCart(c echo.Context) error {
	// Get the cart ID from the request parameters
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, invalidCartMessage)
	}

	// Find the cart in the database
	var cart models.Cart
	result := db.First(&cart, id)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusNotFound, "Cart not found")
	}

	// Bind the request body to the existing cart
	if err := c.Bind(&cart); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid request payload")
	}

	// Save the updated cart in the database
	result = db.Save(&cart)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to update cart")
	}

	// Return the updated cart as JSON response
	return c.JSON(http.StatusOK, cart)
}

// Delete a cart
func deleteCart(c echo.Context) error {
	// Get the cart ID from the request parameters
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, invalidCartMessage)
	}

	// Delete the cart from the database
	result := db.Delete(&models.Cart{}, id)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to delete cart")
	}

	// Return a success message
	return c.NoContent(http.StatusOK)
}

// Init initializes the endpoints with the given database connection
func InitCartEndpoints(database *gorm.DB, e *echo.Echo) {
	db = database

	// Define the routes

	e.GET(cartIdEndpoint, getCart)
	e.POST(cartEndpoint, createCart)
	e.PUT(cartIdEndpoint, updateCart)
	e.DELETE(cartIdEndpoint, deleteCart)
}
