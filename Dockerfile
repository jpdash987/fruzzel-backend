# Use an official Maven image to build the app
FROM maven:3.9.7-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
# Download dependencies first (cache layer)
RUN mvn dependency:go-offline -B
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK image to run the app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/sales-tracker-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
