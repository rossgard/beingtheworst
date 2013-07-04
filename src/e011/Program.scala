package e011


class Program extends FactorySpecs {

  /**
   * Employee policies
   */

    test("empty factory allows any employee not bender to be assigned") {
      when = (x : FactoryAggregate) => f.assignEmployeeToFactory("fry")
      then(List(new EmployeeAssignedToFactory("fry")))
    }

    test("duplicate employee bane is assigned but not allowed") {
      given = List(new EmployeeAssignedToFactory("fry"))
      when = f.assignEmployeeToFactory("fry")
      thenException("only one employee can have")
    }

    test("no employee named bender is allowed to be assigned") {
      when = f.assignEmployeeToFactory("bender")
      thenException("Guys with the name bender are trouble")
    }

  /**
   * Shipment policies
   */

    test("a shipment received announcement is made with correct car parts list") {
      given = List(new EmployeeAssignedToFactory("yoda"))
      when = f.receiveShipmentInCargoBay("shipment-777", Array[CarPart](new CarPart("engine", 1)))
      then(List(new ShipmentReceivedInCargoBay("shipment-777", Array[CarPart](new CarPart("engine", 1)))))
    }

  test("empty shipment is not allowed") {
    given = List(new EmployeeAssignedToFactory("yoda"))
    when = f.receiveShipmentInCargoBay("shipment-777", Array.empty)
    thenException("Empty shipments are not accepted!")
  }

  test("there are already two shipments in cargo bay so no new shipments allowed") {
    given = List(
      new ShipmentReceivedInCargoBay("shipment-777", Array[CarPart](new CarPart("engine", 1))),
      new ShipmentReceivedInCargoBay("shipment-778", Array[CarPart](new CarPart("engine", 1))))
    when = f.receiveShipmentInCargoBay("shipment-779", Array[CarPart](new CarPart("engine", 1)))
    thenException("More than two shipments can't fit")
  }

  /**
   * Inventory policies
   */







}
