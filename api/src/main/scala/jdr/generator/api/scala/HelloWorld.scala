package jdr.generator.api.scala;


// HelloWorld est un object en Scala (ce qui crée une instance singleton et son "companion class" contenant les définitions)
object HelloWorld {

  // Il est possible d'appeler la methode SCALA main de l'object HelloWorld depuis du code JAVA comme ceci :
  // HelloWorld.main(new String[]{});
  def main(args: Array[String]) : Unit = {
    println("Hello world, from Scala !")
  }

}