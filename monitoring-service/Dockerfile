FROM openjdk
MAINTAINER Erickson Silva

ADD ./target/monitoring-service.jar/ /app/

CMD ["java", "-Xmx200m", "-jar", "/app/monitoring-service.jar"]

EXPOSE 8080
