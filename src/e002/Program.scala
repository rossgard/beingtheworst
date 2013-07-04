package e002

import collection.mutable.Queue

object Program extends App {

  val basket = new ProductBasket

  // normal block method calls
  basket.addProduct("butter", 1)
  basket.addProduct("pepper", 2)

  // create a new message to hold the arguments of 5 candles to be added to the basket
  val message = new AddProductToBasketMessage("candles", 5)

  // send message to product basket to be handled
  applyMessage(basket, message)

  // create more AddProductToBasketMessage's and put them in a queue for processing later
  val queue: Queue[AddProductToBasketMessage] = Queue.empty

  queue.enqueue(new AddProductToBasketMessage("Chablis wine", 1))
  queue.enqueue(new AddProductToBasketMessage("shrimps", 10))

  queue foreach (message => println("Message in Queue is: " + message))

  while (!queue.isEmpty) applyMessage(basket, queue.dequeue)

  def applyMessage(basket: ProductBasket, message: Object) {    
    basket.when(message)
  }
  

  class ProductBasket {

    val products: Map[String, Double] = Map.empty withDefaultValue (0)

    def addProduct(name: String, quantity: Int) {
      products.updated(name, products(name) + quantity)
      printf("Product basket: I added %s unit(s) of '%s' \n", quantity, name)
    }

    def when(toBasketMessage: Object) = toBasketMessage match { // TODO: Use dynamic
      case addProductToBasketMessage: AddProductToBasketMessage => addProduct(addProductToBasketMessage.name, addProductToBasketMessage.quantity)
    }

    def getProductTotals: Map[String, Double] = products
  }

  @scala.serializable
  class AddProductToBasketMessage(val name: String, val quantity: Int) {

    override def toString: String = "Add %s %s to basket".format(quantity, name)
  }
}