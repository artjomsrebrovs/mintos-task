# MINTOS TASK
## Task details
### Functional requirements
Implement a RESTful web service that would handle GET requests to path “weather” by returning the
weather data determined by IP of the request originator.
Upon receiving a request, the service should perform a geolocation search using a non-commercial, 3rd party
IP to location provider.

Having performed the reverse geo search service should use another non-commercial, 3rd party service to
determine current weather conditions using the coordinates of the IP
> The application is Spring Boot based micro service, runs on port 8080 and exposes 3 endpoints:
> ```jvm
> - GET /weather
> main application endpoint to get the weather conditions data at the requesters ip adress
> relies on either a X-FORWARDED-FOR header or ip is taken from clinet request
> ```
> ```jvm
> - GET /weather/{ipAddress}
> with ip adress path variable
> ```
> ```jvm
> - GET /weather_records
> additional application endpoint for debugging purposes
> ```
> Two 3rd party services are used to get geographical location of the requester ip address 
> and then get the current weather conditions at the give coordinates.
### Non-functional requirements
- Test coverage should be not less than 80%
> The application is covered by 18 unit tests with the total coverage of 88% of all the lines (according to IntelliJ test coverage tool).
> Apart from that test reporting plugins are configured: jacoco and surefire
>
> To execute the report generation run the following commands: 
> ```jvm
> mvn test 
> ```
> and 
> ```jvm
> mvn surefire-report:report  
> ```
- Implemented web service should be resilient to 3rd party service unavailability
> The service client implementation allow 3rd party service outages or HTTP related errors.
> The application log all exceptions and errors ans well as keeps all failed attempts to get weather data in database.
- Data from 3rd party providers should be stored in a database
> All the data retrieved from geo location and weather services, successful and failed requests are stored in the H2 file database. 
> Database schema is initialized upon application first startup.
> H2 database exposes a web console accessible here:
> ```jvm
> http://localhost:8080/h2/
> ```
> Or on Azure Container Instance:
> ```jvm
> weatherdata.ayeybtc2hqajc6dp.swedencentral.azurecontainer.io:8080/h2
> ```
> With a following credentials:
> ```jvm
> jdbc url: jdbc:h2:./weather-data-h2-db
> username: admin
> password: pass
> ```
- An in-memory cache should be used as the first layer in data retrieval
> Caffeine cache is integrated with Spring Boot to provide a caching layer. 
- DB schema should allow a historical analysis of both queries from a specific IP and of weather conditions for specific coordinates
> The request and response data is stored in 1st normal form allowing quick and easy historical analysis.
- DB schema versioning should be implemented
> Database schema versioning is done with Flyway.
- Application containerization
> Dockerfile with openjdk 17 is located in deploy/docker directory, built image pushed into Docker Hub repository: 
> ```jvm
> docker pull artyom366/weather_app:v5
> ```
- Deployment
> The application is deployed on Azure Container Instance and accessible here: 
> ```jvm
> weatherdata.ayeybtc2hqajc6dp.swedencentral.azurecontainer.io:8080/weather/
> ```
- JAR executable 
> Jar application file is located in deploy/docker directory
