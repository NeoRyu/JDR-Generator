package jdr.generator.api.scala.tests

import jdr.generator.api.scala.tools.TypescriptOperators._
import jdr.generator.api.scala.tools._

import java.awt.Color
import java.util.{Calendar, Date}

// Généricité : classe conteneur Reference, pouvant être vide ou pointer vers un objet typé
class Reference[T] {
  private var contenu: T = _
  def set(valeur: T): Unit = { contenu = valeur } // Methode avec paramètre - ex : cellule.set(0)
  def get: T = contenu                            // Méthode sans paramètre - ex : cellule.get
  // def getRef(): T = contenu                    // Méthode avec paramètre vide - ex : cellule.getMe()
}

/*
val = variable immutable
var = var mutable
def = function
trait = class abstraite
 */

object ScalaTests {

  case class Personne(name : String,addr: String, age: String)

  // COLLECTIONS
  var planetes = List(("Mercure", 57.9), ("Venus", 108.2), ("Mars", 227.9), ("Jupiter", 778.3))
  var cl = List(1,2,3,4,5)
  var clb = scala.collection.mutable.ListBuffer("x", "y", "z")
  var cm = scala.collection.mutable.Map("x" -> 24, "y" -> 25, "z" -> 26)
  var cs1 = Set(Color.red, Color.green, Color.blue)
  val cseq = Seq(7,4,1,8,5,2,9,6,3,0,10) // on ne peut que update la place de 2 items, pas en ajouter
  val cseq1 = Seq("Avion","Voiture","Bateau")
  val cseq2 = Seq("Maison","Bateau","Aeroport","Quai") // On met deux fois Bateau
  var iterator = Iterator("x", "y", "z")
  var css = scala.collection.mutable.SortedSet("hello", "world")
  var cb = scala.collection.mutable.Buffer("x", "y", "z")
  var cis = IndexedSeq(1.0, 2.0)
  /*
    Par défaut, Scala sélectionne toujours des collections immuables. Par exemple, Set sans préfixe ou sans import sera un ensemble immuable.
    Pour obtenir les versions mutables par défaut, on doit écrire explicitement collection.mutable.Set, ou collection.mutable.Iterable.
    - IMMUTABLE : https://docs.scala-lang.org/resources/images/tour/collections-immutable-diagram.svg
    - MUTABLE   : https://docs.scala-lang.org/resources/images/tour/collections-mutable-diagram.svg
   */

  // Ajout d'une fonction idiomatique de suppression d'une planète dans une List
  def remove(item: (String,Double), list: List[(String,Double)]): List[(String,Double)] = list diff List(item)

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

    // 5/ Jeu d'essai sur des collections
    println("5.1-1/ List : " + cl.head + " = " + cl(0))
    cl = 10 :: cl // on ajoute un objet Int 10 dans le head de la list
    println("5.1-2/ List : " + cl.head + " > " + cl(1) + " > " + cl(2))

    clb += "a" // on peux ajouter des String avec += dans ListBuffer
    clb -= "y" // on peux supprimer des String avec -= dans ListBuffer
    println("5.2-1/ ListBuffer : " + clb )
    println("5.2-2/ ListBuffer (min/max): " + clb.min + " >...> " + clb.max  )

    println("5.3-1/ Set: " + cs1.head + cs1.tail) // head affiche le premier element, tail tout sauf le premier
    val cs2 = Set(Color.red, Color.black, Color.white) // red est un doublon
    var csSentence = cs1 ++ cs2 // red n'apparaitra qu'une fois dans sentence en combinant les deux Set
    println("5.3-2/ Set (cs1+cs2): " + csSentence) // ce qui est le cas, on remarque que les valeur sont aussi mélangées

    println("5.4-1/ Map: x = " + cm("x")) // On peux accéder a une value d'un Map par sa key, ici x = 24
    cm.keys.foreach( i => println(s"key: $i | value: " + cm(i)) ) // parcours du map

    println("5.4-2.a/ Seq: " + cseq)
    println("5.4-2.b/ Seq (sorted): " + cseq.sorted) // tri sur des Int
    println("5.4-2.c/ Seq1 : " + cseq1)
    println("5.4-2.c/ Seq2 : " + cseq2)
    val concatSeq = cseq1++cseq2 // ++ permet la concatenation de deux listes
    println("5.4-2.d/ concatSeq : " + concatSeq) // la concat les met dans le meme ordre que ceux de l'ajout
    println("5.4-2.d/ concatSeq (sorted) : " + concatSeq.sorted)  // Tri sur des String

    planetes = ("Terre", 149.6) :: planetes // On ajoute une entrée dans le head de la List ; si c'était un val et non un var cette commande serait impossible
    println("5.5-0/ planètes : " + planetes) // On vérifie notre liste de planetes
    planetes.foreach {
      case ("Terre", distance) =>
        println(s"5.5-1/ Notre planète est à une distance de $distance millions de km du Soleil")
      case _ =>
    }
    planetes = remove(planetes.head, planetes) // on appel une fonction idiomatique définie préalablement pour suprrimer Terre qui se trouve en head de List
    println("5.5-2/ planètes : " + planetes) // On vérifie notre liste de planetes
    planetes.foreach {
      case ("Terre", distance) => println(s"5.5-3/ Ce message ne dois donc jamais apparaitre")
      case _ =>
    }

    println("5.6/ Iterator : " + iterator)
    while (iterator.hasNext) { // On va parcourir l'itération
      print(iterator.next + (iterator.hasNext ? " / " :: " [END]")) // affichant un a un les item et ajoutant un / si il y en a un apres, sinon on affiche [END]
      // (value condition ? true :: false)
      // Il s'agit de ma librairie TypescriptOperators permettant de simuler le comportement de opérateur conditionnel ternaire reproduisant ce qui ce fait en Typescript
      // Cela équivaut a :  if(value condition) { true } else { false } que je trouve moins ergonomique
      // (iterator.hasNext ?? " / ") n'est ici pas possible car on obtiendrai "xtrueytruezfalse", true etant la valeur non empty de hasNext elle serai ajoutée
    }

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