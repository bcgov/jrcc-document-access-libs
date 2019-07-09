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
    <version>0.3.0</version>
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
bcgov-access-ttl=[int] <-- the time to live for document in the temp storage 
bcgov-access-publish-document-type: <-- the type of document to publish
bcgov-access-input: [http] <-- the input plugin
bcgov-access-output: [console,rabbitmq] <-- the ouput plugin

```

## Configuration

### Input

You can configure the document input using `bcgov.access.input` property.

> bcgov.access.input=http

when set to `http` jrcc access exposes the [document API](jrcc-access-api/jrcc.swagger.yml).
You can configure the webserver using standard spring configuration.
Document sent to the api are handle with the default documentReadyHandler.

### Output

#### Console

You can configure the document output using `bcgov.access.output` property. the default configuration is `console`.

> bcgov.access.ouput.plugin=console

when set to `console` the transaction details and the payload are printed to standard output.


#### RabbitMq

> bcgov.access.ouput.plugin=rabbitmq

when set to `rabbitmq` a document ready message is send to rabbitmq and the document is stored to reddis cache. this configuration implies that you have a running instance of reddis and rabbitmq
You can configure reddis and rabbitmq using the standard spring boot configuration.

##### Configuration

| name | definition |
| --- | --- |
| bcgov.access.input.http.output.rabbitmq.ttl | the time to live for the document in storage expressed in hour |

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

Run the `make.bat` file

Ru the sample

```bash
mvn spring-boot:run -f jrcc-access-spring-boot-sample-app\pom.xml
```

This app is configure to receive document using the http plugin.

you can use this [Postman collection](jrcc-access-api/jrcc-document-api.postman_collection.json) to interact with the server.

For body, select binary and click select file
set the http header to `Content-Type:application/octet-stream`

![Postman config](docs\postman.body.png)


To view the message in a queue, login to [rabbitmq management console](http://localhost:15672) with default guest/guest and create a binding to the `document.ready` exchange using `test-doc` routing key

![binding](docs/document.ready.bind.png)