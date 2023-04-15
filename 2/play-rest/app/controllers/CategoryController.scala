package controllers

import javax.inject._
import models.{CategoryItem, NewCategoryItem, PutCategoryItem}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import scala.collection.mutable

@Singleton
class CategoryController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  private var categoryList = new mutable.ListBuffer[CategoryItem]()
  categoryList += CategoryItem(1, "Electronics", "Electronic devices and gadgets\"")
  categoryList += CategoryItem(2, "Clothing", "Apparel, shoes, and accessories")
  categoryList += CategoryItem(3, "Books", "Books, magazines, and comics")
  categoryList += CategoryItem(4, "Home & Kitchen", "Home appliances, kitchenware, and furniture")
  categoryList += CategoryItem(5, "Toys & Games", "Toys, games, and puzzles for all ages")

  implicit val categoryItemJson = Json.format[CategoryItem]
  implicit val newCategoryItemJson = Json.format[NewCategoryItem]
  implicit val putCategoryItemJson = Json.format[PutCategoryItem]

  // curl localhost:9000/category
  def getAll(): Action[AnyContent] = Action {
    if (categoryList.isEmpty) NoContent else Ok(Json.toJson(categoryList))
  }

  // curl localhost:9000/category/1
  def getById(itemId: Long) = Action {
    val foundItem = categoryList.find(_.id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

  // curl -v -d '{"id": "1", "name": "name", "description": "some new item"}' -H 'Content-Type: application/json' -X PUT localhost:9000/category
  // id is obligatory, rest parameters are optional
  def put() = Action(parse.json) { implicit request =>
    request.body.validate[PutCategoryItem].fold(
      errors => {
        BadRequest(Json.obj("status" -> "Error", "message" -> JsError.toJson(errors)))
      },
      item => {
        categoryList.find(_.id == item.id) match {
          case Some(existingItem) =>
            val updatedItem = existingItem.copy(
              name = item.name.getOrElse(existingItem.name),
              description = item.description.getOrElse(existingItem.description)
            )
            categoryList = categoryList.filterNot(_.id == item.id)
            categoryList += updatedItem
            Accepted(Json.toJson(updatedItem))
          case None => NotFound
        }
      }
    )
  }

  // curl -X DELETE localhost:9000/category/1
  def deleteById(itemId: Long): Action[AnyContent] = Action {
    val foundItem = categoryList.find(_.id == itemId)
    foundItem match {
      case Some(item) =>
        categoryList.filterInPlace(_.id != itemId)
        Ok
      case None => NotFound
    }
  }

  // curl -v -d '{"name": "name", "description": "some new item"}' -H 'Content-Type: application/json' -X POST localhost:9000/category
  def addNewItem() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val categoryItem: Option[NewCategoryItem] = jsonObject.flatMap(Json.fromJson[NewCategoryItem](_).asOpt)

    categoryItem match {
      case Some(newItem) =>
        val nextId = categoryList.map(_.id).max + 1
        val toBeAdded = CategoryItem(nextId, newItem.name, newItem.description)
        categoryList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
  }
}
