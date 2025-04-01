package jdr.generator.api.config;

import jdr.generator.api.characters.context.DefaultContextJson;

/**
 * https://ai.google.dev/api/generate-content?hl=fr#generationconfig
 */
public interface IGeminiGenerationConfig {
    Integer maxOutputTokens = 2048; // Nombre maximal de jetons à inclure dans une réponse candidate.
    double temperature = 0.7; // Contrôle le caractère aléatoire de la sortie.
    double topP = 0.8; // Probabilité cumulée maximale des jetons à prendre en compte lors de l'échantillonnage combiné Top-k et Top-p (noyau).
    double topK = 40; // Nombre maximal de jetons à prendre en compte lors de l'échantillonnage.

    public Object generate(DefaultContextJson data);

    public Object illustrate(String image);

    public Object stats(Long characterId);
}
