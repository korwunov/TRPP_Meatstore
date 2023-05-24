FROM openjdk:17
ARG JAR_FILE=OnlineShop-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]