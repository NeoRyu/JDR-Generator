# API GoogleGenerativeAI

Ce projet inclut le code source pour interagir avec Gemini à partir d'une API REST de base développée en Node.js avec Express et TypeScript.

## Configuration requise

1. Installer Node.js [https://nodejs.org/en/download/](https://nodejs.org/en/download/)
2. Installer TypeScript

```sh
npm install -g typescript
```

3. Installer les dépendances requises

```sh
npm install
```

4. Obtenir une clé API Gemini : [https://ai.google.dev/gemini-api/docs/pricing?hl=fr](https://ai.google.dev/gemini-api/docs/pricing?hl=fr)
5. Mettre à jour votre fichier `.env` avec votre clé API (https://aistudio.google.com/apikey)

## Générer et exécuter

Pour supprimer le dossier dist avant de le rebuild :
```sh
rd /s /q dist
```

Pour générer du code JS à partir du code source TypeScript.
```sh
tsc
```

Pour démarrer le serveur depuis le dossier compilé (/dist).

```sh
node dist/app.js
```

## Tester les endpoints

Executer une requête POST sur http://localhost:3000/generate en ajoutant dans le body en raw :
```json
{ "prompt" : "You are an expert in RPGs, with extensive knowledge of various gaming systems, such as Dungeons & Dragons, Pathfinder, World of Darkness, Call of Cthulhu, Warhammer Fantasy Roleplay, Shadowrun, GURPS, and Fate. Your role is to create characters. You will be informed about the game system, the character's race, class, and perhaps some description to inspire you. Your response needs to be only in JSON format with the following data filled in, and the texts need to be in French (fr-FR), except for the name: example answer: ''' { 'name': 'Eldric Ironhand', 'âge': 28, 'birthPlace': 'Citadelle d'Ironcrest', 'childhoodStory': 'J'ai grandi dans une famille de forgerons, fasciné par les armes et les armures.', 'feelingsAboutParents': 'Respecte profondément, en particulier son père, un forgeron renommé.', 'parentsAlive': true, 'detailsAboutParents': 'Le père est un maître forgeron ; la mère est une artisane qualifiée.', 'siblings': 'Une sœur cadette, apprentie alchimiste.', 'youthFriends': 'Divers, principalement d'autres apprentis d'artisans locaux.', 'maritalStatus': 'Célibataire', 'conjugalHistory': 'Quelques romances passagères, rien de grave.', 'enfants': 'Aucun', 'education': 'Éducation de base, mais formation approfondie au combat et à la forge.', 'profession': 'Guerrier et forgeron', 'reasonForProfession': 'Passion pour les armes et désir d'honorer la tradition familiale.', 'physicalDescription': 'Grand, musclé, cheveux bruns courts, yeux verts, avec des cicatrices de bataille.', 'distinctiveTrait': 'Extrêmement doué en forgeage et en combat.', 'goal': 'Devenez un héros légendaire et forgez une arme unique.', 'reasonForGoal': 'Désir de prouver votre valeur et vos capacités.', 'planIfSuccessful': 'Fondez votre propre école de guerriers et de forgerons.', 'planIfFailed': 'Retournez à la citadelle et continuez en tant que forgeron.', 'biggestObstacle': 'Prouvez votre valeur dans un monde plein de héros et d'aventures.', 'overcomingObstacle': 'Dévouement implacable à l'entraînement et aux missions.', 'changeInWorld': 'Je veux inspirer les autres à poursuivre leurs rêves.', 'changeInSelf': 'J'espère trouver votre véritable objectif et vos capacités.', 'fears': 'Être oublié ou considéré comme médiocre.', 'selfDescription': 'Un guerrier dévoué avec un cœur d'artisan.', 'attitudeTowardsWorld': 'Optimiste et stimulant.', 'attitudeTowardsPeople': 'Amical mais prudent.', 'attitudeTowardsGroups': 'Préfère l'action individuelle, mais respecte la force des groupes.', 'leisureActivities': 'Forge, chasse, jeux de stratégie.', 'clothingPreferences': 'Habillez-vous de manière pratique, en préférant les vêtements qui permettent de bouger facilement.', 'workPreferences': 'Travaux impliquant des défis physiques et stratégiques.', 'favoriteFood': 'Ragoût de bœuf en morceaux.', 'hobbies': 'Forge, entraînement au combat, échecs.', 'pet': 'Un chien de chasse robuste nommé Brawn.', 'idealCompany': 'Des compagnons fidèles et stimulants.', 'typeOfLover': 'Quelqu'un de fort, indépendant et passionné.', 'residenceLocation': 'Une maison modeste dans la Citadelle d'Ironcrest.', 'climat': 'Tempéré, avec des hivers rigoureux.', 'reasonForResidence': 'Proximité de la famille et accès à la forge familiale.', 'commonProblems': 'Défis dans la forge, rivalités locales.', 'dailyRoutine': 'S'entraîner, forger, passer du temps avec ses amis et sa famille.', 'image': 'Un homme grand et musclé aux cheveux bruns et aux yeux verts, portant une armure pratique et tenant un marteau de forge.' } ''' " }
```

Executer une requête POST sur http://localhost:3000/illustrate en ajoutant dans le body en raw :
```json
{ "prompt" : "Using Imagen3, generate an illustration in a heroic-fantasy style, but not realistic, close to that of the French illustrator Grosnez (https://www.artstation.com/grosnez) based on this prompt: Une femme élégante aux longs cheveux bruns et aux yeux verts, portant un chapeau d'aventurière et tenant une carte ancienne." }
```