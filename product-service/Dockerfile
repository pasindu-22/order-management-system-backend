
FROM eclipse-temurin:21-jdk-alpine

LABEL maintainer="Pasindu <your_email@example.com>"
LABEL version="1.0"
LABEL description="Product Service Microservice"

WORKDIR /app

COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]