FROM openjdk:17-jdk-alpine
MAINTAINER flavien-dws.web.app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]