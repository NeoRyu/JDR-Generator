package jdr.generator.api.scala.tests

import jdr.generator.api.scala.tools.TypescriptOperators._
import jdr.generator.api.scala.tools._

import java.awt.Color
import java.util.{Calendar, Date}
import scala.collection.mutable


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
  var cm = mutable.Map("x" -> 24, "y" -> 25, "z" -> 26)
  // Plutôt que d'inscrire scala.collection.mutable, il est possible de l'importer pour simplement définir la collection sur mutable
  var cs1 = Set(Color.red, Color.green, Color.blue)
  val cseq = Seq(7,4,1,8,5,2,9,6,3,0,10) // on ne peut que update la place de 2 items, pas en ajouter
  val cseq1 = Seq("Avion","Voiture","Bateau")
  val cseq2 = Seq("Maison","Bateau","Aeroport","Quai") // On met deux fois Bateau
  var iterator = Iterator("x", "y", "z")
  var css = mutable.SortedSet("hello", "world")
  var cb = mutable.Buffer("x", "y", "z")
  var cis = IndexedSeq(1.0, 2.0)
  val ca = Array("Orange", "Pomme", "Banane")
  /*
    Par défaut, Scala sélectionne toujours des collections immuables. Par exemple, Set sans préfixe ou sans import sera un ensemble immuable.
    Pour obtenir les versions mutables par défaut, on doit écrire explicitement collection.mutable.Set, ou collection.mutable.Iterable.
    - IMMUTABLE : https://docs.scala-lang.org/resources/images/tour/collections-immutable-diagram.svg
    - MUTABLE   : https://docs.scala-lang.org/resources/images/tour/collections-mutable-diagram.svg
   */

  private def patternMatching(x: Any) : String = x match {
    case _: Char => "Char"
    case _: Int | _: Long | _: Short => "Integer"
    case _: Float => "Float"
    case _: Double => "Double"
    case _: String => "String"
    case _ => "N/A"
  }


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

    clb += "a" // on peut ajouter des String avec += dans ListBuffer
    clb -= "y" // on peut supprimer des String avec -= dans ListBuffer
    println("5.2-1/ ListBuffer : " + clb )
    println("5.2-2/ ListBuffer (min/max): " + clb.min + " >...> " + clb.max  )

    println("5.3-1/ Set: " + cs1.head + cs1.tail) // head affiche le premier element, tail tout sauf le premier
    val cs2 = Set(Color.red, Color.black, Color.white) // red est un doublon
    var csSentence = cs1 ++ cs2 // red n'apparaitra qu'une fois dans Sentence en combinant les deux Set
    println("5.3-2/ Set (cs1+cs2): " + csSentence) // ce qui est le cas, on remarque que les valeurs sont aussi mélangées

    println("5.4-1/ Map: x = " + cm("x")) // On peux accéder a une value d'un Map par sa key, ici x = 24
    println("5.4-1/ Map: keys = " + cm.keys)
    println("5.4-1/ Map: values = " + cm.values)
    cm.keys.foreach( i => println(s"key: $i | value: " + cm(i)) ) // parcours du map

    println("5.4-2.a/ Seq: " + cseq)
    println("5.4-2.b/ Seq (sorted): " + cseq.sorted) // tri sur des Int
    println("5.4-2.c/ Seq1 : " + cseq1)
    println("5.4-2.c/ Seq2 : " + cseq2)
    val concatSeq = cseq1++cseq2 // ++ permet la concatenation de deux listes
    println("5.4-2.d/ concatSeq : " + concatSeq) // la concat les met dans le meme ordre que ceux de l'ajout
    println("5.4-2.d/ concatSeq (sorted) : " + concatSeq.sorted)  // Tri sur des String

    planetes = ("Terre", 149.6) :: planetes // On ajoute une entrée dans le head de la List ; si c'était un val et non un var cette commande serait impossible
    println("5.5-0/ planètes : " + planetes) // On vérifie notre liste de planètes
    planetes.foreach {
      case ("Terre", distance) =>
        println(s"5.5-1/ Notre planète est à une distance de $distance millions de km du Soleil")
      case _ =>
    }
    // Ajout d'une fonction idiomatique de suppression d'une planète dans une List
    def remove(item: (String,Double), list: List[(String,Double)]): List[(String,Double)] = list diff List(item)
    planetes = remove(planetes.head, planetes) // on appel une fonction idiomatique définie préalablement pour supprimer Terre qui se trouve en head de List
    println("5.5-2/ planètes : " + planetes) // On vérifie notre liste de planètes
    planetes.foreach {
      case ("Terre", distance) => println(s"5.5-3/ Ce message ne dois donc jamais apparaitre")
      case _ =>
    }

    println("5.6/ Iterator : " + iterator)
    while (iterator.hasNext) { // On va parcourir l'itération
      print(iterator.next + (iterator.hasNext ? " / " :: "\n")) // affichant un a un les item et ajoutant un / s'il y en a un apres, sinon on affiche \n
      // (value condition ? true :: false)
      // Il s'agit de ma librairie TypescriptOperators permettant de simuler le comportement de l'opérateur conditionnel ternaire reproduisant ce qui ce fait en Typescript
      // Cela équivaut a :  if(value condition) { true } else { false } que je trouve moins ergonomique
      // (iterator.hasNext ?? " / ") n'est ici pas possible car on obtiendrai "xtrueytruezfalse", true étant la valeur non empty de hasNext, elle serait ajoutée sous forme booléenne
    }

    println("5.7.1/ Array : " + ca.head + " & " + ca.tail.mkString("Array(", ", ", ")")) // Array est une matrice partiellement modifiable
    ca(2) = "RAISIN" // "ca" est une variable immutable (val), sa longueur est fixe, commence à l'index 0 comme n'importe quelle collection, mais il est possible de modifier malgré tout un de ses items
    println("5.7.2/ Array : " + ca.head + " & " + ca.tail.mkString("Array(", ", ", ")")) // Banane (à l'emplacement 0>1>2) est bien remplacée par RAISIN

    // PATTERN MATCHING
    println("6-1/ 1 = " + patternMatching(1))
    println("6-2/ 1L = " + patternMatching(1L))
    println("6-3/ 1:Short = " + patternMatching(1: Short))
    println("6-4/ 1.0 = " + patternMatching(1.0)) // Si non précisé, il s'agira d'un Double
    println("6-4.a/ 1.0d = " + patternMatching(1.0d))
    println("6-4.b/ 1.0f = " + patternMatching(1.0f))
    println("6-5/ 'a' = " + patternMatching('a'))
    println("6-6/ \"a\" = " + patternMatching("a"))
    println("6-7/ Array(\"abc\") = " + patternMatching(Array("abc")))
    println("6-8/ List(\"def\") = " + patternMatching(List("def")))
    println("6-9/ true = " + patternMatching(true))

    // Création d'une fonction récursive
    def sumListInt(list: List[Int]): Int = list match {
      case head :: tail => head + sumListInt(tail) // On ajoute le head de la liste au call de cette fonction de manière recursive…
      case Nil => 0 // Nil = empty singleton object list : une fois le dernier item atteint, le tail sera vide, on ajoute 0 sans refaire de call recursive, ce qui met fin à l'addition.
    }
    println(s"7.a/ Fonction recursive de somme de la liste $cl")
    println(s"7.b/ Résultat :: " + sumListInt(cl))

    // Pattern matching sur des choix de classe custom
    import jdr.generator.api.scala.tools.JavaDelimiters._
    val spc: JavaDelimiters.Delimiter = SPACING() // Espacement " "
    val line: JavaDelimiters.Delimiter = LINEFEED() // Retour de chariot "\n"
    println(s"8.1 : " + spc.toString)
    println(s"8.2 : " + "a" + delimiter(spc) + "b")
    println(s"8.3 : " + "a" + delimiter(line) + "b")
    println(s"8.4 : " + (spc == line))
