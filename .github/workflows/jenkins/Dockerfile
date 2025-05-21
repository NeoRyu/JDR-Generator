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

#  Copier le fichier plugins.txt dans le conteneur
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
# Exécuter jenkins-plugin-cli pour installer les plugins listés dans plugins.txt
# L'outil jenkins-plugin-cli est inclus dans l'image jenkins/jenkins:lts-jdk17
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt \
    && echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state

# Revenir à l'utilisateur jenkins par défaut
USER jenkins