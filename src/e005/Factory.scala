package e005

trait Event

class FactoryAggregate(val state: FactoryState) {

  var changes: List[Event] = List.empty

  def assignEmployeeToFactory(employeeName: String) {
    printf("?> Command: Assign employee %s to factory\n", employeeName)

    if (state.listOfEmployeeNames.contains(employeeName)) {
      printf(":> FAIL: the name of '%s' only one employee can have\n", employeeName)
      return
    }
    if (employeeName == "bender") {
      println(":> FAIL: Guys with the name 'bender' are trouble.")
      return
    }

    doPaperWork("Assign employee to factory")

    recordThat(new EmployeeAssignedToFactory(employeeName))
  }

  def transferShipmentToCargoBay(shipmentName: String, parts: Array[CarPart]) {
    println("?> Command: transfer shipment to cargo bay")
    if (state.listOfEmployeeNames.isEmpty) {
      fail(":> FAIL: There has to be somebody at factory in order to accept shipments")
      return
    }

    if (parts.length == 0) fail(":> Empty shipments are not accepted!")

    if (state.shipmentsWaitingToBeUnloaded.size > 2) {
      fail(":> FAIL: More than two shipments can't fint into this cargo bay :(")
      return
    }

    doRealWork("opening cargo bay doors")
    recordThat(new ShipmentTransferredToCargoBay(shipmentName, parts))

    val totalCountOfParts = parts.foldLeft(0)(_ + _.quantity)

    if (totalCountOfParts > 10) {
      recordThat(new CurseWordUttered("Boltov tebe v korobky peredach", "awe in the face of the amount of parts delivered"))
    }
  }

  def fail(message: String, args: Array[String] = Array("")) {
    throw new IllegalArgumentException(String.format(message, args))
  }

  def doPaperWork(workName: String) {
    //printf("> Work: papers... %s...\n", workName)
    //Thread.sleep(1000)
  }

  def doRealWork(workName: String) {
    //printf("> Work: heavy stuff... %s...\n", workName)
    //Thread.sleep(1000)
  }

  def recordThat(e: Event) {
    changes = e :: changes
    state.mutate(e)
  }
}

class FactoryState(val changes : List[Event]) {
  
  var listOfEmployeeNames: List[String] = List.empty
  var shipmentsWaitingToBeUnloaded: List[Array[CarPart]] = List.empty

  private def announceInsideFactory(e: EmployeeAssignedToFactory) {
    listOfEmployeeNames = e.employeeName :: listOfEmployeeNames 
  }

  private def announceInsideFactory(e: ShipmentTransferredToCargoBay) {
    shipmentsWaitingToBeUnloaded = e.parts :: shipmentsWaitingToBeUnloaded 
    println("Shipments waiting to be unloaded: " + shipmentsWaitingToBeUnloaded.length)
  }

  private def announceInsideFactory(e: CurseWordUttered) {}

  def mutate(event: Event) {
    event match { // TODO make use of dynamic
      case employeeAssignedToFactory: EmployeeAssignedToFactory => announceInsideFactory(employeeAssignedToFactory)
      case shipmentTransferredToCargoBay: ShipmentTransferredToCargoBay => announceInsideFactory(shipmentTransferredToCargoBay)
      case curseWordUttered: CurseWordUttered => announceInsideFactory(curseWordUttered)
    }
    printf("!> Event: %s\n", event)
  }
}


class CarPart(val name: String, val quantity: Int) {
  override def toString = name + " " + quantity
}

class EmployeeAssignedToFactory(val employeeName: String) extends Event {
  override def toString = employeeName + " assigned to factory"
}

class ShipmentTransferredToCargoBay(val shipmentName: String, val parts: Array[CarPart]) extends Event {
  printf("Shipment '%s' transferred to cargo bay \n", shipmentName)

  override def toString = parts mkString " pcs "
}

class CurseWordUttered(val curseWord: String, val meaning: String) extends Event {
  override def toString = String.format("'%s' was heard within the walls. It meant:\r\n    '%s'", curseWord, meaning)
}

