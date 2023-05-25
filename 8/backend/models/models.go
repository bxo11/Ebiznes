package models

// Product model
type Product struct {
	ID         uint     `gorm:"primaryKey"`
	Name       string   `gorm:"not null"`
	Price      uint     `gorm:"not null"`
	CategoryID uint     `gorm:"not null"`
	Category   Category `gorm:"foreignKey:CategoryID"`
}

// Category model
type Category struct {
	ID   uint   `gorm:"primaryKey"`
	Name string `gorm:"not null"`
}

// Cart model
type Cart struct {
	ID          uint      `gorm:"primaryKey"`
	Name        string    `gorm:"not null"`
	Description string    `gorm:"not null"`
	Products    []Product `gorm:"many2many:cart_products;"`
}

// User model
type User struct {
	ID       uint   `gorm:"primaryKey"`
	Email    string `gorm:"unique;not null"`
	Password string `gorm:"not null"`
}

// UserLogin model
type UserLogin struct {
	Email    string `json:"email" form:"email" query:"email" validate:"required,email"`
	Password string `json:"password" form:"password" query:"password" validate:"required"`
}
