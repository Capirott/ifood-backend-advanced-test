FROM openjdk
MAINTAINER Erickson Silva

ADD ./target/track-suggestion-service.jar/ /app/

CMD ["java", "-Xmx200m", "-jar", "/app/track-suggestion-service.jar"]

EXPOSE 8989
