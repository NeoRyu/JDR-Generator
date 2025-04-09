package jdr.generator.api.scala.tools

// jdr.generator.api.scala.tools.TypescriptOperator.Ord


class ScalaDate(a: Int, m: Int, j: Int) extends Ord {
  private def annee: Int = a
  private def mois: Int = m
  private def jour: Int = j

  override def toString: String = s"$annee-$mois-$jour"

  // Redéfinition de la méthode equals, héritée de Object, pour comparer correctement les dates en comparant leurs champs individuels.
  // L’implémentation par défaut de equals n’est pas utilisable, car en Java, elle compare les objets physiquement.
  /*override def equals(that: Any): Boolean =
    that.isInstanceOf[Date] && {
      val d = that.asInstanceOf[Date]
      d.jour == jour && d.mois == mois && d.annee == annee
    }*/
  override def equals(that: Any): Boolean = that match {
    case other: ScalaDate => other.jour == jour && other.mois == mois && other.annee == annee
    case _ => false
  }

  // Date étend le trait Ord et implémente la méthode abstraite < ainsi que la méthode equals,
  // elle hérite automatiquement des implémentations concrètes pour les autres méthodes du trait Ord
  override def <(that: Any): Boolean = that match {
    case d: ScalaDate =>
      (annee < d.annee) ||
        (annee == d.annee && (mois < d.mois ||
          (mois == d.mois && jour < d.jour)))
    case _ => sys.error(s"ERROR : On ne peut pas comparer $that et une Date")
  }

  // Redéfinition de hashCode pour être cohérent avec equals
  override def hashCode(): Int = {
    val prime = 31
    var result = 1
    result = prime * result + annee
    result = prime * result + jour
    result = prime * result + mois
    result
  }

}
