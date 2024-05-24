FROM openjdk:17

WORKDIR /app

COPY build/libs/vidconnect-0.0.1-SNAPSHOT.jar /app/vidconnect-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "vidconnect-0.0.1-SNAPSHOT.jar"]

