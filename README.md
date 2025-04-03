# JDR-Generator

## Description
JDR-Generator est une application interactive pour la création de personnages RPG. Ce projet est structuré comme un monorepo contenant à la fois un backend API, développé avec NESTJS et une interface frontand, créé avec React. 
Intégrant Gemini et Imagen, IA de Google ; JDR-Generator propose des fonctionnalités avancées pour la génération de caractères automatiques, y compris des détails tels que l'historique de personnage, les caractéristiques physiques et psychologiques, ou encore une illustration du personnage basé sur le contexte généré par les IA Google Gemini et Imagen3.

## Caractéristiques principales: 
- Génération automatisée de caractères pour Jeux De Rôles
- Interface interactive pour la création et la visualisation des personnages générés à partir d'un contexte.
- Prise en charge de divers systèmes de JDR.
- Génération de personnages et illustrations via l'intégration de GoogleGenerativeAI.

![{A9C4900F-274B-451F-8097-A1AAAA3B500F}](https://github.com/user-attachments/assets/d60becac-93b7-4940-a3fc-5e1d26516053)
![{A8D02D44-A30A-41B4-A562-6219D38B19C1}](https://github.com/user-attachments/assets/78b3df51-f65c-483c-9311-a40c00f8344c)
![{E8893894-CADA-4F5E-96FE-9C47959FE2E9}](https://github.com/user-attachments/assets/58a7e538-0037-4ea4-93eb-a61fd597e1c1)
![{CE9E8BF3-B49E-4F1D-9233-77F1BD3D2E04}](https://github.com/user-attachments/assets/9781b1b9-d458-491f-9edd-7d566c5b3536)
![{20240EB7-0A44-45C2-9244-2EBCF16F60B5}](https://github.com/user-attachments/assets/35336cfb-e2b8-445d-905a-cbe03c6d761a)
![{90DCBF62-C6FC-4F1F-AEFD-F95E6F257B82}](https://github.com/user-attachments/assets/e1bfb0bd-8f70-4b66-b7b3-dda68f52739a)


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
