package e007
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class When_transfer_shipment_to_cargo_bay extends FactorySpecs {

  val f: FactoryAggregate = new FactoryAggregate(new FactoryState(List.empty))

  test("Empty shipment") {
    given = List(new EmployeeAssignedToFactory("yoda"))
    when = () => f.transferShipmentToCargoBay("some shipment", Array[CarPart]())
    thenException("Empty shipments are not accepted!")
  }

  test("Empty shipment and no workers at the factory") {
    // given no events
    when = () => f.assignEmployeeToFactory("fry")
    then(List[Event](new EmployeeAssignedToFactory("fry")))
  }

  test("There already are two shipments") {
    given = List(
      new EmployeeAssignedToFactory("chubakka"),
      new ShipmentTransferredToCargoBay("shipmt-11", Array(new CarPart("engine", 3))),
      new ShipmentTransferredToCargoBay("shipmt-12", Array(new CarPart("wheels", 40))))

    when = () => f.transferShipmentToCargoBay("shipmnt-13", Array(new CarPart("bmw6", 20)))
    thenException("More than two shipments can't fit")
  }
}