# cd C:\Users\fredericcoupez\IdeaProjects\JDR-Generator\.github\workflows\jenkins\agent
# docker build -t eli256/jenkins-docker-image-agent:latest .
# cd C:\Users\fredericcoupez\IdeaProjects\JDR-Generator\.github\workflows\jenkins
# docker build -t eli256/jenkins-docker-image .
# docker push eli256/jenkins-docker-image:latest
# docker run -d -p 9000:8080 -p 50000:50000 --group-add 999 --name jenkins-container -v /c/Users/fredericcoupez/IdeaProjects/JDR-Generator/.jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock eli256/jenkins-docker-image

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

# Créer le groupe 'docker' s'il n'existe pas, puis ajouter l'utilisateur 'jenkins'
RUN groupadd -r docker || true \
    && usermod -aG docker jenkins

# Ajouter l'utilisateur 'jenkins' au groupe 'root'
# Ceci donne à l'utilisateur jenkins les permissions d'accéder au socket docker sur l'hôte.
RUN usermod -aG root jenkins

# Revenir à l'utilisateur jenkins par défaut
USER jenkins