# jrcc-document-access-libs

This library provides a service to store documents using [redis](https://redis.io/) cache.

## Road Map

* [X] Store a document in redis cache
* [ ] Retrieve document from redis cache
* [X] Publish a document ready message to rabbitMq
* [ ] Subscribe when a document is ready from rabbitMq

## jrcc document access spring boot starter

This provide a spring boot starter for the document access lib using [redis](https://redis.io/)

### Usage

Add `jrcc-access-spring-boot-starter` to your project

```xml
<dependency>
    <groupId>ca.gov.bc.open</groupId>
    <artifactId>jrcc-access-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```

Add settings into `application.settings` file

```properties

# common spring boot settings (redis)

spring.redis.database=
spring.redis.host=
spring.redis.port=
spring.redis.password=
spring.redis.ssl=
spring.redis.timeout=
spring.redis.cluster.nodes=
spring.redis.sentinel.master=
spring.redis.sentinel.nodes=

# common spring boot settings (amqp)

spring.rabbitmq.host=
spring.rabbitmq.port=
spring.rabbitmq.username=
spring.rabbitmq.password=

# bc gov settings

bcgov.access.ttl= <-- cache time to live expressed in hours (default = 1)

```

### Use the RabbitMqDocumentOutput

```java
@Component
public class MyComponent {
  

  // Autowired the documentoutput interface
  @Autowired
  private DocumentOutput documentOutput; 
  
  @Override
  public void doSomethig(String content) {
    
    // Creates a new transaction
    TransactionInfo transactionInfo = new TransactionInfo("testfile.txt", "jrcc-access-sample", LocalDateTime.now());
    
    try {
      // Send the content to redis and rabbit
      this.documentOutput.send(content, transactionInfo); 
    } catch(ServiceUnavailableException e) {
      // If one of the requested service is unavailable, handle it by catching the custom ServiceUnavailableException
    }
  }
}
```

## References

* [Spring Boot Autoconfiguration for Spring AMQP (RabbitMQ)](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-messaging.html#boot-features-amqp)
* [Spring Data Redis](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/)

## Sample App

The sample app is a demo that shows the usage of `jrcc-access-spring-boot-starter`

Run the application (using [docker](https://www.docker.com/))

Create a redis container

```bash
docker run --name some-redis -p 6379:6379 -d redis
```
Create a rabit container

```bash
docker run -d --hostname some-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```

Install jrcc-access-libs

```bash
cd jrcc-document-access-libs
mvn install
```

Install jrcc-access-spring-boot-autoconfigure

```bash
cd jrcc-access-spring-boot-autoconfigure
mvn install
```

Install jrcc-access-spring-boot-starter

```bash
cd jrcc-access-spring-boot-starter
mvn install
```

Run the sample app

```bash
cd jrcc-access-spring-boot-sample-app
mvn spring-boot:run
```

You should get a similar output

```console
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.6.RELEASE)

2019-07-04 10:39:25.297  INFO 13440 --- [           main] JrccAccessSpringBootSampleAppApplication : Starting JrccAccessSpringBootSampleAppApplication on CAVICSR7LNQKC2 with PID 13440
2019-07-04 10:39:25.300  INFO 13440 --- [           main] JrccAccessSpringBootSampleAppApplication : No active profile set, falling back to default profiles: default
2019-07-04 10:39:25.919  INFO 13440 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode!
2019-07-04 10:39:25.922  INFO 13440 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data repositories in DEFAULT mode.
2019-07-04 10:39:25.958  INFO 13440 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 17ms. Found 0 repository interfaces.
2019-07-04 10:39:27.257  INFO 13440 --- [           main] JrccAccessSpringBootSampleAppApplication : Started JrccAccessSpringBootSampleAppApplication in 2.347 seconds (JVM running for 4.044)
2019-07-04 10:39:27.259  INFO 13440 --- [           main] eAppApplication$ApplicationStartupRunner : Starting access sample app
2019-07-04 10:39:27.279  INFO 13440 --- [           main] c.g.b.o.j.a.s.RabbitMqDocumentOutput     : Attempting to publish [document type: test-doc].
2019-07-04 10:39:27.345  INFO 13440 --- [           main] c.g.b.o.j.a.s.RabbitMqDocumentOutput     : [document type: test-doc] successfully stored to redis key [c7ce3298-3bf9-435c-96e5-4d1f698ff9f0].
2019-07-04 10:39:27.440  INFO 13440 --- [           main] o.s.a.r.c.CachingConnectionFactory       : Attempting to connect to: localhost:5672
2019-07-04 10:39:27.509  INFO 13440 --- [           main] o.s.a.r.c.CachingConnectionFactory       : Created new connection: connectionFactory#4264b240:0/SimpleConnection@1bd81830 [delegate=amqp://guest@127.0.0.1:5672/, localPort= 51847]
2019-07-04 10:39:27.554  INFO 13440 --- [           main] c.g.b.o.j.a.s.RabbitMqDocumentOutput     : [document type: test-doc] successfully published to [document.ready] with [test-doc] routing key
2019-07-04 10:39:27.558  INFO 13440 --- [           main] eAppApplication$ApplicationStartupRunner : Successfully store and send message
```

To view the message in a queue, login to [rabbitmq management console](http://localhost:15672) with default guest/guest and create a binding to the `document.ready` exchange using `test-doc` routing key

![binding](docs/document.ready.bind.png)