# Base image para Java 17
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR gerado pelo Maven para o container
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Define o comando para executar o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
