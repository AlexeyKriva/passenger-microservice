FROM maven:3.8.5-openjdk-17

WORKDIR /passenger

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean
RUN mvn package -DskipTests

FROM openjdk:17-jdk

COPY /target/passenger-microservice-0.0.1-SNAPSHOT.jar /passenger/launch-passenger.jar

ENTRYPOINT ["java","-jar","/passenger/launch-passenger.jar"]

EXPOSE 8081