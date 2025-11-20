FROM eclipse-temurin:21-jre
COPY target/*.jar app.jar
ENTRYPOINT ["java","-XX:+UseContainerSupport","-jar","app.jar"]
