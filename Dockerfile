FROM openjdk:21-jdk

COPY target/grandmafood-1.0.0.jar .

EXPOSE 8080

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/grandmafood-1.0.0.jar"]