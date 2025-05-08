# Définit la variable d'environnement pour activer le profil Docker de Spring
ENV SPRING_PROFILES_ACTIVE=docker

# Utilise l'image Maven avec OpenJDK 17 comme environnement de build (nommé 'builder')
FROM maven:3.8.5-openjdk-17 AS builder

# Définit le répertoire de travail à l'intérieur du conteneur de build
WORKDIR /app

# Copie le répertoire .mvn pour la configuration Maven
COPY .mvn .mvn/

# Copie le fichier pom.xml pour gérer les dépendances et le build
COPY pom.xml ./

# Copie le code source de l'application Java
COPY src ./src

# Exécute la commande Maven pour nettoyer le projet et construire le package JAR, en sautant les tests
RUN mvn clean package -DskipTests

# Utilise l'image JRE OpenJDK 17 en version alpine pour l'environnement d'exécution (plus légère)
FROM eclipse-temurin:17-jre-alpine

# Définit le répertoire de travail à l'intérieur du conteneur d'exécution
WORKDIR /app

# Met à jour les packages et installe curl (utile pour les tests ou la surveillance)
RUN apk update && apk add curl

# Copie le fichier JAR construit lors de l'étape 'builder' vers le conteneur d'exécution
COPY --from=builder /app/target/*.jar app.jar

# Expose le port sur lequel l'application Java s'exécutera
EXPOSE 8080

# Définit la commande à exécuter au démarrage du conteneur
ENTRYPOINT ["java", "-jar", "app.jar"]