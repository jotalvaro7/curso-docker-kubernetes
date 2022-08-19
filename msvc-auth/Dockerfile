ARG MSVC_NAME=msvc-auth

#Generando una primera imagen
FROM openjdk:16-jdk-alpine as builder

#primer stage
ARG MSVC_NAME

WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

#para descargar las dependencias primero y guardarlas en cache para optimizar tiempo
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#copiar el proyecto a src
COPY ./$MSVC_NAME/src ./src
#generar el jar
RUN ./mvnw clean package -DskipTests

#Generando la segunda imagen a partir de la primera
FROM openjdk:16-jdk-alpine

ARG MSVC_NAME

WORKDIR /app
RUN mkdir ./logs
#Copiando desde la imagen anterior solamente el JAR en /app (Libera peso en la imagen)
ARG TARGET_FOLDER=/app/$MSVC_NAME/target
COPY --from=builder $TARGET_FOLDER/msvc-auth-0.0.1-SNAPSHOT.jar .
ARG PORT_APP=9000
ENV PORT $PORT_APP
EXPOSE $PORT

CMD ["java", "-jar", "msvc-auth-0.0.1-SNAPSHOT.jar"]