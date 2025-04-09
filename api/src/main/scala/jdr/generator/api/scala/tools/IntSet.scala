package jdr.generator.api.scala.tools

// Un trait (comme une interface en Java, mais plus puissant) qui définit le comportement d'un ensemble d'entiers.
// Il spécifie les opérations qu'un ensemble d'entiers doit pouvoir effectuer.
trait IntSet {
  // Vérifie si l'ensemble contient l'entier 'x'. Retourne true si c'est le cas, false sinon.
  def contains(x: Int): Boolean
  // Crée un nouvel ensemble contenant tous les éléments de cet ensemble plus l'entier 'x'.
  def incl(x: Int): IntSet
  // Crée un nouvel ensemble contenant tous les éléments qui sont présents à la fois dans cet ensemble et dans 'other'.
  def intersect(other: IntSet): IntSet
  // Crée un nouvel ensemble contenant tous les éléments qui sont présents soit dans cet ensemble, soit dans 'other', ou dans les deux.
  def union(other: IntSet): IntSet
  // Crée un nouvel ensemble contenant tous les éléments de cet ensemble à l'exception de l'entier 'x'.
  def excl(x: Int): IntSet
  // Vérifie si l'ensemble est vide. Retourne true s'il ne contient aucun élément, false sinon.
  def isEmpty: Boolean
}

// Une classe qui représente un ensemble d'entiers vide.
// Elle implémente le trait IntSet, fournissant une implémentation spécifique pour un ensemble vide.
class EmptySet extends IntSet {
  // Un ensemble vide ne contient aucun élément, donc 'contains' retourne toujours false.
  def contains(x: Int): Boolean = false
  // Inclure un élément 'x' dans un ensemble vide crée un nouvel ensemble non vide contenant uniquement 'x'.
  // Ce nouvel ensemble est représenté par une instance de la classe NonEmptySet.
  def incl(x: Int): IntSet = new NonEmptySet(x, new EmptySet, new EmptySet)
  // L'union d'un ensemble vide avec n'importe quel autre ensemble est cet autre ensemble.
  def union(other: IntSet): IntSet = other
  // L'intersection d'un ensemble vide avec n'importe quel autre ensemble est toujours un ensemble vide.
  def intersect(other: IntSet): IntSet = new EmptySet
  // Exclure un élément 'x' d'un ensemble vide ne change rien, car l'ensemble ne contient aucun élément.
  // On retourne donc l'ensemble vide lui-même.
  def excl(x: Int): IntSet = this
  // Un EmptySet est vide par définition, donc 'isEmpty' retourne true.
  def isEmpty: Boolean = true
}

// Une classe qui représente un ensemble d'entiers non vide.
// Elle implémente le trait IntSet en utilisant une structure d'arbre binaire pour stocker les éléments.
// 'elem' est l'entier stocké dans ce nœud de l'arbre.
// 'left' est le sous-ensemble contenant les éléments plus petits que 'elem'.
// 'right' est le sous-ensemble contenant les éléments plus grands que 'elem'.
class NonEmptySet(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  // Vérifie si l'ensemble contient 'x' en parcourant l'arbre binaire.
  // Si 'x' est plus petit que 'elem', on cherche dans le sous-ensemble gauche.
  // Si 'x' est plus grand que 'elem', on cherche dans le sous-ensemble droit.
  // Si 'x' est égal à 'elem', l'ensemble contient 'x', donc on retourne true.
  def contains(x: Int): Boolean =
    if (x < elem) left contains x
    else if (x > elem) right contains x
    else true

  // Inclure un élément 'x' dans l'ensemble non vide crée potentiellement un nouvel ensemble non vide.
  // Si 'x' est plus petit que 'elem', on crée un nouvel ensemble avec 'elem', le sous-ensemble gauche avec 'x' inclus, et le sous-ensemble droit inchangé.
  // Si 'x' est plus grand que 'elem', on crée un nouvel ensemble avec 'elem', le sous-ensemble gauche inchangé, et le sous-ensemble droit avec 'x' inclus.
  // Si 'x' est déjà égal à 'elem', l'ensemble ne change pas, donc on retourne l'ensemble actuel ('this').
  def incl(x: Int): IntSet =
    if (x < elem) new NonEmptySet(elem, left incl x, right)
    else if (x > elem) new NonEmptySet(elem, left, right incl x)
    else this

  // L'union de cet ensemble non vide avec 'other' est obtenue en prenant l'union du sous-ensemble gauche,
  // de l'union du sous-ensemble droit et de 'other', puis en incluant l'élément courant 'elem' dans le résultat.
  def union(other: IntSet): IntSet =
    ((left union right) union other) incl elem

  // L'intersection de cet ensemble non vide avec 'other' contient 'elem' si 'other' contient également 'elem'.
  // Dans ce cas, l'intersection est un nouvel ensemble non vide contenant 'elem' et l'intersection des sous-arbres gauche et droit avec 'other'.
  // Si 'other' ne contient pas 'elem', l'intersection est l'union de l'intersection du sous-arbre gauche avec 'other' et de l'intersection du sous-arbre droit avec 'other'.
  def intersect(other: IntSet): IntSet =
    if (other contains elem) new NonEmptySet(elem, left intersect other, right intersect other)
    else left intersect other union (right intersect other)

  // Exclure un élément 'x' de cet ensemble non vide crée potentiellement un nouvel ensemble non vide.
  // Si 'x' est plus petit que 'elem', on crée un nouvel ensemble avec 'elem', le sous-ensemble gauche sans 'x', et le sous-arbre droit inchangé.
  // Si 'x' est plus grand que 'elem', on crée un nouvel ensemble avec 'elem', le sous-ensemble gauche inchangé, et le sous-arbre droit sans 'x'.
  // Si 'x' est égal à 'elem', l'élément courant doit être retiré. On retourne alors l'union des sous-arbres gauche et droit,
  // ce qui combine tous les éléments plus petits et plus grands que l'élément retiré.
  def excl(x: Int): IntSet =
    if (x < elem) new NonEmptySet(elem, left excl x, right)
    else if (x > elem) new NonEmptySet(elem, left, right excl x)
    else left union right // Si x est l'élément courant, on retourne l'union des sous-arbres

  // Un NonEmptySet contient au moins un élément ('elem'), donc 'isEmpty' retourne toujours false.
  def isEmpty: Boolean = false
}