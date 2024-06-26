package main

import (
	"awesomeProject/endpoints"
	"awesomeProject/models"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
	"log"
)

func main() {
	// Connect to the SQLite database
	db, err := gorm.Open(sqlite.Open("gorm.db"), &gorm.Config{})
	if err != nil {
		log.Fatal(err)
	}

	// Auto-migrate the Product model
	db.AutoMigrate(&models.User{})
	db.AutoMigrate(&models.Category{})
	db.AutoMigrate(&models.Product{})
	db.AutoMigrate(&models.Cart{})

	// Create a new Echo instance
	e := echo.New()

	e.Use(middleware.CORSWithConfig(middleware.CORSConfig{
		AllowOrigins: []string{"*"},
		AllowHeaders: []string{echo.HeaderContentType, echo.HeaderAuthorization},
	}))

	e.Use(middleware.JWTWithConfig(middleware.JWTConfig{
		SigningMethod: "HS256",
		SigningKey:    []byte("your-secret-key"),
		Skipper: func(c echo.Context) bool {
			// Skip authentication for specific endpoints
			if c.Path() == "/login" || c.Path() == "/register" {
				return true
			}
			return false
		},
	}))

	// Initialize the endpoints with the database connection
	endpoints.InitAuthEndpoints(db, e)
	endpoints.InitProductEndpoints(db, e)
	endpoints.InitCartEndpoints(db, e)

	// Start the server
	e.Start(":8080")
}
