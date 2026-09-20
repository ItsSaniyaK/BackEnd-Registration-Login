FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

EXPOSE 10000

CMD ["sh", "-c", "java -jar target/user-auth-backend-0.0.1-SNAPSHOT.jar"]