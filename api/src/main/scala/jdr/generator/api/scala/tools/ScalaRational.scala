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

}