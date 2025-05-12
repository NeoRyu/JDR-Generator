package jdr.generator.api.config;

import jdr.generator.api.characters.context.DefaultContextJson;

/**
 * Configuration de l'API Gemini pour la génération de contenu.
 *
 * <p>Cette interface définit les constantes de configuration et les méthodes pour interagir avec l'API
 * Gemini afin de générer du contenu textuel et des illustrations. Elle s'appuie sur la documentation
 * officielle de Google : <a
 * href="https://ai.google.dev/api/generate-content?hl=fr#generationconfig">https://ai.google.dev/api/generate-content?hl=fr#generationconfig</a>
 */
public interface IGeminiGenerationConfiguration {

  /**
   * Nombre maximal de jetons à inclure dans une réponse candidate.
   *
   * <p>Corresponds to the `maxOutputTokens` parameter in the Gemini API.
   */
  Integer maxOutputTokens = 2048;

  /**
   * Contrôle le caractère aléatoire de la sortie.
   *
   * <p>Corresponds to the `temperature` parameter in the Gemini API.
   * <p>Valeurs : 0 = Déterministe, 1 = Complètement aléatoire.
   */
  double temperature = 0.7;

  /**
   * Probabilité cumulée maximale des jetons à prendre en compte lors de l'échantillonnage combiné
   * Top-k et Top-p (noyau).
   *
   * <p>Corresponds to the `topP` parameter in the Gemini API.
   */
  double topP = 0.8;

  /**
   * Nombre maximal de jetons à prendre en compte lors de l'échantillonnage.
   *
   * <p>Corresponds to the `topK` parameter in the Gemini API.
   */
  double topK = 40;

  /**
   * Génère du contenu textuel à partir des données de contexte par défaut fournies.
   *
   * @param data Les données de contexte par défaut pour la génération.
   * @return L'objet contenant le contenu textuel généré.
   */
  public Object generate(DefaultContextJson data);

  /**
   * Génère une illustration à partir d'une description textuelle (prompt).
   *
   * @param image La description textuelle de l'image à générer.
   * @return L'objet représentant l'image générée.
   */
  public Object illustrate(String image);

  /**
   * Génère des statistiques pour un personnage donné.
   *
   * @param characterId L'identifiant du personnage.
   * @return L'objet contenant les statistiques du personnage.
   */
  public Object stats(Long characterId);
}
