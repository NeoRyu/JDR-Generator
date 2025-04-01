# JDR-Generator

## Description
JDR-Generator est une application interactive pour la création de personnages RPG. Ce projet est structuré comme un monorepo contenant à la fois un backend API, développé avec NESTJS et une interface frontand, créé avec React. 
Intégrant Gemini et Imagen, IA de Google ; JDR-Generator propose des fonctionnalités avancées pour la génération de caractères automatiques, y compris des détails tels que l'historique de personnage, les caractéristiques physiques et psychologiques, ou encore une illustration du personnage basé sur le contexte généré par les IA Google Gemini et Imagen3.

## Caractéristiques principales: 
- Génération automatisée de caractères pour Jeux De Rôles
- Interface interactive pour la création et la visualisation des personnages générés à partir d'un contexte.
- Prise en charge de divers systèmes de JDR.
- Génération de personnages et illustrations via l'intégration de GoogleGenerativeAI.

  
![{C9180717-6C2A-4520-B860-2FBCFA61F561}](https://github.com/user-attachments/assets/23c22527-e6de-4025-b440-309e171b5882)
![{B1BADC8D-3446-43BD-A26F-AC6DBCE194A8}](https://github.com/user-attachments/assets/fccf5b27-6e6e-44ab-a995-9c554c0a991b)
![{69E5AA75-E60B-42E6-8BAF-DCFC49D40BD1}](https://github.com/user-attachments/assets/2bb42ac7-3e96-4718-ab91-dcacbfbb4897)
![{8AF7F57D-3C10-4781-A52F-7639863A895A}](https://github.com/user-attachments/assets/f7954fcc-f83d-4894-8e3e-6685b6207cb7)
![{E5999697-1151-40FC-B121-D3C506EA4232}](https://github.com/user-attachments/assets/b2c59561-9e4e-440d-a2a4-0c4882eb1a5b)




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
