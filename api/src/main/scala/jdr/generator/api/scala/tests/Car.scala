package jdr.generator.api.scala.tests

// On défini un objet abstrait
trait AvgSpeed {
  var name: String
  def calcAvgSpeed(distKm: Double, timeHours: Double): Unit = {
    println(s"Nous avons une distance de $distKm kilomètres à parcourir en $timeHours heures")
    val avgSpeed = distKm / timeHours
    println(s"Un véhicule $name devra donc rouler à une vitesse moyenne de $avgSpeed km/h")
  }
}

// dont la classe Car va étendre
class Car(vehicleName: String = "inconnue") extends AvgSpeed {
  // name n'étant pas défini dans le trait AvgSpeed, on va lui indiquer que le
  // paramètre vehicleName de notre classe est la valeur a utiliser
  override var name: String = vehicleName

  // On crée une méthode appelant le constructeur this vide, qui définira que
  // l'instanciation de la classe new Car sans paramètre vehicleName défini
  // aura une valeur par défaut "sans marque", qui écrasera la valeur "inconnue"
  // indiquée par défaut dans le constructeur (valeur "inconnue" de facto inutile).
  def this() = this("sans marque")
}

// Création d'un objet compagnon (portant le meme nom que la classe Car) devant
// être défini dans le meme fichier source. La création d'un objet singleton
// n'est pas initialisé avant qu'un code n'y accède, et ne peuvent pas accepter
// de paramètres et sont en fait implémentés en tant qu'instance d'une classe synthétique.
object Car {
  private val voiture = new Car("Citroen C6")
  private val voitureConso = new CarConso(72,10,8.5,11.2,10.0)
  private val voitureDist = new CarDist(72d,10d,8.5,11.2,10.0)

  def main(args: Array[String]): Unit = {
    // Tests de notations INFIX sur des méthodes intégrées et personnalisées :

    new Car calcAvgSpeed (100, 2)
    // On instancie un nouvel objet Car, qui est une instance de la classe Car dans le corps de l'objet compagnon
    // Cet objet est en fait une instance de la classe compagnon Car, et non de l'objet compagnon.

    voiture calcAvgSpeed (200, 2)
    // voiture est aussi un objet singleton instanciant la classe "Car".

    voitureConso kmParPlein "Mixte"
    // On instancie d'autres classes dans cet objet compagnon "Car" ...
    voitureDist kmParPlein "Route"
    voitureDist kmParPlein "urbain"
    // ... CarConso et CarDist, permettant de définir plus concrètement l'objet en question
  }
}
