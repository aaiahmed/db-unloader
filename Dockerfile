FROM maven:3.6.3-openjdk-11 as builder
COPY ./ ./
RUN mvn clean package

FROM openjdk:11
COPY --from=builder target/db-unloader-1.0-SNAPSHOT-jar-with-dependencies.jar /app.jar
RUN mkdir /output
CMD ["java", "-jar", "/app.jar"]
