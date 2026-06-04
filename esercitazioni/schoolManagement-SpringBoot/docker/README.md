Il README dei Container/Docker
   Visto che hai creato diversi container e configurazioni, potresti creare un file specifico: Scuola/docker/README.md (o semplicemente documentarlo bene in una sezione dedicata).


1. SE AVVI PER LA PRIMA VOLTA IL PROGETTO CREA LA RETE DOCKER NET-SCUOLA
docker network create net-scuola
- Dopo averla creata verifica effettiva creazione con:
docker network ls

2. Se si avvia per la prima volta il progetto effettuare prima un clean del package ed effettuare la build del compose
   docker compose -f docker-compose-spring.yml up --build
altrimenti:
    docker compose -f docker-compose-spring.yml up

3. ìSe si vuole avviare anche il container db
    docker compose -f docker-compose-mysql.yml up
    docker compose -f docker-compose-mysql.yml up

4. In base a cosa si vuole avviare bisogna cambiare il profilo nel .env
   SPRING_PROFILES_ACTIVE=dev (per h2)
   SPRING_PROFILES_ACTIVE=mysql (per db mysql)
   SPRING_PROFILES_ACTIVE=postgres (per db postgres)

5. Per controllare container in esecuzione 
docker ps   

6. Utilizza le porte:
-8081 per Spring, 
-5432 per Postgres, 
-3306 per Mysql.

Istruzioni per pulire i volumi (postgres_data).

7. Se si avvia per la prima volta il progetto 
effettuare prima un clean del package ed effettuare la build del compose
docker compose -f docker-compose-spring.yml up --build