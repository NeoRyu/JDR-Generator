# JDR-Generator Web (Frontend)

## Description

Ce module contient l'interface utilisateur web de JDR-Generator, développée avec React et TypeScript. Il permet aux utilisateurs d'interagir avec les APIs backend pour définir le contexte de création de personnage et visualiser les résultats générés par les APIs (descriptions de personnages, statistiques, illustrations).

## Caractéristiques Principales

* **Interface Interactive :** Permet aux utilisateurs de saisir facilement les informations nécessaires pour générer des personnages (race, classe, système de jeu, etc.).
* **Visualisation des Personnages :** Affiche les détails des personnages générés (texte et images) de manière claire et conviviale.
* **Développement avec React et TypeScript :** Utilise React pour la construction de l'interface utilisateur et TypeScript pour une meilleure gestion du code et des types.
* **Communication avec les APIs Backend :** Interagit avec les APIs Java et NestJS pour récupérer et afficher les données des personnages.

## Technologies Utilisées

* React
* TypeScript
* npm ou yarn
* (Autres bibliothèques frontend utilisées, par exemple, pour la gestion d'état, les requêtes HTTP, etc. - à compléter)

## Pré-requis

* Node.js
* npm ou yarn

## Installation

1.  **Cloner le dépôt :**

    ```bash
    git clone [https://github.com/NeoRyu/JDR-Generator.git](https://github.com/NeoRyu/JDR-Generator.git)
    ```

    ```bash
    cd JDR-Generator
    ```

2.  **Accéder au répertoire du module web :**

    ```bash
    cd web
    ```

3.  **Installer les dépendances :**

    ```bash
    npm install
    ```

    * Cette commande installera toutes les bibliothèques et tous les outils nécessaires au fonctionnement de l'interface web.

4.  **Configurer les variables d'environnement (si nécessaire) :**

    * Si l'interface web nécessite des variables d'environnement (par exemple, l'URL de l'API backend), créez un fichier `.env` à la racine du répertoire `web` et définissez les variables.
    * Exemple :

        ```
        REACT_APP_API_URL=http://localhost:3000  # URL de l'API NestJS
        ```

    * **Note :** Les variables d'environnement dans React doivent généralement être préfixées par `REACT_APP_`.

## Exécution de l'interface web

1.  **Démarrer le serveur de développement :**

    ```bash
    npm run start:dev
    ```

    * Cette commande lance l'application en mode développement avec rechargement automatique des modifications. Vous pourrez accéder à l'application dans votre navigateur à l'adresse indiquée (généralement `http://localhost:3000`).

    * Si vous souhaitez simplement démarrer l'application en mode production (après avoir construit le projet), vous pouvez utiliser :

        ```bash
        npm run start
        ```

## Structure du Projet (Aperçu)

web/
├── public/       # Fichiers statiques (index.html, etc.)
├── src/          # Code source de l'application React
│   ├── components/  # Composants React réutilisables
│   ├── lib/       
│   ├── pages/       # Pages de l'application
│   ├── services/       # Les differents services qui feront des calls vers le module 'api'
│   ├── App.tsx     # Composant principal de l'application
│   └── ...
├── package.json  # Dépendances et scripts npm
└── ...

## Communication avec les APIs

L'interface web communique avec les APIs backend (Java et NestJS) pour gérer les données des personnages et déclencher les actions de génération. La communication se fait via des requêtes HTTP et les données sont échangées au format JSON.  La bibliothèque `axios` est utilisée pour effectuer ces requêtes, et `react-query` est utilisé pour gérer l'état des requêtes et mettre en cache les données.

Voici une description détaillée des interactions :

* **URL de base de l'API :** L'URL de base de l'API Java est définie dans la variable d'environnement `VITE_API_URL`. Si cette variable n'est pas définie, l'URL de repli est `http://localhost:8080`.

* **Services et leurs fonctions :**

    * **`getListCharactersFull.service.ts` :**
        * Utilise `useQuery` pour récupérer la liste complète des personnages depuis l'API Java.
        * Effectue une requête `GET` à l'endpoint `/characters/full`.
        * Définit l'interface `CharactersResponse` pour typer la réponse de l'API.
        * Gère la mise en cache, le chargement et les erreurs grâce à `react-query`.

        ```typescript
        import { useQuery } from 'react-query';
        import axios, { AxiosResponse } from 'axios';
        import { CharacterFull } from '@/components/model/character-full.model.tsx';

        export interface CharactersResponse {
            data: CharacterFull[];
        }

        const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

        export const getListCharactersFull = () => {
            return useQuery<AxiosResponse<CharactersResponse>, Error>(
                'charactersFull',
                async () => {
                    return await axios.get<CharactersResponse>(`${API_BASE_URL}/characters/full`);
                }
            );
        };
        ```

    * **`deleteCharacter.service.ts` :**
        * Utilise `useMutation` pour supprimer un personnage via l'API Java.
        * Effectue une requête `DELETE` à l'endpoint `/characters/{id}`.
        * Gère les erreurs et affiche un message de succès en cas de réussite.

        ```typescript
        import axios from 'axios';
        import { useMutation } from 'react-query';

        interface DeleteCharacterResponse {
            // Définir les propriétés de la réponse si nécessaire
        }

        const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'; // Fallback

        const deleteCharacterRequest = async (id: number): Promise<DeleteCharacterResponse> => {
            try {
                const response = await axios.delete(`${API_BASE_URL}/characters/${id}`);
                if (response.status !== 204) {
                    throw new Error(`Unexpected status code: ${response.status}`);
                }
                return response.data; // Retournez les données de la réponse si nécessaire
            } catch (error: any) {
                if (axios.isAxiosError(error)) {
                    console.error(`Erreur : ${error.message || 'Erreur inconnue'}`);
                    throw new Error(error.message);
                } else {
                    console.error(`Erreur : ${error.message || 'Erreur inconnue'}`);
                    throw new Error('Failed to delete character');
                }
            }
        };

        export const useDeleteCharacter = () => {
            return useMutation<DeleteCharacterResponse, Error, number>({
                mutationFn: deleteCharacterRequest,
                onSuccess: () => {
                    console.log('Personnage supprimé avec succès !');
                },
                onError: (error: Error) => {
                    console.error(`Erreur : ${error.message || 'Une erreur inconnue est survenue.'}`);
                },
            });
        };
        ```

    * **`createCharacter.service.ts` :**
        * Utilise `useMutation` pour créer un nouveau personnage en envoyant les informations de prompt à l'API Java.
        * Effectue une requête `POST` à l'endpoint `/characters/generate`.
        * La fonction `cleanData` est utilisée pour nettoyer les données avant de les envoyer à l'API.

        ```typescript
        import axios from 'axios';
        import { useMutation } from 'react-query';

        function cleanData(chaine: string | undefined): string | undefined {
            if (!chaine) return chaine;
            return chaine;
        }

        const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'; // Fallback pour le dev local

        export const useCreateCharacter = () => {
            return useMutation('characters',
                async (payloadData: {
                    promptSystem: string;
                    promptRace: string;
                    promptGender: string;
                    promptClass?: string;
                    promptDescription?: string;
                }) => {
                    const cleanedData = {
                        ...payloadData,
                        promptSystem: cleanData(payloadData.promptSystem),
                        promptRace: cleanData(payloadData.promptRace),
                        promptGender: cleanData(payloadData.promptGender),
                        promptClass: cleanData(payloadData.promptClass),
                        promptDescription: cleanData(payloadData.promptDescription),
                    };
                    return await axios.post(`${API_BASE_URL}/characters/generate`, cleanedData, {
                        headers: {
                            'Content-Type': 'application/json',
                            // Les headers CORS sont gérés par le serveur
                            // 'Access-Control-Allow-Origin': 'http://localhost:8080',
                            // 'Access-Control-Allow-Credentials': 'true',
                        },
                    });
                });
        };
        ```

    * **`illustrateCharacters.service.ts` :**
        * Utilise `useMutation` pour générer une illustration pour un personnage en communiquant avec l'API NestJS.
        * Effectue une requête `POST` à l'endpoint `/characters/illustrate`.
        * Envoie le prompt d'image à l'API pour générer l'image.

        ```typescript
        import { useMutation } from 'react-query';
        import axios from 'axios';

        const API_BASE_URL = process.env.VITE_API_URL || 'http://localhost:8080'; // Fallback

        // TODO : Permettre aux utilisateurs de générer une nouvelle illustration si l'actuelle ne leur convient pas
        export const useIllustrateCharacter = () => {
            return useMutation('illustrate', async (imagePrompt: string) => {
                return await axios.post(`${API_BASE_URL}/characters/illustrate`, imagePrompt, {
                    headers: {
                        'Content-Type': 'application/json',
                        // Les headers CORS sont gérés par le serveur
                        // 'Access-Control-Allow-Origin': 'http://localhost:8080',
                        // 'Access-Control-Allow-Credentials': 'true',
                    },
                });
            });
        };
        ```

    * **`updateCharacter.service.ts` :**
        * Fournit une fonction `updateCharacter` pour mettre à jour les détails d'un personnage via l'API Java.
        * Effectue une requête `PUT` à l'endpoint `/characters/details/{id}`.
        * Utilise des fonctions `cleanData` et `validateData` pour nettoyer et valider les données avant de les envoyer à l'API.

        ```typescript
        import axios from 'axios';
        import { CharacterFull } from '@/components/model/character-full.model.tsx';

        function cleanData(value: any): any {
            if (typeof value === 'string') {
                return value;
            }
            if (typeof value === 'object' && value !== null) {
                if (Array.isArray(value)) {
                    return value.map(item => cleanData(item));
                } else {
                    const echappe = { ...value };
                    for (const key in echappe) {
                        echappe[key] = cleanData(echappe[key]);
                    }
                    return echappe;
                }
            }
            return value;
        }

        function validateData(characterFull: CharacterFull) {
            if (!characterFull || !characterFull.details || !characterFull.details.id) {
                throw new Error('ID de personnage invalide.');
            }
            if (!characterFull.details.name) {
                throw new Error('Le nom du personnage est requis.');
            }
        }

        const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'; // Fallback

        export const updateCharacter = () => {
            const updateCharacter = async (characterFull: CharacterFull) => {
                try {
                    validateData(characterFull);
                    const cleanedData = cleanData(characterFull);
                    cleanedData.updatedAt = new Date().toISOString().slice(0, 16);
                    const response = await axios.put(`${API_BASE_URL}/characters/details/${characterFull.details.id}`, cleanedData);
                    return response.data;
                } catch (error: any) {
                    if (error.response) {
                        console.error('Erreur du serveur:', error.response.data);
                        throw new Error(`Erreur du serveur: ${error.response.status}`);
                    } else if (error.request) {
                        console.error('Aucune réponse du serveur.');
                        throw new Error('Aucune réponse du serveur.');
                    } else {
                        console.error('Erreur de requête:', error.message);
                        throw new Error('Erreur de requête.');
                    }
                }
            };
            return { updateCharacter };
        };
        ```
      
## Licence
```markdow
Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/
```