FROM openjdk
MAINTAINER Erickson Silva

ADD ./target/discovery-service.jar/ /app/

CMD ["java", "-Xmx200m", "-jar", "/app/discovery-service.jar"]

EXPOSE 8761
