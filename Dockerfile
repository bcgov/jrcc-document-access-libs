FROM maven:3.8.6-eclipse-temurin-8-focal AS dependencies

WORKDIR /app

COPY pom.xml pom.xml
COPY jrcc-access-api/pom.xml jrcc-access-api/pom.xml
COPY jrcc-access-spring-boot-autoconfigure/pom.xml jrcc-access-spring-boot-autoconfigure/pom.xml
COPY jrcc-access-spring-boot-starter/pom.xml jrcc-access-spring-boot-starter/pom.xml
COPY jrcc-document-access-libs/pom.xml jrcc-document-access-libs/pom.xml

RUN mvn dependency:go-offline -DskipTests --fail-never

FROM dependencies as build

WORKDIR /app

COPY jrcc-access-api/jrcc.swagger.yml jrcc-access-api/jrcc.swagger.yml
COPY jrcc-access-spring-boot-autoconfigure/src jrcc-access-spring-boot-autoconfigure/src
COPY jrcc-document-access-libs/src jrcc-document-access-libs/src

RUN mvn package -DskipTests

FROM eclipse-temurin:8-jre-jammy

WORKDIR /app

COPY --from=build /app/jrcc-access-api/target/jrcc-document-api*.jar jrcc-document-api.jar
COPY --from=build /app/jrcc-access-spring-boot-autoconfigure/target/jrcc-access-spring-boot-autoconfigure*.jar jrcc-access-spring-boot-autoconfigure.jar
COPY --from=build /app/jrcc-access-spring-boot-starter/target/jrcc-access-spring-boot-starter*.jar jrcc-access-spring-boot-starter.jar
COPY --from=build /app/jrcc-document-access-libs/target/jrcc-document-access-libs*.jar jrcc-document-access-libs.jar
COPY --from=build /app/jrcc-access-api/target/jrcc-document-api*.jar jrcc-document-api.jar