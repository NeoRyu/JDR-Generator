# JDR-Generator

## Description
JDR-Generator est une application interactive pour la création de personnages RPG. Ce projet est structuré comme un
monorepo tournant en localhost ; contenant à la fois deux API backend, l'un développé en JAVA (intégration de SCALA
en parallèle pour l'excerice) permettant de communiquer avec la database MySQL ajoutée via FlywayDb,
l'autre avec NESTJS pour communiquer avec les API de Google Gemini, et une interface frontend en React Typescript.

## Caractéristiques principales: 
- Génération automatisée de caractères pour Jeux De Rôles
- Interface interactive pour la création et la visualisation des personnages générés à partir d'un contexte.
- Prise en charge de divers systèmes de JDR.
- Génération de personnages et illustrations via l'intégration de GoogleGenerativeAI.

## START

### Pré-requis
- JAVA 17
- maven

### Installation
1. Clone le repository :
```bash
git clone https://github.com/NeoRyu/JDR-Generator.git
```
```bash
cd JDR-Generator
```

- Synchroniser le projet Maven pour telecharger les dependances
- Lancer ensuite le service maven "build (API)" : clean install package
- Ne reste plus qu'a run l'application java : jdr.generator.api.ApiApplication