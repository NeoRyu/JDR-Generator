package jdr.generator.api.scala

import jdr.generator.api.scala.tools.TypescriptOperator._


class CarConso(
  kmParLitre: Double,
  litreParPlein: Double,
  kmParLitreRoute: Double = 0.0,
  kmParLitreUrbain: Double = 0.0,
  kmParLitreMixte: Double = 0.0,
) {
  var kml: Double = kmParLitre
  var kmlr: Double = (kmParLitreRoute == 0.0) ? kml :: kmParLitreRoute
  var kmlu: Double = (kmParLitreUrbain == 0.0) ? kml :: kmParLitreUrbain
  var kmlm: Double = kmParLitreMixte ?? kml
  var lpp: Double = litreParPlein
  var kpp: Double = 0.0


  def kmParPlein(): Unit = {
    kpp = kml * lpp
    println("Kilomètres par plein : " + kpp)
  }

  def kmParPlein(typeConso: String = "inconnu"): Unit = {
    val kppByType: Double = typeConso.toLowerCase match {
      case "route" => kmlr * lpp
      case "urbain" => kmlu * lpp
      case "mixte" => kmlm * lpp
      case _ => kml * lpp
    }
    println(s"Kilomètres par plein ($typeConso) : $kppByType")
  }

}


object CarTest extends App {
  val maVoiture = new CarConso(10, 50.0, 8.5, 11.2, 10.0)
  println(s"maVoiture : 10Km par L, 50L dans le reservoir, 8.5Km par L en conso route, 11.2Km/L conso urbain, 10Km/L conso mixte")
  maVoiture.kmParPlein("Route")
  maVoiture.kmParPlein("Urbain")
  maVoiture.kmParPlein("Mixte")
  maVoiture.kmParPlein("Mer")
  maVoiture.kmParPlein()

  val maSecondeVoiture = new CarConso(12.0, 65.0)
  println(s"maVoiture : 10Km par L, 50L dans le reservoir, 8.5Km par L en conso route, 11.2Km/L conso urbain, 10Km/L conso mixte")
  maSecondeVoiture.kmParPlein("Route")
  maSecondeVoiture.kmParPlein("Urbain")
  maSecondeVoiture.kmParPlein("Mixte")
  maSecondeVoiture.kmParPlein("Mer") // Test avec un type non reconnu
  maSecondeVoiture.kmParPlein() // Test avec un type non reconnu
}