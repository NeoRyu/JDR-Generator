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

# Vérifications (pour s'assurer que tout est bien installé)
RUN java -version
RUN mvn -version
RUN node -v
RUN npm -v
RUN docker --version

# L'utilisateur par défaut de amazoncorretto:17-alpine-jdk est root.
# Pour avoir un utilisateur non-root pour les builds Jenkins, vous devrez le créer/définir ici :
# RUN addgroup -S jenkins && adduser -S jenkins -G jenkins
# USER jenkins