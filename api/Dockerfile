FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY .mvn .mvn/
COPY pom.xml ./
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk update && apk add curl
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]