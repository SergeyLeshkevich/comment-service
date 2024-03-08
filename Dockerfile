FROM openjdk:17-alpine

ADD /build/libs/comment-service-0.0.1-SNAPSHOT.jar /app/

CMD ["java", "-Xmx200m", "-jar", "/app/comment-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8085
