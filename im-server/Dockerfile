FROM openjdk:17-jdk-slim

COPY im-server.jar app.jar

# 启动并指定docker环境，支持通过环境变量配置集群ID和端口
ENV WS_MQ=1
ENV WS_PORT=8000
ENTRYPOINT ["sh", "-c", "java -Dws.mq=$WS_MQ -Dws.port=$WS_PORT -jar app.jar --spring.profiles.active=docker"]
