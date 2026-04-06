FROM openjdk:17-jdk

COPY im-springboot.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]