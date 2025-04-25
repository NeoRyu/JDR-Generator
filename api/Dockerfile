# --- Étape 1: Build (avec Maven) ---
# Utilise une image base avec Maven et JDK 17
FROM maven:3.8.5-openjdk-17 AS builder

# Définit le répertoire de travail dans le conteneur de build
WORKDIR /app

# Copie les fichiers pom.xml et le répertoire .mvn
COPY pom.xml .mvn/ ./

# Télécharge les dépendances Maven
# Cela sera mis en cache lors des builds suivants si le pom.xml ne change pas)
RUN mvn dependency:go-offline -B -pl .

# Copie le code source de l'API
COPY src ./src

# Package l'application Spring Boot avec Maven
RUN mvn clean package -DskipTests -B -pl .

# --- Étape 2: Runtime (image finale légère) ---
# Utilise une image base Java avec JRE 17 alternative (Eclipse Temurin Alpine)
FROM eclipse-temurin:17-jre-alpine

# Définit le répertoire de travail dans le conteneur final
WORKDIR /app

# Copie le fichier JAR compilé depuis l'étape 'builder' vers l'image finale
COPY --from=builder /app/target/*.jar app.jar

# Définit la commande pour exécuter l'application Spring Boot
CMD ["java", "-jar", "app.jar"]

# Expose le port sur lequel l'API s'exécute
EXPOSE 8080