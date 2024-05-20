# Military Store App

## Description
The main responsibility of this application is to provide for users the ability to find and buy military equipment.

## Technologies and Frameworks
- Java 21
- Gradle 8.6
- Spring Boot 3.2.3
- PostgreSQL 42.7.2

## How To Run Project Locally (with docker DB)
1. Build the project
```shell
./gradlew build
```

2. Run the project
```shell
java -Dspring.profiles.active=local -jar ./application/build/libs/application.jar
```

## How To Run Project inside Docker
1. Assemble jar file
```shell
./gradlew :application:bootJar
```

2. Run app inside docker container
```shell
docker-compose -f docker-compose-full-app.yaml up
```

## How To Run Project from Docker Hub
1. Login to Docker Hub
```shell
docker login
```

2. Pull the image from Docker Hub
```shell
docker pull seryjk452/military-store
```

3. Run the image
```shell
docker run -e "SPRING_PROFILES_ACTIVE=dev" -p 8080:8080 seryjk452/military-store
```

## How To Run Tests
```shell
./gradlew test
```

## How To Run Docker Vault Secret Manager
1. Login to Docker Hub
```shell
docker login
```

2. Pull the vault image from Docker Hub
```shell
docker pull seryjk452/military-store:vault
```

3. Run the vault image
```shell
docker run -d -p 8200:8200 seryjk452/military-store:vault
```

4. Run the command to initialize the vault secrets
```shell
docker exec -it $(docker ps -q) /bin/sh -c 'vault kv put secret/military-store-app/dev DB_URL=$DATABASE_URL DB_PASSWORD=$DATABASE_PASSWORD DB_USER=$DATABASE_USER'
```

5. Run the project
```shell
java -Dspring.profiles.active=dev -jar ./application/build/libs/application.jar
```

## How to store secrets to the Vault Secret Manager
1. Open the 'docker-compose-vault.yaml' file and add the secrets as environment variables


2. Run docker-compose-vault
```shell
docker-compose -f docker-compose-vault.yaml up
```

3. Commit the docker image
```shell
docker commit $(docker ps -q) vault
```

4. Tag the image
```shell
docker tag vault seryjk452/military-store:vault
```

5. Push the image to Docker Hub
```shell
docker push seryjk452/military-store:vault
```

6. Update command #4 in the 'How To Run Docker Vault Secret Manager' section with new secrets