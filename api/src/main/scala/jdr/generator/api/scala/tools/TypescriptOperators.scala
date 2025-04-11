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

/**
 * Java's Comparable equivalent.
 * Traits are abstract classes designed to be added to other classes.
 * A trait allows adding certain methods or fields to a class that is not yet known.
 */
trait Ord {
  /**
   * Less than comparison.
   * @param that The object to compare with.
   * @return True if this object is less than 'that', false otherwise.
   */
  def < (that: Any): Boolean
  /**
   * Less than or equal to comparison.
   * @param that The object to compare with.
   * @return True if this object is less than or equal to 'that', false otherwise.
   */
  def <=(that: Any): Boolean =  (this < that) || (this == that)
  /**
   * Greater than comparison.
   * @param that The object to compare with.
   * @return True if this object is greater than 'that', false otherwise.
   */
  def > (that: Any): Boolean = !(this <= that)
  /**
   * Greater than or equal to comparison.
   * @param that The object to compare with.
   * @return True if this object is greater than or equal to 'that', false otherwise.
   */
  def >=(that: Any): Boolean = !(this < that)
}


/**
 * Creation of Typescript-like operators that I like, transposed into Scala!
 */
object TypescriptOperators {

  // ------------------------------------------------------------------------------------------
  /**
   * Implicit class that enables the Typescript-like ternary operator syntax using `?` and `::`.
   * When a `Boolean` is followed by `?`, it creates an intermediate structure.
   * @param predicate The boolean condition to evaluate.
   * @tparam T The type of the value to return if the condition is true.
   */
  implicit class Question[T](predicate: => Boolean) {
    /**
     * If the `predicate` is true, this method returns a tuple containing `true` and the provided `left` value.
     * This tuple is then used by the `::` method to determine the final result.
     * @param left The value to return if the `predicate` is true.
     * @return A tuple of (Boolean, T).
     */
    def ?(left: => T): (Boolean, T) = predicate -> left
  }

  /**
   * Implicit class that completes the Typescript-like ternary operator syntax using `::`.
   * It takes the result of the `?` method (a tuple) and a `right` value.
   * @param right The value to return if the condition from the `?` method was false.
   * @tparam R The type of the result. It must be a supertype of the type `L` from the tuple.
   */
  implicit class Colon[R](right: => R) {
    /**
     * Evaluates the boolean in the `pair`. If true, it returns the `left` value from the `pair` (implicitly converted to `R`).
     * If false, it returns the `right` value.
     * @param pair A tuple containing the boolean condition and the value to return if true.
     * @param ev An implicit conversion from the type `L` of the `left` value to the result type `R`.
     * @tparam L The type of the `left` value in the `pair`.
     * @return The `left` value (as `R`) if the condition is true, otherwise the `right` value.
     */
    def ::[L](pair: (Boolean, L))(implicit ev: L => R): R = if (pair._1) pair._2 else right
  }

  // ------------------------------------------------------------------------------------------
  /**
   * Implicit class that provides a Typescript-like nullish coalescing operator (`??`) that also checks a boolean condition.
   * If the `condition` is true, it then checks the `defaultValue` for "null-like" values (None for Option, empty String, zero Double)
   * and returns `defaultValue` if it's null-like, otherwise it returns the `defaultValue` itself.
   * If the `condition` is false, it directly returns the `defaultValue`.
   * @param condition The boolean condition to evaluate.
   * @tparam B The type of the boolean condition (can be any type that can be implicitly converted to Boolean).
   * @tparam T The type of the `defaultValue`.
   *
   * > example : val x = (variable != value ?? otherValue)
   * > demonstration 1 : val y : Double = doubleValue ?? 0.0
   * > demonstration 2 : val z : Double = 10.0 >= 15 ?? 0.0
   */
  implicit class BooleanCoalescing[B, T](condition: => Boolean) {
    /**
     * Evaluates the `condition`. If true, it checks the `defaultValue` for null-like values based on its type.
     * If the `condition` is false, it returns the `defaultValue`.
     * @param defaultValue The value to return if the `condition` is false or if the `condition` is true and `defaultValue` is null-like.
     * @param asT An implicit conversion from type `B` to `Boolean`.
     * @tparam R The result type, which must be a supertype of `T`.
     * @return The `defaultValue` (potentially cast to `R`) based on the condition and the value of `defaultValue`.
     *
     * > example 1: val hasTest: String = ("test" == "test" ?? "Oui") // hasTest will be "test"
     * > example 2: val isTrue: Boolean = (Some(1) != None ?? true) // isTrue will be true
     * > example 3: val emptyString: String = ("" != "value" ?? "default") // emptyString will be "default"
     * > example 4: val zeroDouble: Double = (0.0 != 1.0 ?? 5.0) // zeroDouble will be 5.0
     */
    def ??[R >: T](defaultValue: => R)(implicit asT: B => Boolean): R = {
      if (condition) {
        defaultValue match {
          case opt: Option[_] => opt.getOrElse(defaultValue).asInstanceOf[R] // If Option is None, return defaultValue
          case s: String => if (s.isEmpty) defaultValue.asInstanceOf[R] else s.asInstanceOf[R] // If String is empty, return defaultValue
          case d: Double => if (d == 0.0) defaultValue.asInstanceOf[R] else d.asInstanceOf[R] // If Double is zero, return defaultValue
          case other => defaultValue // For other types, just return defaultValue
        }
      } else defaultValue // If the condition is false, return defaultValue
    }
  }

