package endpoints

import (
	"awesomeProject/models"
	"github.com/golang-jwt/jwt"
	"github.com/labstack/echo/v4"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
	"net/http"
	"time"
)

func InitAuthEndpoints(database *gorm.DB, e *echo.Echo) {
	db = database
	e.POST("/register", RegisterUser)
	e.POST("/login", LoginUser)
}

// RegisterUser creates a new user account
func RegisterUser(c echo.Context) error {
	// Bind the request body to a new User instance
	user := new(models.User)
	if err := c.Bind(user); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid request payload")
	}

	// Hash the password
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(user.Password), bcrypt.DefaultCost)
	if err != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to hash password")
	}

	// Store the hashed password in the user object
	user.Password = string(hashedPassword)

	// Create the user in the database
	result := db.Create(user)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to create user")
	}

	// Remove the password field from the response
	user.Password = ""

	token := jwt.New(jwt.SigningMethodHS256)
	t, err := token.SignedString([]byte("your-secret-key"))

	// Return the created user as JSON response
	return c.JSON(http.StatusCreated, map[string]interface{}{
		"user":  user,
		"token": t,
	})
}

// LoginUser authenticates a user and returns a JWT token
func LoginUser(c echo.Context) error {
	// Bind the request body to a new UserLogin instance
	login := new(models.UserLogin)
	if err := c.Bind(login); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid request payload")
	}

	// Find the user in the database by email
	var user models.User
	result := db.Where("email = ?", login.Email).First(&user)
	if result.Error != nil {
		return echo.NewHTTPError(http.StatusUnauthorized, "Invalid email or password")
	}

	// Compare the provided password with the hashed password
	if err := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(login.Password)); err != nil {
		return echo.NewHTTPError(http.StatusUnauthorized, "Invalid email or password")
	}

	// Generate a JWT token
	token := jwt.New(jwt.SigningMethodHS256)

	// Set claims
	claims := token.Claims.(jwt.MapClaims)
	claims["user_id"] = user.ID
	claims["exp"] = time.Now().Add(time.Hour * 24).Unix()

	// Generate encoded token and send it as response
	t, err := token.SignedString([]byte("your-secret-key"))
	if err != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to generate token")
	}

	// Return the token as JSON response
	return c.JSON(http.StatusOK, map[string]string{
		"token": t,
	})
}
