package jdr.generator.api.scala.tests

import scala.beans.BeanProperty
import scala.math.BigDecimal.RoundingMode

@deprecated
@SerialVersionUID(1L)
class CarBean(
               @BeanProperty var model: String,
               @BeanProperty var price: Double
             ) extends Serializable {
  override def toString: String = s"Car(model=$model, price=$price)"
  override def equals(obj: Any): Boolean = obj match {
    case that: CarBean => this.model == that.model && this.price == that.price
    case _ => false
  }
  override def hashCode(): Int = {
    val prime = 31
    var result = 1
    result = prime * result + (if (model == null) 0 else model.hashCode)
    result = prime * result + price.hashCode
    result
  }
  def formattedPrice(currency: String = "€"): String = f"$price%.2f $currency"
  def copy(newPrice: Double = price): CarBean = new CarBean(model, newPrice)
}

class CarModels {
  // On ajoute autant d'instances CarBean de modèles de véhicule qu'on le souhaite ici :
  val C3 = new CarBean("Citroen C3", 15750.42)
  val C6 = new CarBean("Citroen C6", 50450.51)
  val T15SC = new CarBean("Citroën Tract...", 612440.0)
  T15SC.setModel("Citroën Traction 15 Six Cabriolet") // Le Bean possède un getter et setter intégrés

  // Configuration des réductions par modèle
  private val reductionConfig: Map[CarBean, Option[Double]] = Map(
    C3 -> Some(0.20),    // 20% de réduction maximum pour l'instance C3
    C6 -> Some(0.05),    // 5% de réduction maximum pour l'instance C6
    T15SC -> None        // Aucune réduction pour l'instance T15SC
  )

  def getAllModels: List[CarBean] = List(C3, C6, T15SC)

  def getMaxReduction(car: CarBean): Option[Double] = reductionConfig.getOrElse(car, None)

  def isReductionAllowed(car: CarBean): Boolean = getMaxReduction(car).isDefined

  @inline def getListModels: String = "MODELES DISPONIBLES :: " + getAllModels.map(_.model).mkString(", ")
}

object calcPrice {
  // On récupère la liste des instances existantes
  private val carModels = new CarModels()
  private val listModels: List[CarBean] = carModels.getAllModels
  println(carModels.getListModels)

  val taxe = 1.2;
  val taxePercent: String = "(TVA " + (taxe * 100 - 100) + "% incl.)"

  def calcTotalCost(nbCar: Int, selectedCar: CarBean, reduction: Double = 0.0): Double = {
    println(s"\n======================================================================================")

    val maxReductionAllowed = carModels.getMaxReduction(selectedCar).getOrElse(0.0)

    if (reduction > maxReductionAllowed) {
      println(s"'calcTotalCost' :: [ERREUR DE REDUCTION] La réduction demandée de ${reduction * 100}% dépasse le maximum autorisé de ${maxReductionAllowed * 100}% pour le modèle ${selectedCar.model}.")
      return calcTotalCost(nbCar, selectedCar) // Retourne le prix sans réduction
    }

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
    def total(qteCar: Int = nbCar): Double = calcTva(subTotalForCar(qteCar))

    val finalTotal = total(nbCar) * (1 - reduction)

    if (total(nbCar) != 0.0) {
      val unitCost = listModels.find(_.model == selectedCar.model).map(_.price).getOrElse(0.0)
      println(s"Prix d'achat $taxePercent de $nbCar x ${selectedCar.model} : ${"%.2f".format(total(nbCar))} €")
      println(s"> [INFO] Prix unitaire : ${"%.2f".format(unitCost)} € / Prix unitaire $taxePercent : ${"%.2f".format(total(1))} €")
      if (reduction > 0) {
        println(s"> [REDUCTION] Application d'une réduction de ${reduction * 100}% (maximum autorisé: ${maxReductionAllowed * 100}%)")
        println(s"> [REDUCTION] Prix total après réduction : ${finalTotal} €")
      }
      BigDecimal(finalTotal).setScale(2, RoundingMode.HALF_UP).toDouble // Retourner un Double arrondi
    } else {
      println(s"'calcTotalCost' :: Une erreur technique est survenue...")
      println(s"> [ERROR] Le modèle ${selectedCar.model} est introuvable !")
      0.0 // Retourner 0.0 en cas d'erreur
    }
  }

  def isReductionAllowedAndShow(bean: CarBean): Unit = {
    if (carModels.isReductionAllowed(bean)) {
      println(s"[isReductionAllowedAndShow] Le modèle ${bean.model} peut bénéficier d'une réduction.")
      val maxReduction = carModels.getMaxReduction(bean).getOrElse(0.0)
      println(s"[isReductionAllowedAndShow] Le taux de réduction maximum autorisé est de ${maxReduction * 100}%.")
    } else {
      println(s"[isReductionAllowedAndShow] Le modèle ${bean.model} ne peut pas bénéficier de réduction.")
    }
  }

}

