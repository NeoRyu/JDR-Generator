package jdr.generator.api.scala;


// ScalaMessage est un object en Scala (ce qui crée une instance singleton et son "companion class" contenant les définitions)
object ScalaMessage {

  // Il est possible d'appeler la methode SCALA main de l'object ScalaMessage depuis du code JAVA comme ceci :
  // ScalaMessage.main(new String[]{});
  def main(args: Array[String]) : Unit = {
    println("Générations terminées avec succès ! N'oubliez pas que des fichiers ont été déposés dans votre dossier 'downloads/jdr-generator/'... =)")
  }

}