FROM openjdk:17-jdk-slim

COPY im-server.jar app.jar

# 启动并指定docker环境
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]