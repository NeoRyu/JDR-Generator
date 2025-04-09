package jdr.generator.api.scala.tests

trait AvgSpeed {
  var name: String
  def calcAvgSpeed(distKm: Double, timeHours: Double): Unit = {
    println(s"Nous avons une distance de $distKm kilomètres à parcourir en $timeHours heures")
    val avgSpeed = distKm / timeHours
    println(s"Un véhicule $name devra donc rouler à une vitesse moyenne de $avgSpeed km/h")
  }
}

class Car(vehicleName: String) extends AvgSpeed {
  override var name: String = vehicleName
}

object CitroenC6 {
  private val honda = new Car("Citroen C6")
  private val hondaConso = new CarConso(72,10,8.5,11.2,10.0)
  private val hondaDist = new CarDist(72d,10d,8.5,11.2,10.0)

  def main(args: Array[String]): Unit = {
    // Tests de notations INFIX sur des méthodes intégrées et personnalisées
    honda calcAvgSpeed (200, 2)
    hondaConso kmParPlein "Mixte"
    hondaDist kmParPlein "Route"
    hondaDist kmParPlein "urbain"
  }
}
