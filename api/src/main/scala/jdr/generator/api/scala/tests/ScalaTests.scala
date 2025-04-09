package jdr.generator.api.scala.tests

import jdr.generator.api.scala.tools._

import java.util.{Calendar, Date}

// Généricité : classe conteneur Reference, pouvant être vide ou pointer vers un objet typé
class Reference[T] {
  private var contenu: T = _
  def set(valeur: T): Unit = { contenu = valeur } // Methode avec paramètre - ex : cellule.set(0)
  def get: T = contenu                            // Méthode sans paramètre - ex : cellule.get
  // def getRef(): T = contenu                    // Méthode avec paramètre vide - ex : cellule.getMe()
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
    ScalaRationalTests()

    // 4/ Classe abstraite / trait sur des ensemble (Set) de Int
    ScalaIntSetTests()
  }

  private def ScalaRationalTests(): Unit = {
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

  private def ScalaIntSetTests(): Unit = {
    println(s"4/ Classe abstraite / trait sur des ensemble (Set) de Int, défini comme suit :" +
      s"\n- s1 = new NonEmptySet(1, new EmptySet, new EmptySet)" +
      s"\n- s2 = s1 incl 2" +
      s"\n- s3 = new NonEmptySet(3, new EmptySet, new NonEmptySet(4, new EmptySet, new EmptySet))" +
      s"\n- s4 = s2 union s3" +
      s"\n- s5 = s2 intersect s3" +
      s"\n- s6 = s4 excl 2" +
      s"\n- s7 = new EmptySet\n\n"
    )
    val s1 = new NonEmptySet(1, new EmptySet, new EmptySet)
    val s2 = s1 incl 2
    val s3 = new NonEmptySet(3, new EmptySet, new NonEmptySet(4, new EmptySet, new EmptySet))
    val s4 = s2 union s3
    val s5 = s2 intersect s3
    val s6 = s4 excl 2
    val s7 = new EmptySet

    println(s"s1 contains 1: ${s1 contains 1}")
    println(s"s1 contains 2: ${s1 contains 2}")
    println(s"s2 contains 2: ${s2 contains 2}")
    println(s"s3 contains 4: ${s3 contains 4}")
    println(s"s4 (s2 union s3) contains 1: ${s4 contains 1}")
    println(s"s4 (s2 union s3) contains 3: ${s4 contains 3}")
    println(s"s4 (s2 union s3) contains 2: ${s4 contains 2}")
    println(s"s5 (s2 intersect s3) contains 2: ${s5 contains 2}")
    println(s"s5 (s2 intersect s3) contains 3: ${s5 contains 3}")
    println(s"s6 (s4 excl 2) contains 1: ${s6 contains 1}")
    println(s"s6 (s4 excl 2) contains 2: ${s6 contains 2}")
    println(s"s7 isEmpty: ${s7.isEmpty}")
    println(s"s1 isEmpty: ${s1.isEmpty}")
  }

}