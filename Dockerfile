FROM maven:3-eclipse-temurin-17-alpine

WORKDIR /opt/dronesREST
COPY pom.xml pom.xml
COPY src src
RUN mvn -B -U -ntp package

ENTRYPOINT ["java", "-jar", "target/dispatch-controller.jar"]
