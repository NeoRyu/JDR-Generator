# JDR-Generator

## Description
JDR-Generator est une application interactive pour la création de personnages RPG. Ce projet est structuré comme un monorepo contenant à la fois un backend API, développé avec NESTJS et une interface frontand, créé avec React. 
Intégrant Gemini et Imagen, IA de Google ; JDR-Generator propose des fonctionnalités avancées pour la génération de caractères automatiques, y compris des détails tels que l'historique de personnage, les caractéristiques physiques et psychologiques, ou encore une illustration du personnage basé sur le contexte généré par les IA Google Gemini et Imagen3.

## Caractéristiques principales: 
- Génération automatisée de caractères pour Jeux De Rôles
- Interface interactive pour la création et la visualisation des personnages générés à partir d'un contexte.
- Prise en charge de divers systèmes de JDR.
- Génération de personnages et illustrations via l'intégration de GoogleGenerativeAI.

![{6C47D527-687B-4E6E-B699-67C900E966C3}](https://github.com/user-attachments/assets/954b6357-45af-4fcd-a839-1ac9822b3d92)


## START

### Pré-requis
- Node.js
- npm ou yarn

### Installation
1. Clone le repository :
```bash
git clone https://github.com/NeoRyu/JDR-Generator.git
```
```bash
cd JDR-Generator
```

2. Installez les dépendances de l'API:
```bash
cd ./gemini
```
```bash
npm install
```

3. Installez les dépendances WEB :
```bash
cd ../web
```
```bash
npm install
```

4. Définissez les variables d'environnement selon les besoins dans les deux répertoires **(API et Web)**.

### Exécution du projet 
1/ Pour exécuter l'API communiquant avec Gemini & Imagen:
  
- "clean": 
```bash
rd /s /q dist
```
- "build": 
```bash
tsc
```
- "start": 
```bash
node dist/app.js
```

2/ Pour exécuter l'interface WEB permettant de définir le contexte et afficher le rendu généré par les IA :

```bash
cd web
```
```bash
npm run start:dev
```
