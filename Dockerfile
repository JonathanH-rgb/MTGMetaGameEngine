# ---------------------------------------------------------------------------
# Stage 1: build
# ---------------------------------------------------------------------------
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /build

# Copy ONLY the files needed to resolve dependencies first. This layer is cached
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B clean package -DskipTests

# Stage 2: runtime
FROM eclipse-temurin:25-jre AS runtime

WORKDIR /app

# Run as an unprivileged user: if the app is compromised, the attacker is not
# root inside the container.
RUN useradd --system --uid 10001 appuser

COPY --from=builder /build/target/*.jar app.jar

USER appuser
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
