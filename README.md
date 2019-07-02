# jrcc-document-access-libs

This library provides a service to store documents using [redis](https://redis.io/) cache.

## Road Map

* [] Retrieve document from storage

## jrcc document access spring boot starter

This provide a spring boot starter for the document access lib using [redis](https://redis.io/)

### Usage

Add `jrcc-access-spring-boot-starter` to your project

```xml
<dependency>
    <groupId>ca.gov.bc.open</groupId>
    <artifactId>jrcc-access-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Add settings into `application.settings` file

```properties

# common spring boot settings

spring.redis.database=
spring.redis.host=
spring.redis.port=
spring.redis.password=
spring.redis.ssl=
spring.redis.timeout=
spring.redis.cluster.nodes=
spring.redis.sentinel.master=
spring.redis.sentinel.nodes=

# bc gov settings

bcgov.access.ttl= <-- cache time to live expressed in hours (default = 1)

```


## Sample App

The sample app is a demo that shows the usage of `jrcc-access-spring-boot-starter`

Run the application (using [docker](https://www.docker.com/))

Create a redis container

```bash
docker run --name my-redis-container -p 6379:6379 -d redis
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

2019-07-02 09:27:36.154  INFO 7744 --- [           main] JrccAccessSpringBootSampleAppApplication : Starting JrccAccessSpringBootSampleAppApplication on CAVICSR7LNQKC2 with PID 7744 (C:\github\jrcc-document-access-libs\jrcc-access-spring-boot-sample-app\target\classes started by 177226 in C:\github\jrcc-document-access-libs\jrcc-access-spring-boot-sample-app)
2019-07-02 09:27:36.164  INFO 7744 --- [           main] JrccAccessSpringBootSampleAppApplication : No active profile set, falling back to default profiles: default
2019-07-02 09:27:36.813  INFO 7744 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode!
2019-07-02 09:27:36.817  INFO 7744 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data repositories in DEFAULT mode.
2019-07-02 09:27:36.865  INFO 7744 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 29ms. Found 0 repository interfaces.
2019-07-02 09:27:37.590  INFO 7744 --- [           main] JrccAccessSpringBootSampleAppApplication : Started JrccAccessSpringBootSampleAppApplication in 2.219 seconds (JVM running for 14.481)
2019-07-02 09:27:37.592  INFO 7744 --- [           main] eAppApplication$ApplicationStartupRunner : Starting access sample app
2019-07-02 09:27:37.669  INFO 7744 --- [           main] eAppApplication$ApplicationStartupRunner : content successfully stored in redis
2019-07-02 09:27:37.670  INFO 7744 --- [           main] eAppApplication$ApplicationStartupRunner : key: 1057ea9e-9a3e-4741-ba2d-fd13e2c6bf9d
2019-07-02 09:27:37.671  INFO 7744 --- [           main] eAppApplication$ApplicationStartupRunner : MD5: 9CE2CBC8011718E747A86947FAB93F75
```