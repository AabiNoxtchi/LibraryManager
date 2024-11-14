# Getting started

- this project is generated with [spring initializer](https://start.spring.io/) 
- choosing Java 17, Gradle - Groovy, and dependencies : Spring Web, Spring validation, Spring Data JPA, Lombok and PostgreSQL Driver

### Library manager ###
- this project aims to demonstrate model design : entity design and relations between them
- crud operations
- db calls optimizations

##### Many to Many Unidirectional #####
Book and Author has many-to-many relation, in this project there is no need to manage and traverse books from author side,
however books need to be saved with their authors, so unidirectional approach is selected.


##### Author #####
- Author model is small and simple for demonstration, it just has name field,
- and corresponding EditAuthor model to receive in Controller for Post and Put endpoints, 
- the pros to using edit models is further input validations, the cons may be further coding.
- the controller and service for Author are straight forward offering crud endpoints.
- note that an Author won't be deleted if there are books with that author


##### Book #####
- Book model has the relation to Author
- Set is chosen as the collection type,as it optimizes operations within the db when updating the authors collection
- when it is a List, Hibernate will delete all authors in book_authors table and rewrite them all every time they are updated
- when it's Set Hibernate will only update the changes in the Set
- the queries to the db can be shown by adding in application.properties file the following lines :
   ```
  spring.jpa.properties.hibernate.format_sql=true
  spring.jpa.show-sql=true
   ```

- fetch type for authors is eager `FetchType.EAGER`, since all the authors are needed weather in both Get endpoints or in Put endpoint.
- it could be left to the default Lazy, 
  - but updating authors Set would initiate a separate query to fetch them anyway,
  - if we set the book a new set Hibernate would delete all and rewrite them all
  - and in both Get endpoints the books are needed to be displayed with their authors

- we could use authorRepo::getReferenceById instead of authorRepo.findById, to get an author proxy without a call to db,
  - but in that case we can't return book with a list of updated authors if there are any added authors, 
  - because it is a proxy, and when spring tries to send it in a json response and tries to access its properties other than the id
   ,it throws an exception
  - so, this approach is preferable if we don't need the actual authors to return in the response

##### Validation #####

jakarta.validation.constraints  :  
Spring Boot Bean Validation is a powerful feature for validating the data in the system, the de-facto standard is Hibernate Validator,
the Bean Validation framework’s reference implementation, included in 'org.springframework.boot:spring-boot-starter-validation' dependency.
Common validation annotations from jakarta.validation.constraints include `@NotNull`, `@Size`, `@Min`, `@Max`...

For Bean Validation in Spring Boot, the model fields are annotated with these constraints and enable validation by annotating
request body method parameters with `@Valid` or (`@Validated` for group validations). For method validations the required annotation
are added before Path Variables and Request Parameters or for the entire method.

`MethodArgumentNotValidException` is thrown from pojos, `ConstraintViolationException` from method and entity validations, and 
`HandlerMethodValidationException` if there are multiple parameters to validate. If no custom exception handler is provided
`DefaultHandlerExceptionResolver` intercepts these exceptions and maps them to HTTP response codes (like 400 Bad Request). 
By implementing `@ControllerAdvice` error responses can be further controlled.

jakarta.persistence  :  
Mapping Properties and Fields with annotations from jakarta.persistence defines the schema of database tables, annotations like 
@Entity, Primary Keys with @Id and @GeneratedValue, Entity Association Annotations, @Table, @Column and more. The @Column annotation
is used to override default behaviors of the column to which a field or property will be mapped. Some of the details are schema related,
and therefore apply only if the schema is generated from the annotated files. Others apply and are enforced at run time by Hibernate.

The following attributes commonly being overridden:
name : the name of the column, defaults to the name of the property.
length : the size of the column used to map a value (particularly a String value). defaults to 255, which might otherwise result in truncated String data, for example.
nullable : defines column nullability. The default is that fields should be permitted to be null.
unique : defines column uniqueness. This defaults to false.

errors in database persistence like unique column violation cause DataIntegrityViolationException.

##### Testing #####

API Testing:  
API (Application Programming Interface) testing is the process of evaluating the functionality, performance, security, and reliability
of an application programming interface. In the context of Spring Boot, APIs are typically RESTful services that communicate over HTTP.
Spring’s MockMvc is used for Integration Testing to test the endpoints. tests verify the correct handling of HTTP requests and responses.

Tools for testing:  
JUnit and TestNG: are Standard testing frameworks for writing and executing tests.  
Mockito: A mocking framework for creating mock objects in unit tests.  
RestAssured: A popular Java library for testing RESTful APIs.  
Spring Test: Provides testing support for Spring components.  

some annotations for context configuration:  
`@SpringBootTest`: this annotation on the integration test classes loads the complete Spring application context.  
`@DataJpaTest`: ensures that only the components related to JPA (e.g., EntityManager, DataSource) are initialized, 
                providing a lightweight testing environment.  


dependencies needed for testing are added and loaded in build.gradle file:  
```
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	testRuntimeOnly 'com.h2database:h2'
```

`application-test.properties` file is added to provide further configuration for testing environment.
<br><br> 
  

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.5/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.5/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.3.5/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.3.5/reference/data/sql.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

