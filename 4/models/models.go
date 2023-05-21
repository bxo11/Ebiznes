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
