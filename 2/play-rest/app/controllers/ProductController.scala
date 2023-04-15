package controllers

import models.{ProductItem, NewProductItem, PutProductItem}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject._
import scala.collection.mutable

@Singleton
class ProductController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  private var productList = new mutable.ListBuffer[ProductItem]()
  productList += ProductItem(1, "Smartphone", 599.99f, 1)
  productList += ProductItem(2, "Laptop", 999.99f, 1)
  productList += ProductItem(3, "T-Shirt", 19.99f, 2)
  productList += ProductItem(4, "Jeans", 49.99f, 2)
  productList += ProductItem(5, "Fiction Novel", 14.99f, 3)
  productList += ProductItem(6, "Cookbook", 24.99f, 3)
  productList += ProductItem(7, "Blender", 89.99f, 4)
  productList += ProductItem(8, "Coffee Maker", 59.99f, 4)
  productList += ProductItem(9, "Board Game", 34.99f, 5)
  productList += ProductItem(10, "Action Figure", 19.99f, 5)

  implicit val ProductItemJson = Json.format[ProductItem]
  implicit val newProductItemJson = Json.format[NewProductItem]
  implicit val putProductItemJson = Json.format[PutProductItem]

  // curl localhost:9000/category
  def getAll(): Action[AnyContent] = Action {
    if (productList.isEmpty) NoContent else Ok(Json.toJson(productList))
  }

  // curl localhost:9000/category/1
  def getById(itemId: Long) = Action {
    val foundItem = productList.find(_.id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

  // curl -v -d '{"id": "1", "name": "name", "description": "some new item"}' -H 'Content-Type: application/json' -X PUT localhost:9000/category
  // id is obligatory, rest parameters are optional
  def put() = Action(parse.json) { implicit request =>
    request.body.validate[PutProductItem].fold(
      errors => {
        BadRequest(Json.obj("status" -> "Error", "message" -> JsError.toJson(errors)))
      },
      item => {
        productList.find(_.id == item.id) match {
          case Some(existingItem) =>
            val updatedItem = existingItem.copy(
              name = item.name.getOrElse(existingItem.name),
              price = item.price.getOrElse(existingItem.price),
              category = item.category.getOrElse(existingItem.category),
            )
            productList = productList.filterNot(_.id == item.id)
            productList += updatedItem
            Accepted(Json.toJson(updatedItem))
          case None => NotFound
        }
      }
    )
  }

  // curl -X DELETE localhost:9000/category/1
  def deleteById(itemId: Long): Action[AnyContent] = Action {
    val foundItem = productList.find(_.id == itemId)
    foundItem match {
      case Some(item) =>
        productList.filterInPlace(_.id != itemId)
        Ok
      case None => NotFound
    }
  }

  // curl -v -d '{"name": "name", "description": "some new item"}' -H 'Content-Type: application/json' -X POST localhost:9000/category
  def addNewItem() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val productItem: Option[NewProductItem] = jsonObject.flatMap(Json.fromJson[NewProductItem](_).asOpt)

    productItem match {
      case Some(newItem) =>
        val nextId = productList.map(_.id).max + 1
        val toBeAdded = ProductItem(nextId, newItem.name, newItem.price, newItem.category)
        productList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
  }
}
