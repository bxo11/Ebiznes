package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import java.math.BigInteger

fun Application.configureRouting() {
    data class Message(var id: Int, var userId: BigInteger, var message: String)
    data class Category(var id: Int, var name: String, var description: String)
    data class Product(var id: Int, var name: String, var category: Int)

    val categoryList = arrayListOf(
        Category(1, "Electronics", "Devices and gadgets such as smartphones, laptops, and cameras."),
        Category(2, "Clothing", "Apparel items including shirts, pants, dresses, and shoes."),
        Category(3, "Home & Kitchen", "Products for the home, including appliances, cookware, and furniture."),
        Category(4, "Health & Beauty", "Personal care items, cosmetics, and health supplements."),
        Category(5, "Sports & Outdoors", "Sports equipment, camping gear, and other outdoor activities."),
        Category(6, "Toys & Games", "Toys, board games, and other entertainment products for children and adults."),
        Category(7, "Automotive", "Car parts, accessories, and tools for vehicle maintenance."),
        Category(8, "Books", "Printed books, eBooks, and audiobooks across various genres."),
        Category(9, "Grocery", "Food and beverage items, including fresh produce, snacks, and drinks."),
        Category(10, "Office Supplies", "Stationery, office equipment, and other items for professional use.")
    )
    val productList = arrayListOf(
        Product(1, "Smartphone", 1),
        Product(2, "Laptop", 1),
        Product(3, "T-Shirt", 2),
        Product(4, "Jeans", 2),
        Product(5, "Blender", 3),
        Product(6, "Microwave", 3),
        Product(7, "Shampoo", 4),
        Product(8, "Vitamin C", 4),
        Product(9, "Basketball", 5),
        Product(10, "Tent", 5),
        Product(11, "Action Figure", 6),
        Product(12, "Board Game", 6),
        Product(13, "Car Battery", 7),
        Product(14, "Tire Pressure Gauge", 7),
        Product(15, "Mystery Novel", 8),
        Product(16, "Science Fiction eBook", 8),
        Product(17, "Organic Apples", 9),
        Product(18, "Granola Bars", 9),
        Product(19, "Stapler", 10),
        Product(20, "Printer Paper", 10)
    )
    val messageList = ArrayList<Message>()

    routing() {
        route("/message") {
            post {
                val message = call.receive<Message>()
                message.id = messageList.size + 1
                messageList.add(message)
                call.respond("Added")
            }
            get {
                call.respond(messageList)
            }
        }
        route("/category") {
            post {
                val category = call.receive<Category>()
                category.id = categoryList.size +1
                categoryList.add(category)
                call.respond("Added")
            }
            delete("/{id}") {
                call.respond(categoryList.removeAt(call.parameters["id"]!!.toInt()))
            }
            get("/{id}") {
                call.respond(categoryList[call.parameters["id"]!!.toInt() - 1])
            }
            get {
                call.respond(categoryList)
            }
        }
        route("/product") {
            post {
                val product = call.receive<Product>()
                product.id = categoryList.size +1
                productList.add(product)
                call.respond("Added")
            }
            delete("/{id}") {
                call.respond(productList.removeAt(call.parameters["id"]!!.toInt()))
            }
            get("/{id}") {
                call.respond(productList[call.parameters["id"]!!.toInt() - 1])
            }
            get {
                call.respond(productList)
            }
        }
        route("/productByCategory") {
            get("/{id}") {
                val filteredProducts = productList.filter { it.category == call.parameters["id"]!!.toInt() }
                call.respond(filteredProducts)
            }
        }
    }
}
