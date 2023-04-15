package models

case class CategoryItem(id: Long, name: String, description: String)

case class PutCategoryItem(id: Long, name: Option[String], description: Option[String])

case class NewCategoryItem(name: String, description: String)