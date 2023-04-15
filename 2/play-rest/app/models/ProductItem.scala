package models

case class ProductItem(id: Long, name: String, price: Float, category: Long)

case class PutProductItem(id: Long, name: Option[String], price: Option[Float], category: Option[Long])

case class NewProductItem(name: String, price: Float, category: Long)