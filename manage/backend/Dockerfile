FROM openjdk:11-jre-slim AS builder

RUN mkdir -p /home/spring/logs
WORKDIR /home/spring
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:11-jre-slim

WORKDIR /home/spring
COPY --from=builder /home/spring/dependencies/ ./
COPY --from=builder /home/spring/spring-boot-loader/ ./
COPY --from=builder /home/spring/snapshot-dependencies/ ./
COPY --from=builder /home/spring/application/ ./

# EXPOSE 9090
ENTRYPOINT java $JAVA_OPTS -Dfile.encoding=UTF-8 org.springframework.boot.loader.JarLauncher
