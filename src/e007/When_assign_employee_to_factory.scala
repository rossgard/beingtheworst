package e007
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class When_assign_employee_to_factory extends FactorySpecs {

  val f: FactoryAggregate = new FactoryAggregate(new FactoryState(List.empty))

  test("empty factory") {
    // given no events
    when = () => f.assignEmployeeToFactory("fry")
    then(List(new EmployeeAssignedToFactory("fry")))
  }

  test("fry is assigned to factory") {
    given = List(new EmployeeAssignedToFactory("fry"))
    when = () => f.assignEmployeeToFactory("fry")
    thenException("only one employee can have")
  }

  test("bender comes to empty factory") {
    // given no events
    when = () => f.assignEmployeeToFactory("bender")
    thenException("Guys with name 'bender' are trouble")
  }
}