FROM jdk:11-skeleton

# TODO change JAR_FILE first
ARG JAR_FILE=build/libs/skeleton-1.0.0.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
