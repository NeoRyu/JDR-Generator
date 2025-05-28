# docker build -t eli256/jenkins-docker-image-agent:latest .
# docker push eli256/jenkins-docker-image-agent:latest
FROM amazoncorretto:17-alpine-jdk

# Installer Git (pour que la stage 'GIT - Checkout Code' fonctionne pleinement dans le conteneur)
RUN apk add --no-cache git

# Installer les versions de Node.js et npm via apk disponible dans les dépôts Alpine.
RUN apk add --no-cache nodejs npm

# Installer Maven (version 3.8.5)
ENV MAVEN_VERSION=3.8.5
ENV MAVEN_HOME=/usr/local/apache-maven
ENV PATH="${MAVEN_HOME}/bin:${PATH}"
RUN wget -qO- https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xzf - -C /usr/local \
    && mv /usr/local/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME}

# Installer le client Docker CLI
RUN apk add --no-cache docker

# Créer l'utilisateur 'jenkins' avec l'UID 1000
RUN addgroup -S jenkins && adduser -S jenkins -G jenkins -u 1000
# Ajoutez un groupe avec le GID du groupe Docker sur l'hôte (999), et ajoutez l'utilisateur jenkins à ce groupe.
RUN addgroup -S docker_agent_group -g 999 \
    && adduser jenkins docker_agent_group

# Passer à l'utilisateur 'jenkins' pour les commandes suivantes
USER jenkins

# Vérifications
RUN java -version
RUN mvn -version
RUN node -v
RUN npm -v
RUN docker --version
