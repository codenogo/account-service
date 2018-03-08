# account-service



account-service is part of a banking solution service that enables account crediting, debiting and check-balance.


### Stack

account-service implements the following tech:

* [SpringBoot] 
* [SpringData] - Persistence
* [QueryDSL] - Dynamic Querying
* [Rest-Assured] - Test
* [MYSQL] - Database
* [Swagger] - API Doc


### Installation

account-service requires Java 8+ and maven to run.

```sh
$ cd account-service
$ mvn spring-boot:run
```

To generate QueryDSL data when using IntelliJIdea

```sh
* Open app with intellij
* Right click on POM file
* Select Maven >> Generate Sources and Update sources
```

#### API DOC

http://localhost:7000/swagger-ui.html


### Test

 ```sh
$ mvn test
```

License
----

MIT




[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [SpringBoot]: <https://projects.spring.io/spring-boot/>
   [SpringData]: <http://projects.spring.io/spring-data/>
   [QueryDSL]: <http://www.querydsl.com/>
   [Rest-Assured]: <http://rest-assured.io/>
   [MYSQL]: <https://www.mysql.com/>
   [Swagger]: <https://swagger.io/>
   