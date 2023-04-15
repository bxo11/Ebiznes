package controllers

import models.{OrderItem, NewOrderItem, PutOrderItem}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject._
import scala.collection.mutable

@Singleton
class OrderController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  private var orderList = new mutable.ListBuffer[OrderItem]()
  orderList += OrderItem(1, 1, 1)
  orderList += OrderItem(2, 1, 2)
  orderList += OrderItem(3, 1, 3)
  orderList += OrderItem(4, 1, 4)
  orderList += OrderItem(5, 2, 1)

  implicit val OrderItemJson = Json.format[OrderItem]
  implicit val newOrderItemJson = Json.format[NewOrderItem]
  implicit val putOrderItemJson = Json.format[PutOrderItem]

  // curl localhost:9000/order/user/1
  def getOrderForUser(userId: Long): Action[AnyContent] = Action {
    val foundItems = orderList.filter(_.userId == userId)
    if (foundItems.nonEmpty) {
      Ok(Json.toJson(foundItems))
    } else {
      NotFound
    }
  }

  // curl localhost:9000/order/1
  def getById(itemId: Long) = Action {
    val foundItem = orderList.find(_.id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

  // curl -v -d '{"id": "1", "name": "name", "description": "some new item"}' -H 'Content-Type: application/json' -X PUT localhost:9000/order
  // id is obligatory, rest parameters are optional
  def put() = Action(parse.json) { implicit request =>
    request.body.validate[PutOrderItem].fold(
      errors => {
        BadRequest(Json.obj("status" -> "Error", "message" -> JsError.toJson(errors)))
      },
      item => {
        orderList.find(_.id == item.id) match {
          case Some(existingItem) =>
            val updatedItem = existingItem.copy(
              userId = item.userId.getOrElse(existingItem.userId),
              product = item.product.getOrElse(existingItem.product)
            )
            orderList = orderList.filterNot(_.id == item.id)
            orderList += updatedItem
            Accepted(Json.toJson(updatedItem))
          case None => NotFound
        }
      }
    )
  }

  // curl -X DELETE localhost:9000/order/1
  def deleteById(itemId: Long): Action[AnyContent] = Action {
    val foundItem = orderList.find(_.id == itemId)
    foundItem match {
      case Some(item) =>
        orderList.filterInPlace(_.id != itemId)
        Ok
      case None => NotFound
    }
  }

  // curl -v -d '{"name": "name", "description": "some new item"}' -H 'Content-Type: application/json' -X POST localhost:9000/order
  def addNewItem() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val orderItem: Option[NewOrderItem] = jsonObject.flatMap(Json.fromJson[NewOrderItem](_).asOpt)

    orderItem match {
      case Some(newItem) =>
        val nextId = orderList.map(_.id).max + 1
        val toBeAdded = OrderItem(nextId, newItem.userId, newItem.product)
        orderList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
  }
}
