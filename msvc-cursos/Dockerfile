ARG MSVC_NAME=msvc-cursos
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
ARG TARGET_FOLDER=/app/msvc-cursos/target
COPY --from=builder $TARGET_FOLDER/msvc-cursos-0.0.1-SNAPSHOT.jar .
ARG PORT_APP=8002
ENV PORT $PORT_APP
EXPOSE $PORT

#ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "msvc-cursos-0.0.1-SNAPSHOT.jar"]