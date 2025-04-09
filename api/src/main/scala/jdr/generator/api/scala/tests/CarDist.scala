package jdr.generator.api.scala.tests

import jdr.generator.api.scala.tools.TypescriptOperators._


class CarDist(
  litreParPlein: Double,
  kmParLitre: Double,
  kmParLitreRoute: Double = 0.0,
  kmParLitreUrbain: Double = 0.0,
  kmParLitreMixte: Double = 0.0,
) {
  var kml: Double = kmParLitre
  var kmlr: Double = (kmParLitreRoute == 0.0) ? kml :: kmParLitreRoute
  var kmlu: Double = (kmParLitreUrbain == 0.0) ? kml :: kmParLitreUrbain
  var kmlm: Double = (kmParLitreMixte == 0.0) ? kml :: kmParLitreMixte
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
      case _ => 0.0
    }
    println(s"Kilomètres par plein ($typeConso) : $kppByType")
  }

}

object CarDistTest {
  val maVoiture = new CarDist(50.0, 10)
  println(s"maVoiture : 50L dans le reservoir, 10Km par L")
  maVoiture.kmParPlein("Route")
  maVoiture.kmParPlein("Urbain")
  maVoiture.kmParPlein("Mixte")
  maVoiture.kmParPlein() // Test sans type = mixte
  maVoiture.kmParPlein("Mer") // Test avec un type non reconnu
}