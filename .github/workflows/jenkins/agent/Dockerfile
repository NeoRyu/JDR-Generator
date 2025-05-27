# docker build -t jdr-generator-jenkins-agent:latest .
FROM amazoncorretto:17-alpine-jdk

# Installer Git (pour que la stage 'GIT - Checkout Code' fonctionne pleinement dans le conteneur)
RUN apk add --no-cache git

# Installer Node.js (version 20.11.1)
# > Étape 1 : Téléchargement et extraction
ENV NODE_VERSION=20.11.1
RUN apk add --no-cache --virtual .build-deps python3 make gcc g++ \
    && wget -qO- https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}-linux-x64.tar.gz | tar xzf - -C /usr/local --strip-components=1 \
    && apk del .build-deps
# > Étape 2 : Mettre à jour le PATH et installer npm@latest ( /usr/local/bin -> PATH )
ENV PATH="/usr/local/bin:${PATH}"
RUN npm install -g npm@latest

# Installer Maven (version 3.8.5)
ENV MAVEN_VERSION=3.8.5
ENV MAVEN_HOME=/usr/local/apache-maven
ENV PATH="${MAVEN_HOME}/bin:${PATH}"
RUN wget -qO- https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xzf - -C /usr/local \
    && mv /usr/local/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME}

# Vérifications (pour s'assurer que tout est bien installé)
RUN java -version
RUN mvn -version
RUN node -v
RUN npm -v

# L'utilisateur par défaut de amazoncorretto:17-alpine-jdk est root.
# Pour avoir un utilisateur non-root pour les builds Jenkins, vous devrez le créer/définir ici :
# RUN addgroup -S jenkins && adduser -S jenkins -G jenkins
# USER jenkins