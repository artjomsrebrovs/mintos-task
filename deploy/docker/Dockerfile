FROM openjdk:17-alpine
MAINTAINER Artjoms Revrovs
RUN mkdir app
WORKDIR app
COPY task-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]