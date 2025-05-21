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

# Télécharger et installer le plugin Credentials Binding (pour la commande 'withUser' dans le Jenkinsfile)
RUN curl -fsSL "https://updates.jenkins.io/download/plugins/credentials-binding/687.v619cb_15e923f/credentials-binding.hpi" -o /usr/share/jenkins/ref/plugins/credentials-binding.jpi \
    && jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins/credentials-binding.jpi

# Revenir à l'utilisateur jenkins par défaut
USER jenkins

# docker stop jenkins-container
# docker rm jenkins-container
# docker build -t eli256/jenkins-docker-image .
# docker run -d --name jenkins-container -p 8080:8080 -p 50000:50000 -v /var/run/docker.sock:/var/run/docker.sock -v /c/Users/fredericcoupez/IdeaProjects/JDR-Generator/.jenkins:/var/jenkins_home eli256/jenkins-docker-image