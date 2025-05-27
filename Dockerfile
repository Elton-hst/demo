# Etapa 1: build usando Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia o pom.xml e baixa dependências antes para aproveitar cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o restante do projeto e realiza o build
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagem final com JDK 21
FROM eclipse-temurin:21.0.2_13-jdk

WORKDIR /app

# Copia o JAR gerado do builder
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8081

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
