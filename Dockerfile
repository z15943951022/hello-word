FROM openjdk:8u222-jre
VOLUME /tmp
ADD target/*.jar app.jar
ENV JAVA_OPTS=""
ENV LANG C.UTF-8
ENV TZ=Asia/Shanghai
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar