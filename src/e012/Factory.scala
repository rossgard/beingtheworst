package e012

/**
 *
 */
trait Factory {

  trait Event

  class EmployeeAssignedToFactory(val employeeName: String) extends Event {
    override def toString = employeeName + " assigned to factory"
  }

}
