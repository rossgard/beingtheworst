package e012

class FactoryState {

  var listOfEmployeeNames: List[String] = List.empty

  def when(e : EmployeeAssignedToFactory) {
    listOfEmployeeNames += e.name
  }


}
