FROM openjdk:11-jre

WORKDIR /app

COPY build/libs/orlik-backend-*.jar /app/application.jar

CMD ["java", "-jar", "/app/application.jar"]