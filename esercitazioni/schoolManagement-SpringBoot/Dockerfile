# STAGE 1: Build
# Usiamo un'immagine con Maven e Java per compilare il progetto
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamo il file pom.xml e scarichiamo le dipendenze
# Questo passaggio viene "cacheato" da Docker: se non cambi il pom, non riscarica tutto ogni volta
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamo il codice sorgente e compiliamo il pacchetto .jar
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Run
# Usiamo un'immagine molto più leggera (JRE) solo per far girare l'app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Prendiamo solo il file .jar generato nello stage precedente
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Avviamo l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]