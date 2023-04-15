package models

case class OrderItem(id: Long, userId: Long, product: Long)

case class PutOrderItem(id: Long, userId: Option[Long], product: Option[Long])

case class NewOrderItem(id: Long, userId: Long, product: Long)