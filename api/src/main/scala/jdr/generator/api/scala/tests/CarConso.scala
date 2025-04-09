package jdr.generator.api.scala.tests

import jdr.generator.api.scala.tools.TypescriptOperators._


class CarConso(
  reservoir: Double,
  LitresAu100Km: Double,
  LitresAu100KmRoute: Double = 0.0,
  LitresAu100KmUrbain: Double = 0.0,
  LitresAu100KmMixte: Double = 0.0,
) {
  var la100: Double = LitresAu100Km
  var la100r: Double = LitresAu100KmRoute ?? la100
  var la100u: Double = LitresAu100KmUrbain ?? la100
  var la100m: Double = LitresAu100KmMixte ?? la100
  var ldp: Double = reservoir
  var kpp: Double = 0.0

  def kmParPlein(): Unit = {
    kpp = ldp / la100 * 100
    println("Kilomètres par plein : " + kpp)
  }

  def kmParPlein(typeConso: String = "inconnu"): Unit = {
    val lppByType: Double = typeConso.toLowerCase match {
      case "route" => ldp / la100r * 100
      case "urbain" => ldp / la100u * 100
      case "mixte" => ldp / la100m * 100
      case _ => 0.0
    }
    println(s"Kilomètres par plein ($typeConso) : $lppByType")
  }

}


object CarConsoTest extends App {
  val maVoiture = new CarConso(50.0, 10.0, 8.5, 11.2)
  println(s"maVoiture : 65L dans le reservoir, avec une conso de 12L/100km en mixte|moyenne, 8.5L/100 sur route et 11.2L/100 en conso urbaine")
  maVoiture.kmParPlein("Route")
  maVoiture.kmParPlein("Urbain")
  maVoiture.kmParPlein("Mixte")
  maVoiture.kmParPlein() // Test sans type = mixte
  maVoiture.kmParPlein("Mer") // Test avec un type non reconnu
}