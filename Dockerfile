FROM openjdk:21-ea-1-jdk

WORKDIR /app

COPY target/agendamento-0.0.1-SNAPSHOT.jar  /app/agendamento.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/agendamento.jar"]