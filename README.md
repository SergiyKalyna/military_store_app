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
