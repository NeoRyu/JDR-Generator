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

# Télécharger et rendre exécutable le script install-plugins.sh
RUN curl -fsSL https://raw.githubusercontent.com/jenkinsci/docker/master/install-plugins.sh -o /usr/local/bin/install-plugins.sh \
    && chmod +x /usr/local/bin/install-plugins.sh

# Ajouter le plugin Credentials Binding via le fichier plugins.txt et le script install-plugins.sh
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

# Exécuter le script d'installation des plugins
# Jenkins fournit un script par défaut pour installer les plugins listés dans plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# Revenir à l'utilisateur jenkins par défaut
USER jenkins