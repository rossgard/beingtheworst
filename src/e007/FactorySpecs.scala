package e007

import org.scalatest.FunSuite

abstract class FactorySpecs extends FunSuite {

  var given: List[Event] = List.empty

  var when: () => Any = null

  def then(then: List[Event]): List[Event] = assertCustomerGWT(given, when, then)

  def thenException(exception : String) {
    try {
      execute(given, when)
      fail("Expected exception: " + exception)
    } catch {
      case e: Exception => assert(exception == e.getMessage)
    }
  }

  def assertCustomerGWT(given: List[Event], when: () => Any, then: List[Event]): List[Event] = {
    val changes = execute(given, when)

    if (Option(then).getOrElse(List()).isEmpty) println("Expect no events")
    else for (e <- then) yield println("Then: " + e)

    assertEquality(then, changes)

    changes
  }

  def execute(given: List[Event], when: () => Any): List[Event] = {
    if (given.isEmpty) println("Given no events")

    for (e <- given) yield println("Given: " + e)

    val customer = new FactoryAggregate(new FactoryState(given))
    println("Customer created")

    printWhen(when)
    
    //customer
    
    //customer.assignEmployeeToFactory("fry") // todo check how to use anonymous func

    customer.changes
  }

  def printWhen(when: () => Any) {
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