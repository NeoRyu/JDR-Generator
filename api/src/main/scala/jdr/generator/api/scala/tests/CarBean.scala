package jdr.generator.api.scala.tests

import scala.beans.BeanProperty
import scala.reflect.runtime.universe._


// JavaBeans
class CarBean
(
  @BeanProperty var model: String,
  @BeanProperty var price: Double
  // Si l'on souhaite ajouter de nouvelles properties, il faudra modifier les instances correspondantes dans CarModels
)

class CarModels {
  // On ajoute autant d'instances CarBean de modèles de véhicule qu'on le souhaite ici :
  val C3 = new CarBean("Citroen C3", 15750.43)
  val C6 = new CarBean("Citroen C6", 50450.51)
  val T15SC = new CarBean("Citroën Traction 15 Six Cabriolet", 612440.0)

  // Génère automatiquement une liste de toutes les instances de CarModel dans cet objet
  def getAllModels: List[CarBean] = {
    val mirror = runtimeMirror(getClass.getClassLoader)
    val currentMirror = mirror reflect this
    val modelTermSymbols = currentMirror.symbol.typeSignature.decls.filter(sym => sym.isTerm && !sym.isMethod)
    modelTermSymbols.map { symbol =>
      val instance = currentMirror.reflectField(symbol.asTerm).get
      instance match {
        case car: CarBean => car
        case func: Function0[_] if func.apply().isInstanceOf[CarBean] => func.apply().asInstanceOf[CarBean]
        case _ => null
      }
    }.collect { case model: CarBean => model }.toList
  }
}

object calcPrice {
  // On récupère la liste des instances existantes
  private val listModels: List[CarBean] = new CarModels().getAllModels

  val taxe = 1.2;
  val taxePercent: String = "(TVA " + (taxe * 100 - 100) + "% incl.)"

  def calcTotalCost(nbCar: Int, selectedCar: CarBean): Double = {
    // Calcul du sous-total quantité x prix du model
    def subTotalForCar: Int => Double = (qte: Int) => {
      listModels.find(_.model == selectedCar.model) match {
        case Some(foundCar) => qte * foundCar.price
        case None => 0.0 // Si l'instance CarBean n'a pas était définie dans CarModels
      }
    }

    // Calcul sous total avec tva appliquée
    def calcTva: Double => Double = (sub: Double) => {
      sub * taxe
    }
    // Calcul du prix total pour le nombre de véhicules de ce modèle
    def total(qteCar: Int = nbCar): Double = (calcTva compose subTotalForCar)(qteCar)
    println(s"======================================================================================")
    if (total(nbCar) != 0.0) {
      val unitCost = listModels.find(_.model == selectedCar.model).map(_.price).getOrElse(0.0)
      println(s"Prix d'achat $taxePercent de $nbCar x ${selectedCar.model} : " + total(nbCar) + " €")
      println(s"> [INFO] Prix unitaire : " + unitCost + s" € / Prix unitaire $taxePercent : " + total(1) + " €")
      total(nbCar) // Retourner le prix total du nombre choisi pour le véhicule du modèle sélectionné
    } else { // Si le model est introuvable
      println(s"Une erreur technique est survenue...")
      println(s"> [ERROR] Le modèle ${selectedCar.model} est introuvable !")
      0.0 // Retourner 0.0 en cas d'erreur
    }
  }

}

object CarTest extends App {
  private val models = new CarModels()

  private val prixUndefinedCarModels = calcPrice.calcTotalCost(10000, new CarBean("LOTUS", 1))
  println(s"Prix total pour la tentative d'achat de 10000 véhicules LOTUS à 1 € : $prixUndefinedCarModels €")

  private val prixC3 = calcPrice.calcTotalCost(10, models.C3)
  private val prixC6 = calcPrice.calcTotalCost(3, models.C6)
  private val prixT15SC = calcPrice.calcTotalCost(1, models.T15SC)

  private val prixTotal = prixC6 + prixC3 + prixT15SC
  println(s"\n======================================================================================")
  println(s"Prix total pour l'achat de l'ensemble de ses véhicules : $prixTotal €")
}