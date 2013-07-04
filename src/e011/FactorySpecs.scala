package e011

import org.scalatest.FunSuite

abstract class FactorySpecs extends FunSuite {

  val f : FactoryAggregate = new FactoryAggregate(new FactoryState(List.empty))

  var given: List[Event] = List.empty

  var when: Unit = Nil

  def then(then: List[Event]): List[Event] = assertCustomerGWT(given, when, then)

  def thenException(exception : String) {
    try {
      executeTest(given, when)
      fail("Expected exception: " + exception)
    } catch {
      case e: Exception => assert(exception == e.getMessage)
    }
  }

  def assertCustomerGWT(given: List[Event], when: Unit, then: List[Event]): List[Event] = {
    val changes = executeTest(given, when)

    if (Option(then).getOrElse(List()).isEmpty) println("Expect no events")
    else for (e <- then) yield println("Then: " + e)

    assertEquality(then, changes)

    changes
  }

  def executeTest(given: List[Event], when: Unit): List[Event] = {
    if (given.isEmpty) println("Given no events")

    for (e <- given) yield println("Given: " + e)

    val customer = new FactoryAggregate(new FactoryState(given))
    println("Customer created")

    printWhen(when)

    when

    customer.changes
  }

  def printWhen(when: Unit) {
    println("")
    println("When " + when)
    println("")
  }

  def assertEquality(expected: List[Event], actual: List[Event]) {
    println("Expected " + expected)
    println("Actual " + actual)

    assert(expected == actual)
  }
}