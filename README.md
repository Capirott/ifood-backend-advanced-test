# Track Suggestion Application

## Functional Service

This application has only one functional service which is *track-suggestion-service*.
This service compose of requisitions to both Spotify and OpenWeatherMap API for suggesting tracks based on the Business Rules defined in the test description (at the end). 
It was implemented using Spring MVC and consist of two end points, as described in the table bellow.

Method | PATH | Description
 ------- | ------------ | ------- |
 GET | _/suggestions/tracks/city/{**name**}_ | Get recommended tracks based on the city temperature
 GET | _/suggestions/tracks/coordinates/{**latitude**}/{**longitude**}_ | Get recommended tracks based on the coordinates temperature
 
    For more information about the endpoints refer to the swagger documentation
Some of the technologies used:
- Hystrix for implementing a fault-tolerant system.
- Spring Boot Cache Starter and Caffeine

# Architecture Sketch

#### config-service
Spring Cloud Config for scalable and centralized configuration for distributed system.
> For simplicity the shared configs are saved locally intead of a remote server.

#### gateway-service
Spring Cloud Starter Gateway used to handle requests (and load balance) by routing them to the appropriate backend services. 
> That means all requests starting with /suggestions/tracks will be routed to track-suggestion-service. 

### discovery-service
Spring Cloud Starter Netflix Eureka used to allow automatic detection of services instances.

## monitoring-service (optional)
This service displays the Hystrix Dashboard. Service created for analyzing the Circuit Breaker metrics of *track-suggestion-service*.
>The *track-suggestion-service* has the endpoint to *hystrix.stream*  available.
> The path is _/suggestions/tracks/actuator/hystrix.stream_.
 
#### Important end-points:
- http://localhost:4000/ - Gateway (port 80 if running in docker)
- http://localhost:8761/ - Eureka Dashboard
- http://localhost:8989/suggestions/tracks/swagger-ui.html -  Swagger Document API for the functional service.
- http://localhost:9000/hystrix - Hystrix Dashboard

#### Running the application
> Requirements: OpenJDK 11 and Apache Maven

You can run it using the `docker-compose.yml` file or an IDE.

If you are running on an IDE, it's required to set the ENVIRONMENT VARIABLES defined in the `.env` file. 
Otherwise, before running in docker you need to build the application using `mvn install` and set the adequate values in same file.

If the API's crendentials is not defined the application will run in fail-safe mode, which means returning static objects from the fallbacks methods. The fallback objets are also configurable in `track-suggestion-service.yml` file. 

On a final note, all applications require that config-server is running and sorry for my bad english.

---
# iFood Backend Advanced Test

Create a micro-service able to accept RESTful requests receiving as parameter either city name or lat long coordinates and returns a playlist (only track names is fine) suggestion according to the current temperature.

## Business rules

* If temperature (celcius) is above 30 degrees, suggest tracks for party
* In case temperature is between 15 and 30 degrees, suggest pop music tracks
* If it's a bit chilly (between 10 and 14 degrees), suggest rock music tracks
* Otherwise, if it's freezing outside, suggests classical music tracks 

## Hints

You can make usage of OpenWeatherMaps API (https://openweathermap.org) to fetch temperature data and Spotify (https://developer.spotify.com) to suggest the tracks as part of the playlist.

## Non functional requirements

As this service will be a worldwide success, it must be prepared to be fault tolerant, responsive and resilient.

Use whatever language, tools and frameworks you feel comfortable to, and briefly elaborate on your solution, architecture details, choice of patterns and frameworks.

Also, make it easy to deploy/run your service(s) locally (consider using some container/vm solution for this). Once done, share your code with us.
