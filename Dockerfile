FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the bidworm directory into the container
COPY bidworm/ /app/

# Ensure mvnw is executable
RUN chmod +x /app/mvnw

# Run Maven build
RUN /app/mvnw clean package

# Expose the API port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/bidworm-0.0.1-SNAPSHOT.jar"]
