/*
 * Copyright (c) 2025 [COUPEZ Frédéric]
 *
 * This specific file (TypescriptOperators.scala) is based on code from:
 * https://github.com/NeoRyu/JDR-Generator/tree/main/api/src/main/scala/jdr/generator/api/scala/tools
 * and is under a MIT License. The rest of the application is under an Apache License, Version 2.0.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jdr.generator.api.scala.tools

// Équivalence de Comparable en Java
// Les traits sont des classes abstraites conçues pour être ajoutées à d'autres classes.
// Un trait permet d'ajouter certaines méthodes ou certains champs à une classe encore inconnue.
trait Ord {
  def < (that: Any): Boolean
  def <=(that: Any): Boolean =  (this < that) || (this == that)
  def > (that: Any): Boolean = !(this <= that)
  def >=(that: Any): Boolean = !(this < that)
}


// Création d'opérateur Typescript que j'affectionne transposé dans Scala !
object TypescriptOperators {

  // ------------------------------------------------------------------------------------------
  // Opérateur Ternaire
  // > description : valeur correspondant à la condition ? valeur à affecter : sinon autre valeur
  // > exemple : val x = (variable == value ? "value" :: "otherValue")
  // > demonstration 1 : val hasTest : String = ("test" == "test" ? "Oui" :: "Non")
  // > demonstration 2 : val isTest : Boolean = (hasTest ? true :: false)
  implicit class Question[T](predicate: => Boolean) {
    def ?(left: => T): (Boolean, T) = predicate -> left
  }
  implicit class Colon[R](right: => R) {
    def ::[L](pair: (Boolean, L))(implicit ev: L => R): R = if (pair._1) pair._2 else right
  }

  // ------------------------------------------------------------------------------------------
  // Opérateur Double d'Exclamation
  // > description : valeur non nulle et correspondant a la condition ?? sinon autre valeur
  // > exemple : val x = (variable != value ?? otherValue)
  // > demonstration 1 : val y : Double = doubleValue ?? 0.0
  // > demonstration 2 : val z : Double = 10.0 >= 15 ?? 0.0
  implicit class BooleanCoalescing[B, T](condition: => Boolean) {
    def ??[R >: T](defaultValue: => R)(implicit asT: B => T): R = {
      if (condition) {
        defaultValue match {
          case opt: Option[_] => opt.getOrElse(defaultValue).asInstanceOf[R]
          case s: String => if (s.isEmpty) defaultValue.asInstanceOf[R] else s.asInstanceOf[R]
          case d: Double => if (d == 0.0) defaultValue.asInstanceOf[R] else d.asInstanceOf[R]
          case _ => defaultValue
        }
      } else defaultValue
    }
  }
  implicit class OptionCoalescing[A](option: Option[A]) {
    def ??[B >: A](defaultValue: => B): B = option.getOrElse(defaultValue)
  }
  implicit class NotZeroOrValueDouble(value: Double) {
    def ??[B >: Double](defaultValue: => B): B = if (value != 0.0) value else defaultValue
  }
  implicit class NotNullOrValue[A](value: A) {
    def ??[B >: A](defaultValue: => B)(implicit ev: Null <:< A = null): B =
      if (value != null) value else defaultValue
  }
  // ------------------------------------------------------------------------------------------

}