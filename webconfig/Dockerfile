FROM maven:3.9-amazoncorretto-21-debian-bookworm AS builder
WORKDIR /app

COPY pom.xml .
COPY commons/pom.xml commons/
COPY product/pom.xml product/
COPY webconfig/pom.xml webconfig/
COPY auth/pom.xml auth/

# prefetch dependencies
RUN mvn dependency:go-offline -B

# copy source
COPY commons/src commons/src
COPY webconfig/src webconfig/src

# build service
RUN mvn clean install -Dspring.profiles.active=prod -pl webconfig -am

# run image
FROM openjdk:21-slim-bullseye
WORKDIR /app
COPY --from=builder /app/webconfig/target/*.jar ./app.jar
CMD ["java", "-jar", "./app.jar", "--spring.profiles.active=prod"]