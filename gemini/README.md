# JDR-Generator

## Description
JDR-Generator est une application interactive pour la création de personnages RPG. Ce projet est structuré comme un
monorepo tournant en localhost ; contenant à la fois deux API backend, l'un développé en JAVA pour communiquer avec la
database MySQL ajoutée via FlywayDb, l'autre avec NESTJS pour communiquer avec les API de Google Gemini, et une
interface frontend en React Typescript.

## Caractéristiques principales:
- Génération automatisée de caractères pour Jeux De Rôles
- Interface interactive pour la création et la visualisation des personnages générés à partir d'un contexte.
- Prise en charge de divers systèmes de JDR.
- Génération de personnages et illustrations via l'intégration de GoogleGenerativeAI.

# API : Google Generative AI

**Ce projet inclut le code source pour interagir avec Gemini à partir d'une API REST de base développée en Node.js avec Express et TypeScript.**

## Configuration requise

1. Installer Node.js : [https://nodejs.org/en/download/](https://nodejs.org/en/download/)

2. Installer TypeScript
    ```sh
    npm install -g typescript
    ```

3. Installer les dépendances requises
    ```sh
    npm install
    ```

4. Obtenir une clé d'API GOOGLE pour Gemini : [https://ai.google.dev/gemini-api/docs/pricing?hl=fr](https://ai.google.dev/gemini-api/docs/pricing?hl=fr)

5. Mettre à jour votre fichier `.env` avec votre clé API (https://aistudio.google.com/apikey) et votre username de connection windows.

6. Ajouter un dossier nommé `jdr-generator` (ou tout autre nom que vous définirez dans le fichier `.env` pour `DOWNLOAD_FOLDER`) dans votre dossier de téléchargement 
(`C:\Users\`{windows_username}`\Downloads\jdr-generator\`)
   > Pour choisir un autre emplacement, modifiez les variables `pathSrc` dans les fichiers du dossier 'controllers'

## Générer et exécuter

- Pour supprimer le dossier `dist` (nécessaire avant un rebuild) :
    ```sh
    rd /s /q dist
    ```

- Pour compiler le code JS à partir du code source TypeScript.
    ```sh
    tsc
    ```

- Pour démarrer le serveur depuis le dossier compilé (`dist`).
    ```sh
    node dist/app.js
    ```

## Tester les endpoints

- Exécuter une requête POST sur http://localhost:3000/generate pour obtenir le JSON d'un personnage généré depuis Gemini
  > A noter que les JSON et images seront ajoutés dans le dossier configuré dans le fichier `.env`

- Exécuter une requête POST sur http://localhost:3000/illustrate en ajoutant dans le body en raw le contenu de l'attribut "image" obtenu précédemment pour générer une illustration, exemple :
    ```json
    { "prompt" : "Using Imagen3, generate an illustration in a heroic-fantasy style, but not realistic, close to that of the French illustrator Grosnez (https://www.artstation.com/grosnez) based on this prompt: Une femme élégante aux longs cheveux bruns et aux yeux verts, portant un chapeau d'aventurière et tenant une carte ancienne." }
    ```

- Exécuter une requête POST sur http://localhost:3000/stats en ajoutant dans le body en raw un contenu plus complexe basé sur les détails obtenus précédemment pour générer un json contenant diverses traits de statistiques, exemple :
    ```json
    { "prompt" : "promptSystem: 'context.PromptSystem', promptRace: 'context.PromptRace', promptGender: 'context.PromptGender', promptClass: 'context.PromptClass', promptDescription: 'context.PromptDescription', name: 'details.Name', age: 'details.Age', education: 'details.Education', profession: 'details.Profession', reasonForProfession: 'details.ReasonForProfession', workPreferences: 'details.WorkPreferences', changeInSelf: 'details.ChangeInSelf', changeInWorld: 'details.ChangeInWorld', goal: 'details.Goal', reasonForGoal: 'details.ReasonForGoal'" }
    ```

## Exemple de génération avec un contexte personnalisé :

L'utilisateur saisi ce contexte lors de la création d'un nouveau personnage :
```json
{
  "prompt_system": "Dungeons & Dragons",
  "prompt_race": "Gobelin",
  "prompt_gender": "Male",
  "prompt_class": "Artificier",
  "prompt_description": Banni de sa communauté pour ses expériences explosives et son ouverture d'esprit aux autres éspèces,  ce gobelin plus intelligent que la moyenne a récemment rejoint la civilisation humaine. De par ses origines, il n'est pas apprécié par la population locale de son nouveau lieu de résidence, et les autorités le confondent souvent avec un ennemis. Il a trouvé refuge dans une maison abandonné en périphérie de la ville, dans l'attente de compagnons qui sauront apprécier ses talents et ne s'arrêteront pas à ses origines."
}
```

L'IA se base sur ce contexte pour générer les détails du background du personnage :
```json
{
    "name": "Zik Zapboum",
    "age": 17,
    "birthPlace": "Un terrier gobelin oublié, quelque part dans les collines.",
    "residenceLocation": "Une maison abandonnée à la périphérie de la ville.",
    "reasonForResidence": "Refuge après avoir été banni de sa communauté et cherchant un endroit où ses talents sont appréciés.",
    "climate": "Variable, mais préfère les environnements tempérés pour éviter les problèmes d'humidité avec ses inventions.",
    "commonProblems": "Préjugés des habitants, confusions avec des ennemis, manque de ressources pour ses expériences.",
    "dailyRoutine": "Récupérer des matériaux, bricoler, expérimenter, éviter les ennuis avec les autorités locales.",
    "parentsAlive": false,
    "detailsAboutParents": "Probablement morts dans une explosion ou mangés par quelque chose de plus gros. Zik ne s'en souvient pas très bien.",
    "feelingsAboutParents": "Indifférent. Les gobelins ne s'attachent pas beaucoup à leurs parents.",
    "siblings": "Beaucoup de frères et sœurs, mais il ne les connaît pas.",
    "childhoodStory": "A toujours été fasciné par les choses qui explosent et les mécanismes complexes, ce qui l'a conduit à être banni.",
    "youthFriends": "Aucun. Les autres gobelins le trouvaient bizarre et dangereux.",
    "pet": "Un rat mécanique qu'il a construit, nommé 'Clac'.",
    "maritalStatus": "Célibataire",
    "typeOfLover": "Quelqu'un d'intelligent, d'ouvert d'esprit et qui ne le juge pas sur son apparence.",
    "conjugalHistory": "Aucune. Les relations amoureuses ne sont pas une priorité pour lui.",
    "children": 0,
    "education": "Autodidacte. A appris en observant, en expérimentant et en volant des livres.",
    "profession": "Artificier (en herbe)",
    "reasonForProfession": "Passion pour la création et l'amélioration des choses.",
    "workPreferences": "Travaux impliquant de la créativité, de la résolution de problèmes et des explosions contrôlées.",
    "changeInSelf": "Devenir un artificier reconnu et respecté, malgré ses origines.",
    "changeInWorld": "Créer des inventions qui améliorent la vie des gens (ou qui font boum de manière spectaculaire).",
    "goal": "Construire l'invention ultime, qui prouvera sa valeur au monde.",
    "reasonForGoal": "Désir de prouver qu'il est plus qu'un simple gobelin.",
    "biggestObstacle": "Le manque de ressources, les préjugés et sa propre tendance à faire exploser les choses.",
    "overcomingObstacle": "Persévérance, ingéniosité et une bonne dose de chance.",
    "planIfSuccessful": "Ouvrir un atelier et partager ses connaissances avec d'autres.",
    "planIfFailed": "Retourner dans les collines et recommencer à bricoler en secret.",
    "selfDescription": "Un gobelin intelligent, excentrique et un peu fou, avec un talent pour les explosions.",
    "distinctiveTrait": "Des cheveux verts hérissés à cause de ses expériences électriques.",
    "physicalDescription": "Petit, peau verte, yeux jaunes brillants, toujours couvert de suie et de graisse.",
    "clothingPreferences": "Vêtements pratiques et résistants, avec beaucoup de poches pour ranger ses outils et ses composants.",
    "fears": "Être incompris, retourner à la vie misérable d'un gobelin sans intérêt.",
    "favoriteFood": "Champignons (de préférence ceux qui brillent dans le noir).",
    "leisureActivities": "Bricoler, lire des livres sur la science et la magie, observer les réactions chimiques.",
    "hobbies": "Créer de nouvelles inventions, collectionner des pièces détachées, faire des expériences explosives (en toute sécurité, bien sûr).",
    "idealCompany": "Des compagnons aventureux, intelligents et tolérants, qui apprécient son génie (et ses explosions).",
    "attitudeTowardsGroups": "Au début méfiant, mais loyal envers ceux qui gagnent sa confiance.",
    "attitudeTowardsWorld": "Curieux et optimiste, malgré les difficultés.",
    "attitudeTowardsPeople": "Prudent, mais espère trouver des amis et des alliés.",
    "image": "Un gobelin vert, couvert de suie, avec des cheveux hérissés et des lunettes de protection sur le nez, tenant un étrange appareil fumant."
}
```

Après génération de l'illustration par l'IA basée sur l'attribut "image" dans les details, l'IA génère des stats basées sur le contexte et plusieurs attributs précédemment générés dans les détails :
```json
{
    "nom": "Zik Zapboum",
    "race": "Gobelin",
    "classe": "Artificier",
    "niveau": 1,
    "alignement": "Chaotique Neutre",
    "points_de_vie": 9,
    "jets_de_des_de_vie": "1d8",
    "bonus_de_maîtrise": "+2",
    "statistiques": {
        "force": 8,
        "dexterite": 14,
        "constitution": 13,
        "intelligence": 15,
        "sagesse": 10,
        "charisme": 12
    },
    "modificateurs": {
        "force": "-1",
        "dexterite": "+2",
        "constitution": "+1",
        "intelligence": "+2",
        "sagesse": "+0",
        "charisme": "+1"
    },
    "jets_de_sauvegarde": {
        "force": "-1",
        "dexterite": "+2",
        "constitution": "+1",
        "intelligence": "+4",
        "sagesse": "+0",
        "charisme": "+1"
    },
    "compétences": {
        "acrobaties": "+2",
        "arcane": "+4",
        "athletisme": "-1",
        "discretion": "+4",
        "histoire": "+2",
        "investigation": "+4",
        "manipulation_des_outils": "+4 (Outils de bricoleur)",
        "nature": "+2",
        "perception": "+0",
        "persuasion": "+1"
    },
    "langues": [
        "Commun",
        "Gobelin"
    ],
    "équipement": [
        "Arbalète légère",
        "20 carreaux",
        "Un paquet d'outils de bricoleur",
        "Un poignard",
        "Un sac à dos",
        "Une couverture",
        "Une boîte à amadou",
        "10 torches",
        "Une gourde",
        "50 pieds de corde de chanvre",
        "Un jeu d'osselets",
        "Un costume de voyage",
        "15 pièces d'or"
    ],
    "capacités_de_classe": [
        "Infusion d'artisanat",
        "Connaissance de l'artisanat",
        "Outils requis : Pour lancer un sort d'artificier, vous devez posséder des outils de bricoleur ou d'artisanat dans votre main libre."
    ],
    "sorts_connus": [
        "Réparation",
        "Lumière",
        "Trait de givre"
    ],
    "infusions_connues": [
        "Arme infusée",
        "Armure infusée"
    ],
    "sous_classe": "Artificier Alchimiste",
    "capacités_de_sous_classe": [
        "Distillateur expérimental",
        "Bouteille alchimique"
    ],
    "notes": "Zik Zapboum a une affinité particulière pour les explosifs et les gadgets qui font 'boum'. Il est maladroit en société mais brillant dans son atelier."
}
```

Les stats sont stockés sous format JSON dans la base de données car les attributs peuvent (et seront certainement) différent d'une génération à une autre, le contexte du systeme de jeu étant le principal facteur qui déterminera la génération de ce json complexe.