object CarTest extends App {
  private val models = new CarModels()

  private val prixUndefinedCarModels = calcPrice.calcTotalCost(10000, new CarBean("LOTUS", 1))
  println(s"Prix total pour la tentative d'achat de 10000 véhicules LOTUS à 1 € : $prixUndefinedCarModels €")

  private val prixC3 = calcPrice.calcTotalCost(10, models.C3)
  private val prixC6 = calcPrice.calcTotalCost(3, models.C6)
  private val prixT15SC = calcPrice.calcTotalCost(1, models.T15SC) // On peut verifier ici que le setter a bien fonctionné

  private val prixTotal = prixC6 + prixC3 + prixT15SC
  println(s"\n======================================================================================")
  println(s"Prix total pour l'achat de l'ensemble de ses véhicules (non arrondi) : ${prixTotal} €")
  println(s"Prix total pour l'achat de l'ensemble de ses véhicules (arrondi) : ${"%.2f".format(prixTotal)} €")
  println(s"Prix total pour l'achat de l'ensemble de ses véhicules (arrondi à l'unité) : ${prixTotal.round} €")

  println(s"\n======================================================================================")
  val maVoiture = new CarBean("Peugeot 208", 18000.0)
  println(maVoiture)
  println(s"Prix formaté : ${maVoiture.formattedPrice()}")
  println(s"Prix formaté en USD : ${maVoiture.formattedPrice("$")}")
  val autreVoiture = new CarBean("Peugeot 208", 18000.0)
  println(s"maVoiture == autreVoiture : ${maVoiture == autreVoiture}")
  private val prixAutreVoiture = calcPrice.calcTotalCost(10, autreVoiture)
  println(s"Prix total pour l'achat d'une voiture générée inexistante de la liste : ${prixAutreVoiture} €")
  // Si le modèle n'existe pas, il n'est pas possible de simuler le calcul du cout total

  println(s"\n======================================================================================")
  val voitureCopie = models.C3.copy(newPrice = 1000.0)
  println(s"Voiture d'origine : ${models.C3}")
  println(s"Voiture copiée avec nouveau prix : $voitureCopie")
  private val prixCopie = calcPrice.calcTotalCost(10, voitureCopie)
  println(s"Prix total pour l'achat de la voiture copiée avec prix modifié : ${prixCopie} €")
  // Si le modèle existe, mais que l'on modifie son prix, la simulation de calcul du cout total ne prend pas en compte cette valeur
  println(s"\n======================================================================================")

  // Dernier cas d'exemple avec réduction
  println(s"\n======================== TEST D'AJOUT DE REDUCTION ========================")

  val voiturePourReduction = models.C3
  val reductionPourcentageValide = 0.18 // 18% de réduction (inférieur au maximum de 20%)
  calcPrice.isReductionAllowedAndShow(voiturePourReduction)
  private val prixAvecReductionValide = calcPrice.calcTotalCost(5, voiturePourReduction, reductionPourcentageValide)
  println(s"Prix total pour l'achat de 5 x ${voiturePourReduction.model} avec une réduction de ${reductionPourcentageValide * 100}% : ${prixAvecReductionValide} €")
  println(s"==========================================================================")

  val voitureSansReduction = models.C6
  val tentativeReductionNonAutorisee = 0.06 // 6% de réduction (supérieur au maximum de 5%)
  calcPrice.isReductionAllowedAndShow(voitureSansReduction)
  private val prixSansReductionTentative = calcPrice.calcTotalCost(2, voitureSansReduction, tentativeReductionNonAutorisee)
  println(s"Prix total pour l'achat de 2 x ${voitureSansReduction.model} avec une tentative de réduction de ${tentativeReductionNonAutorisee * 100}% : ${prixSansReductionTentative} €")
  println(s"==========================================================================")

  val voitureAvecReductionMaxDepassee = models.T15SC
  val reductionTropElevee = 0.20  // 20% de réduction (aucune reduction n'est autorisée sur ce véhicule)
  calcPrice.isReductionAllowedAndShow(voitureAvecReductionMaxDepassee)
  private val prixReductionMaxDepassee = calcPrice.calcTotalCost(1, voitureAvecReductionMaxDepassee, reductionTropElevee)
  println(s"Prix total pour l'achat de 1 x ${voitureAvecReductionMaxDepassee.model} avec une réduction de ${reductionTropElevee * 100}% (dépasse le maximum) : ${prixReductionMaxDepassee} €")


  println(s"==========================================================================")
}