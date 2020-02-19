FROM maven:3.5.2-jdk-8 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean install

FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/app/target/crawler_test*.jar /usr/app/crawler_app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/crawler_app.jar"]