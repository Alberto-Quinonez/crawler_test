FROM openjdk:8-jdk-alpine
COPY target/crawler_test-1.0-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch demo-docker-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","crawler_test-1.0-SNAPSHOT.jar"]