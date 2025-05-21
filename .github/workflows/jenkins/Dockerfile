# Utilise l'image Jenkins LTS officielle comme base
FROM jenkins/jenkins:lts-jdk17

# Passer en utilisateur root pour installer Docker CLI
USER root

# Installer les dépendances nécessaires et le Docker CLI
RUN apt-get update \
    && apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release \
    && curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg \
    && echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list \
    && apt-get update \
    && apt-get install -y docker-ce-cli \
    && rm -rf /var/lib/apt/lists/*

# Créer le répertoire des plugins s'il n'existe pas et s'assurer des permissions
RUN mkdir -p /usr/share/jenkins/ref/plugins \
    && chmod 777 /usr/share/jenkins/ref/plugins

# Télécharger et installer le plugin Credentials Binding (pour la commande 'withUser' dans le Jenkinsfile)
# Utilisez un répertoire temporaire pour le téléchargement
RUN curl -fsSL "https://updates.jenkins.io/download/plugins/credentials-binding/687.v619cb_15e923f/credentials-binding.hpi" -o /tmp/credentials-binding.jpi

# Installer le plugin depuis le fichier téléchargé
RUN jenkins-plugin-cli --plugin-file /tmp/credentials-binding.jpi

# Nettoyer le fichier temporaire
RUN rm /tmp/credentials-binding.jpi

# Revenir à l'utilisateur jenkins par défaut
USER jenkins