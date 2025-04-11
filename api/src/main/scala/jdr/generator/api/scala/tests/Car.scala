package jdr.generator.api.scala.tests

trait osef {
  def lol(): Unit = {
    println("test reussi d'implémentation de ce trait inutile avec 'with'")
  }

  val y: Int = 5 // variable scope y
  val multiply: (Int, Int) => Int = (x:Int, z:Int) => x * (if (z == 0) y else z) // la closure est capable d'accéder a la variable scope y
  val x10: Int => Int = multiply(10, _:Int) // Fonction partiellement appliquée
  def curry(a:Int): Int => Int = (b:Int) => multiply(a, b) // Fonction curry recevant d'abord un parametre, puis attendant de recevoir le second pour executer multiply

}

// On défini un objet abstrait
trait AvgSpeed {
  var name: String
  def calcAvgSpeed(distKm: Double, timeHours: Double): Unit = {
    println("----------------------------------------------------------------------------")
    println(s"Nous avons une distance de $distKm kilomètres à parcourir en $timeHours heures")
    val avgSpeed = distKm / timeHours
    println(s"Un véhicule $name devra donc rouler à une vitesse moyenne de $avgSpeed km/h")
  }
}

// dont la super-classe Vehicle va étendre AvgSpeed
class Vehicle(vehicleName: String = "inconnu") extends AvgSpeed {
  // name n'étant pas défini dans le trait AvgSpeed, on va lui indiquer que le
  // paramètre vehicleName de notre classe est la valeur à utiliser
  // Si aucune valeur, ce sera "inconnu" qui servira de name
  override var name: String = vehicleName
}

// et la sous-classe Car va étendre la superclasse Vehicle et avoir un autre héritage multiple, celui du trait osef
class Car(test: String = "undefined") extends Vehicle(test) with osef {
  // On crée une méthode appelant le constructeur this vide, qui définira que
  // l'instanciation de la classe new Car sans paramètre vehicleName défini
  // aura une valeur par défaut "sans marque", qui écrasera la valeur "undefined"
  // indiquée par défaut dans le constructeur (valeur "undefined" de facto inutile).
  def this() = this("sans marque")

  def vroom(): Unit = {
    println("Vroom vroom...")
    lol() // on appel la methode défini dans le trait osef renvoyant une chaine inutile
    println(multiply(5,0)) // on affiche le résultat du field multiply contenant une fonction anonyme multipliant un entier par 5 (si la deuxieme valeur est 0) : 5*(5)=25
    println(multiply(6,1)) // on affiche le résultat du field multiply contenant une fonction anonyme multipliant les deux entiers : 6*1=6
    println(x10(7)) // on affiche le résultat du field x10 contenant une fonction partiellement appliquée multipliant  un entier (ici 7) par 10 en appelant le champs multiply : 10*7=70
    println(curry(2)(8))// on affiche le résultat d'une fonction curry multipliant les deux entiers en appelant le champs multiply : 2*8=16
  }

  def calcTotalCost(nbCar: Int, model: String): Unit = {
    val taxe = 1.2; // TVA de 20%
    val taxePercent: String = "(TVA " + (taxe * 100 - 100) + "% incl.)" // Il s'agit d'une pure function, accessible n'importe ou dans cette methode
    val cost = Map(
      "Citroen C6" -> 50450.51,
      "Citroen C3" -> 15750.43,
      "Citroën Traction 15 Six Cabriolet" -> 612440.0
    ) // database de model/prix
    val subTotal = (qte: Int) => {
      qte * cost(model)
    } // calcul du sous-total quantité x prix du model
    val calcTva = (sub: Double) => {
      sub * taxe
    } // calcul sous total avec tva appliquée

    // TEST DE FONCTIONS COMPOSEES :
    def total1(qteCar: Int = nbCar) = (calcTva compose subTotal)(qteCar)
    // compose appel d'abord la dernière fonction subTotal (et utiliser nbCar) avant d'appeler la premiere (et utiliser subTotal comme param)
    val total2 = (subTotal andThen calcTva)(nbCar)
    //andThen va d'abord appeler la premiere methode subTotal (et utiliser nbCar) avant d'appeler la suivante (et utiliser subTotal comme param)
    if (total1(nbCar) == total2) {
      println(s"Prix d'achat $taxePercent de $nbCar x $model : $total2 €")
      println(s"> [INFO] Prix unitaire : "+ cost(model) + s" € / Prix unitaire $taxePercent : " + total1(1) + " €")
    } else { // Ne devrait pas arriver dans notre cas présent,
      val getHighestPrice: Double = scala.collection.mutable.ListBuffer(total1(nbCar), total2).max
      // Ici les prix devant être normalement identique, on peut éviter une erreur préjudiciable au vendeur en prenant le prix le plus élevé
      val getBestReductionPrice: Double = scala.collection.mutable.ListBuffer(total1(nbCar), total2).min
      // ou au contraire utiliser min qui aurait permis de détecter par exemple la meilleure réduction si une telle fonctionnalité avait été mise en place
      println(s"[INFO] Exemple de prix d'achat $taxePercent de $nbCar x $model : $getHighestPrice € (avec toutes les options)")
      println(s"[INFO] Exemple de prix d'achat $taxePercent de $nbCar x $model : $getBestReductionPrice € (sans option ajoutée)")
    }

  }
}


// Création d'un objet compagnon (portant le meme nom que la classe Car) devant
// être défini dans le meme fichier source. La création d'un objet singleton
// n'est pas initialisé avant qu'un code n'y accède, et ne peuvent pas accepter
// de paramètres et sont en fait implémentés en tant qu'instance d'une classe synthétique.
object Car {
  private val voiture = new Car("Citroen C6")
  // voiture un objet singleton instanciant la classe "Car".
  private val voitureConso = new CarConso(72,10,8.5,11.2,10.0)
  private val voitureDist = new CarDist(72d,10d,8.5,11.2,10.0)

  def main(args: Array[String]): Unit = {
    // Tests de notations INFIX sur des méthodes intégrées et personnalisées :

    new Vehicle calcAvgSpeed (100, 2) // inconnu
    new Car calcAvgSpeed (100, 2) // sans marque

    // On instancie un nouvel objet Car, qui est une instance de la classe Car dans le corps de l'objet compagnon
    // Cet objet est en fait une instance de la classe compagnon Car, et non de l'objet compagnon.
    voiture calcAvgSpeed (200, 2) // Citroen C6
    // voiture.vroom()
    voitureConso kmParPlein "Mixte"
    // On instancie d'autres classes dans cet objet compagnon "Car" ...
    voitureDist kmParPlein "Route"
    voitureDist kmParPlein "urbain"
    // ... CarConso et CarDist, permettant de définir plus concrètement l'objet en question
    voiture.calcTotalCost(10 ,"Citroen C6") // on veux acheter deux voitures
  }
}
