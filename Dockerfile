FROM openjdk:20-jdk-slim-buster
ARG JAR_FILE="build/libs/demo-0.0.1.jar"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]