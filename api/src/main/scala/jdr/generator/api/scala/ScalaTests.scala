package jdr.generator.api.scala

import jdr.generator.api.scala.tools.{ScalaDate, ScalaRational}

import java.util.{Calendar, Date}

// Généricité : classe conteneur Reference, pouvant être vide ou pointer vers un objet typé
class Reference[T] {
  private var contenu: T = _
  def set(valeur: T): Unit = { contenu = valeur }
  def get: T = contenu
}

object ScalaTests {

  def main(args: Array[String]): Unit = {
    // 1/ Test de la redéfinition de Date
    def date: Date = new Date()
    println("1/ Nous sommes le " + new ScalaDate(Calendar.YEAR, Calendar.MONTH, Calendar.DATE).toString)

    // 2/ Test de l'inférence Int avec une opération via le conteneur générique Reference (ou T est paramétrisé en Double)
    def valReference = date.getDate
    val cellule = new Reference[Double]
    cellule.set(valReference)
    println("2/ La valeur de référence "+valReference+" est la moitié de " + (cellule.get * 2))

    // 3/ Scala n'a pas de type prédéfini pour représenter les nombres rationnels, ScalaRational va y remédier. Appel de la fonction ScalaRationalTest
    ScalaRationalTest()

  }

  private def ScalaRationalTest(): Unit = {
    // 3/ Scala n'a pas de type prédéfini pour représenter les nombres rationnels, ScalaRational va y remédier
    val r1 = new ScalaRational(1, 2)
    val r2 = new ScalaRational(1, 3)
    println(s"3/ Tests avec les nombres rationnels $r1 et $r2")

    println(s"$r1 + $r2 = ${r1 + r2}")
    println(s"$r1 - $r2 = ${r1 - r2}")
    println(s"$r1 * $r2 = ${r1 * r2}")
    println(s"$r1 / $r2 = ${r1 / r2}")

    println(s"$r1 == $r2: ${r1 == r2}")
    println(s"$r1 < $r2: ${r1 < r2}")
    println(s"$r1 > $r2: ${r1 > r2}")
    println(s"$r1 <= $r2: ${r1 <= r2}")
    println(s"$r1 >= $r2: ${r1 >= r2}")

    val sortedRationals = List(new ScalaRational(3, 4), new ScalaRational(1, 2), new ScalaRational(2, 3)).sorted
    println(s"> Rationnels triés: $sortedRationals")
  }

}