  /**
   * Implicit class that provides a Typescript-like nullish coalescing operator (`??`) for `Option`.
   * If the `option` is `None`, it returns the `defaultValue`; otherwise, it returns the value inside the `Option`.
   * @param option The `Option` to check.
   * @tparam A The type of the value inside the `Option`.
   *
   * > example : val name: String = Option("John") ?? "Guest" // name will be "John"
   * > example : val missingName: String = None ?? "Guest" // missingName will be "Guest"
   */
  implicit class OptionCoalescing[A](option: Option[A]) {
    /**
     * Returns the value inside the `option` if it's `Some`, otherwise returns the `defaultValue`.
     * @param defaultValue The value to return if the `option` is `None`.
     * @tparam B The result type, which must be a supertype of `A`.
     * @return The value inside the `option` or the `defaultValue`.
     */
    def ??[B >: A](defaultValue: => B): B = option.getOrElse(defaultValue)
  }

  /**
   * Implicit class that provides a Typescript-like nullish coalescing operator (`??`) for `Double`, checking for zero.
   * If the `value` is `0.0`, it returns the `defaultValue`; otherwise, it returns the `value`.
   * @param value The `Double` value to check.
   * @tparam B The result type, which must be a supertype of `Double`.
   * @return The `value` if it's not 0.0, otherwise the `defaultValue`.
   *
   * > example : val value: Double = 3.14 ?? 1.0 // value will be 3.14
   * > example : val zeroValue: Double = 0.0 ?? 1.0 // zeroValue will be 1.0
   */
  implicit class NotZeroOrValueDouble(value: Double) {
    def ??[B >: Double](defaultValue: => B): B = if (value != 0.0) value else defaultValue
  }

  /**
   * Implicit class that provides a Typescript-like nullish coalescing operator (`??`) for any reference type, checking for `null`.
   * If the `value` is `null`, it returns the `defaultValue`; otherwise, it returns the `value`.
   * @param value The value to check for null.
   * @tparam A The type of the value.
   *
   * > example : val maybeNull: String = null
   * > example : val nonNull: String = "Hello"
   * > example : val result1: String = maybeNull ?? "Default" // result1 will be "Default"
   * > example : val result2: String = nonNull ?? "Default" // result2 will be "Hello"
   */
  implicit class NotNullOrValue[A](value: A) {
    /**
     * Returns the `value` if it's not `null`, otherwise returns the `defaultValue`.
     * @param defaultValue The value to return if `value` is `null`.
     * @param ev An implicit evidence that `Null` is a subtype of `A`. This ensures that the operator is only used with reference types.
     * @tparam B The result type, which must be a supertype of `A`.
     * @return The `value` if it's not null, otherwise the `defaultValue`.
     */
    def ??[B >: A](defaultValue: => B)(implicit ev: Null <:< A = null): B =
      if (value != null) value else defaultValue
  }
  // ------------------------------------------------------------------------------------------

}