## Spring Batch - retrieve data from the CSV file

#### Goal

Create a microservice which serves the contents of player.csv through REST API.

The service should expose two REST endpoints:
* GET /api/players - returns the list of all players.
* GET /api/players/{playerID} - returns a single player by ID.

## Requirements

* [JDK 17.0.4.1](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or later
* [Maven 3.6.3](http://maven.apache.org/download.cgi) or later

## Build with Maven

* [Welcome to Apache Maven](https://maven.apache.org/)
* [Building Java Projects with Maven](https://spring.io/guides/gs/maven/)

## Build and run tests with Maven

* cd into project-root-folder using the terminal.

* Run this maven command:
 
``` 
mvn clean test
``` 

## Run the service

* cd into project-root-folder using your terminal.

* Using Maven you can run the application using **mvn spring-boot:run**. 

```
mvn spring-boot:run
```

* Or you can build an executable JAR file with **mvn clean package** and run the JAR by typing:

```
  java -jar target/player-service-1.0.0.jar
```

## Test the service

* Run item-service, and visit the following urls using Postman or other API client:

Load:
```
GET localhost:8080/api/players/load
```

Get Player:
```
GET localhost:8080/api/players/aaronto01
```

Get None Exist Player:
```
GET localhost:8080/api/players/fakeId
```

Get All Players:
```
GET localhost:8080/api/players
```

Get All Players Pageable:
```
GET localhost:8080/api/players/listPageable?page=0&size=3
```

## H2 Console

http://localhost:8080/api/h2-console/
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:file:./data/playerdb
User Name: sa
Password:
```
## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.3/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#using.devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#web)
* [Spring Batch](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#howto.batch)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

## Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
