package jdr.generator.api.scala.tools


class ScalaRational(n: Int, d: Int) extends Ordered[ScalaRational] {

  require(d != 0, "Le dénominateur ne peut pas être zéro")

  private def gcd(x: Int, y: Int): Int = {
    if (x == 0) y
    else if (x < 0) gcd(-x, y)
    else if (y < 0) -gcd(x, -y)
    else gcd(y % x, x)
  }

  private val g = gcd(n, d)
  val numerateur: Int = n / g
  val denominateur: Int = d / g

  def +(that: ScalaRational): ScalaRational =
    new ScalaRational(numerateur * that.denominateur + that.numerateur * denominateur,
      denominateur * that.denominateur)

  def -(that: ScalaRational): ScalaRational =
    new ScalaRational(numerateur * that.denominateur - that.numerateur * denominateur,
      denominateur * that.denominateur)

  def *(that: ScalaRational): ScalaRational =
    new ScalaRational(numerateur * that.numerateur, denominateur * that.denominateur)

  def /(that: ScalaRational): ScalaRational =
    new ScalaRational(numerateur * that.denominateur, denominateur * that.numerateur)

  override def toString: String = "" + numerateur + "/" + denominateur
  override def hashCode(): Int = (numerateur, denominateur).hashCode()
  override def compare(that: ScalaRational): Int = {
    val n1 = numerateur * that.denominateur
    val n2 = that.numerateur * denominateur
    if (n1 < n2) -1
    else if (n1 > n2) 1
    else 0
  }

  def test(): Unit = {
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

