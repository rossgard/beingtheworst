package e004

import scala.collection.mutable.ListBuffer

object Program extends App {

  class FactoryImplementation1 {

    def assignEmployeeToFactory(employeeName: String) {}
    def transferShipmentToCargoBay(shipmentName: String, parts: Array[CarPart]) {}
    def unloadShipmentFromCargoBay(employeeName: String, carModel: String) {}
    def produceCar(employeeName: String, carModel: String) {}
  }

  class FactoryImplementation2 {
    def assignEmployeeToFactory(employeeName: String) {
      // CheckIfEmployeeCanBeAssignedToFactory(employeeName)
      // DoPaperWork
      // RecordThatEmployeeAssignedToFactory(employeeName)
    }

    def transferShipmentToCargoBay(shipmentName: String, parts: Array[CarPart]) {
      // CheckIfCargoBayHasFreeSpace(parts)
      // DoRealWork("unloading supplies...")
      // DoPaperWork("Signing the shipment acceptance form")
      // RecordThatSuppliesAreAvailableInCargoBay()
    }

    def unloadShipmentFromCargoBay(employeeName: String, carModel: String) {
      // DoRealWork("passing supplies")
      // RecordThatSuppliesWereUnloadedFromCargoBay
    }

    def produceCar(employeeName: String, carModel: String) {
      // CheckIfWeHaveEnoughSpareParts
      // CheckIfEmployeeIsAvailable
      // DoRealWork
      // RecordThatCarWasProduced
    }
  }

  class FactoryImplementation3 {

    val journalOfFactoryEvents: ListBuffer[Event] = ListBuffer.empty
    val outListOfEmployeeNames: ListBuffer[String] = ListBuffer.empty
    val shipmentsWaitingToBeUnloaded: ListBuffer[Array[CarPart]] = ListBuffer.empty

    def assignEmployeeToFactory(employeeName: String) {
      printf("?> Command: Assign employee %s to factory\n", employeeName)

      if (outListOfEmployeeNames.contains(employeeName)) {
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
      if (outListOfEmployeeNames.isEmpty) {
        println(":> FAIL: There has to be somebody at factory in order to accept shipments")
        return
      }

      if (shipmentsWaitingToBeUnloaded.size > 2) {
        println(":> FAIL: More than two shipments can't fint into this cargo bay :(")
        return
      }

      doRealWork("opening cargo bay doors")
      recordThat(new ShipmentTransferredToCargoBay(shipmentName, parts))

      val totalCountOfParts = parts.foldLeft(0)(_ + _.quantity)

      if (totalCountOfParts > 10) {
        recordThat(new CurseWordUttered("Boltov tebe v korobky peredach", "awe in the face of the amount of parts delivered"))
      }
    }

    def doPaperWork(workName: String) {
      printf("> Work: papers... %s...\n", workName)
      Thread.sleep(1000)
    }

    def doRealWork(workName: String) {
      printf("> Work: heavy stuff... %s...\n", workName)
      Thread.sleep(1000)
    }

    def recordThat(e: Event) {
      journalOfFactoryEvents += e
      
      e match { // TODO make use of dynamic
        case employeeAssignedToFactory: EmployeeAssignedToFactory => announceInsideFactory(employeeAssignedToFactory)
        case shipmentTransferredToCargoBay: ShipmentTransferredToCargoBay => announceInsideFactory(shipmentTransferredToCargoBay)
        case curseWordUttered: CurseWordUttered => announceInsideFactory(curseWordUttered)
      }
      printf("!> Event: %s\n", e)
    }

    def announceInsideFactory(e: EmployeeAssignedToFactory) {
      outListOfEmployeeNames += e.employeeName
    }

    def announceInsideFactory(e: ShipmentTransferredToCargoBay) {
      shipmentsWaitingToBeUnloaded += e.parts
      println("Shipments waiting to be unloaded: " + shipmentsWaitingToBeUnloaded.length)
    }

    def announceInsideFactory(e: CurseWordUttered) {}

  }

  class CarPart(val name: String, val quantity: Int) {
    override def toString = name + " " + quantity
  }

  trait Event {

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

  override def main(args: Array[String]) {
    println("A new day at the factory starts...\r\n")
    
    val factory = new FactoryImplementation3

    factory.transferShipmentToCargoBay("chassis", Array(new CarPart("chassis", 4)))

    factory.assignEmployeeToFactory("yoda")
    factory.assignEmployeeToFactory("luke")
    factory.assignEmployeeToFactory("yoda")
    factory.assignEmployeeToFactory("bender")

    factory.transferShipmentToCargoBay("model T spare parts", Array(
      new CarPart("wheels", 20),
      new CarPart("engine", 7),
      new CarPart("bits and pieces", 2)))
  

  println("\r\nIt's the end of the day. Let's read our journal of events once more:\r\n");
  println("\r\nWe should only see events below that were actually allowed to be recorded.\r\n");
  
  factory.journalOfFactoryEvents foreach (printf("!> %s\n",_))
  
  println("\r\nIt seems, this was an interesting day! Two Yoda's there should be not!")
  
  }

}