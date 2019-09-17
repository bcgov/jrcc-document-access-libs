# jrcc-document-access-libs

This library provides a service exchange documents between micro services.

## jrcc document access spring boot starter

This provides a spring boot starter for the document access lib using [redis](https://redis.io/)

### Usage

#### Step 1
Add `jrcc-access-spring-boot-starter` to your project (See jrcc-access-spring-boot-sample-app pom.xml as an example)

```xml
<dependency>
    <groupId>ca.bc.gov.open</groupId>
    <artifactId>jrcc-access-spring-boot-starter</artifactId>
    <version>0.8.0</version>
</dependency>
```

#### Step 2
Add settings into the `application.settings` or `application.yml` file.
 ##### Use following settings to config logging level and logging message.
 ```properties
 logging.level.root = INFO  
 logging.level.ca.gov.bc = DEBUG
 logging.pattern.console = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} %X{transaction.filename} %X{transaction.id}- %msg%n"
 ```
logging.pattern.console property only works if we use Logback logging implementation (the default). The pattern which is needed to be specified also follows the [Logback layout rules](https://logback.qos.ch/manual/layouts.html#ClassicPatternLayout)

%X{transaction.filename} is the java logging MDC key for transaction filename supported in jrcc-document-access-libs.
%X{transaction.id} is java logging MDC key for transaction id supported in jrcc-document-access-libs.

 
 ##### Change settings for input/output plugins - using the following configuration guide for Plugin.


## Plugins

<a name="CommonOptions"></a>
### Common Options

| name | definition | required |
| --- | --- | --- |
| [bcgov.access.input.document-type](#bcgovaccessinputdocument-type) | String | No |
| [bcgov.access.input.plugin](#bcgovaccessinputplugin) | String | Yes |
| [bcgov.access.input.sender](#bcgovaccessinputsender) | String | No |

#### bcgov.access.input.document-type

* Value type is String
* Default value is `unknown`

Sets the document type to be manipulated

#### bcgov.access.input.plugin

* Value type is String

Sets the plugin type

#### bcgov.access.input.sender

* Value type is String

Sets the sender of the request

## Input Plugins

You can configure the document input using `bcgov.access.input.plugin` property.

* [Console](#console-input-plugin)
* [Http](#HttpInputPlugin)
* [RabbitMq](#RabbitMqInputPlugin)
* [Sftp](#sftpInputPlugin)

### Console Input Plugin

#### Description

Reads document form the standard input.
Each document is assumed to be one line

#### Configuration

```properties
bcgov.access.input.plugin=console
```

#### Input Configuration Options

There are no special configuration options for this plugin, but it does support the [Common Options](#CommonOptions).

<a name="HttpInputPlugin"></a>
### Http Input Plugin

#### Description

Using this input you can receive a single document over http(s).
For more details have a look at the [document API](jrcc-access-api/jrcc.swagger.yml).

#### Setup

```properties
bcgov.access.input.plugin=http
```
Make sure "spring.main.web-application-type: none"(which will block all http operation) is not present the `application.settings` or `application.yml` file.

You can configure the web server using standard spring configuration.
Document sent to the api are handle with the default documentReadyHandler.

#### Configuration Options

There are no special configuration options for this plugin, but it does support the [Common Options](#CommonOptions) and spring standard EMBEDDED SERVER CONFIGURATION (ServerProperties).
[Common Application Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)

Example to run the service on port `5050`

```properties
server.port=5050
```

<a name="RabbitMqInputPlugin"></a>
### RabbitMq Input Plugin

#### Description

Using this plugin you can receive JSON format messages from a specified rabbitMq queue(in our program, it is test-doc.0s.x0.q).
To publish, visit the RabbitMQ Management console (accessible on port 15672), navigate to the `Queues` tab and scroll down to the `Publish message` section.

The message Payload should be like following:
```properties
{
    "transactionInfo":{
         "fileName":"filename.txt",
         "sender":"unknown",
         "receivedOn":"2019-08-21T22:20:45.173"        
    },        
    "documentInfo":{
        "type":"crown-counsel-report"        
    },        
    "documentStorageProperties":{
        "key":"455591e2-6753-4a7f-9438-bedd52327b52",
        "digest":"311743F3D8EC271CA2BB23936C7392F5"        
    }   
} 
```

The Properties of the published message should be : content_type = application/json.
##### Note
Make sure this is a property and not a header

The lib will try to get the content from Redis Storage with key and md5 specified in above key and md5.

#### Setup

```properties
bcgov.access.input.plugin=rabbitmq
```

#### Configuration Options

It supports the [Common Options](#CommonOptions) and the following options:

| name | type | required |
| --- | --- | --- |
| [bcgov.access.input.rabbitmq.retryDelay](#bcgovaccessinputrabbitmqretryDelay) | Int | No |
| [bcgov.access.input.rabbitmq.retryCount](#bcgovaccessinputrabbitmqretryCount) | Int | No |

##### bcgov.access.input.rabbitmq.retryDelay

* Value type is Int
* Default value is `0`

Sets the delay in seconds between retries when the service is failing to process the message and throwing application known errors.

##### bcgov.access.input.rabbitmq.retryCount

* Value type is Int
* Default value is `1`

Sets the maximum attempt to reprocess a message in the queue.

<a name="sftpInputPlugin"></a>
### Sftp Input Plugin

#### Description

Using this plugin you can receive messages from a specified Sftp server.

#### Setup

```properties
bcgov.access.input.plugin=sftp
```

#### Configuration Options

It support the [Common Options](#CommonOptions) and the following options:

| name | type | required |
| --- | --- | --- |
| [bcgov.access.input.sftp.host](#bcgovaccessinputsftphost) | String | No |
| [bcgov.access.input.sftp.port](#bcgovaccessinputsftpport) | Int | No |
| [bcgov.access.input.sftp.username](#bcgovaccessinputsftpport) | String | Yes |
| [bcgov.access.input.sftp.password](#bcgovaccessinputsftpport) | String | Yes |
| [bcgov.access.input.sftp.remote-directory](#bcgovaccessinputsftpremote-directory) | String | Yes |
| [bcgov.access.input.sftp.filter-pattern](#bcgovaccessinputsftpfilter-pattern) | String | No |
| [bcgov.access.input.sftp.cron](#bcgovaccessinputsftpcron) | String | Yes |
| [bcgov.access.input.sftp.max-message-per-poll](#bcgovaccessinputsftpmax-message-per-poll) | String | No |
| [bcgov.access.input.sftp.ssh-private-key](#bcgovaccessinputsftpssh-private-key) | Resource | No |
| [bcgov.access.input.sftp.ssh-private-passphrase](#bcgovaccessinputsftpssh-private-passphrase) | String | No |
| [bcgov.access.input.sftp.allow-unknown-keys](#bcgovaccessinputsftpallow-unknown-keys) | boolean | No |
| [bcgov.access.input.sftp.known-host-file](#bcgovaccessinputsftpknown-host-file) | String | Yes (if allow-unknown-key is false) |


##### bcgov.access.input.sftp.host

* Value type is String
* Default value is `localhost`

Sets the sftp server host

##### bcgov.access.input.sftp.port

* Value type is Int
* Default value is `22`

Sets the sftp server port

##### bcgov.access.input.sftp.username

* Value type is String

Sets the sftp server username

##### bcgov.access.input.sftp.password

* Value type is String

Sets the sftp server password

##### bcgov.access.input.sftp.remote-directory

* Value type is String

Sets the sftp server remote directory.

##### bcgov.access.input.sftp.filter-pattern

* Value type is String
* Default value is `""`

Sets a regular expression to filter the list.

##### bcgov.access.input.sftp.cron

* Value type is String

Sets a cron tab expression with 6 fields.

##### bcgov.access.input.sftp.max-message-per-poll

* Value type is String
* Required

Sets the maximum message per poll.

##### bcgov.access.input.sftp.ssh-private-key

* Value type is String

Sets the location of the private key.

#####  bcgov.access.input.sftp.ssh-private-passphrase

* Value type is String

Sets the passphrase for the private key.

##### bcgov.access.input.sftp.allow-unknown-keys

* Value type is Boolean
* Default value is false

When no UserInfo has been provided, set to true to unconditionally allow connecting to an unknown host or when a host's key has changed (see knownHosts)

##### bcgov.access.input.sftp.known-host-file

* Value type is String

Specifies the filename that will be used for a host key repository. The file has the same format as OpenSSH's known_hosts file.
If allow-unknown-key is false, this property must be set correctly, or KnownHostFileNotDefinedException or KnownHostFileNotFoundException will be thrown.
If allow-unknown-key is true, this property will be ignored.

## Output Plugins

You can configure the document input using the `bcgov.access.output` property.

* [Console](#ConsoleOutputPlugin)
* [RabbitMq](#RabbitMqOutputPlugin)

<a name="ConsoleOutputPlugin"></a>
### Console Output Plugin

#### Description

A simple output which prints document information to STDOUT.
The console output is mostly used when testing the application configuration.

#### Setup

```properties
bcgov.access.output.plugin=console
```

#### Configuration Options

It supports the [Common Options](#Common Options) and the following options:

| name | type | required |
| --- | --- | --- |
| [bcgov.access.output.console.format](#bcgovaccessoutputconsoleformat) | String | No |

##### bcgov.access.output.console.format

* Value type is String
* Default value is `default`
* Value can be any of `default`, `xml`

When set to `default` the output is truncated to 100 chars.
When set to `xml` the plugins tries to prettify the xml document or return the content of the document

````properties
bcgov.access.output.console.format=xml
````

<a name="RabbitMqOutputPlugin"></a>
### RabbitMq Output Plugin

#### Description

Push documents to a RabbitMq exchange and store document to Redis Cache.

#### Setup

```properties
bcgov.access.output.plugin=rabbitmq
```

#### Configuration Options

It supports the [Common Options](#CommonOptions) and the following options:

| name | type | required |
| --- | --- | --- |
| [bcgov.access.output.rabbitmq.ttl](#bcgovaccessoutputrabbitmqttl) | Int | No |

##### bcgov.access.output.rabbitmq.ttl

* Value type is Int
* Default value is `1`

Sets the time to live for the document in the temporary storage (expressed in hours)

```properties
bcgov.access.output.rabbitmq.ttl
```

## Processor

You can register a processor to transform the content of the message.

To register a processor do the following:

Create a new spring component that implements [DocumentProcessor](jrcc-document-access-libs/src/main/java/ca/bc/gov/open/jrccaccess/libs/processing/DocumentProcessor.java)

```java
@Component
public class UpperCaseProcessor implements DocumentProcessor {

	@Override
	public String processDocument(String content, TransactionInfo transactionInfo) {
		return content.toUpperCase(Locale.CANADA);
	}
}
```

When registered, the processor will act on the input document content. For example, in the case shown above all input content will be converted to upper case.

## References

* [Spring Boot Autoconfiguration for Spring AMQP (RabbitMQ)](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-messaging.html#boot-features-amqp)
* [Spring Data Redis](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/)

## Sample App

The sample app is a demo that shows the usage of `jrcc-access-spring-boot-starter`

### Prerequisites

Running this application requires Apache Maven which in-turn has a dependency on Java. As a result, the following will need to be installed:

* mvn [Apache Maven](https://maven.apache.org/)
* Java JDK [Java SE Development Kit 8u221, Java 1.8](https://www.oracle.com/java/technologies/jdk8-downloads.html)

#### Notes
* Other versions of Java may not work with this project due to project restrictions and requirements.
* When installing Java and Maven, make sure that the path environment variable is set to `<path_to_jdk>/bin`.
* Add an env variable called `JAVA_HOME` and set it to `<path_to_jdk>`.
* Edit the `Path` system variable and add 3 new paths: 1) `%MAVEN_HOME%\bin`, 2) `%JAVA_HOME%\bin`, and 3) `<path_to_jdk>`.

### Installation Steps

Install jrcc-access-libs

Run the following command: `mvn clean install`

Run the sample

```bash
mvn clean install -P sample-app
mvn spring-boot:run -f jrcc-access-spring-boot-sample-app/pom.xml
```

This app is configured to receive document using the http plugin like following in application.yml

```properties
logging:
  level:
    ca:
      gov:
        bc: DEBUG
bcgov:
  access:
    input:
      sender: bcgov
      document-type: test-doc
      plugin: http
    output:
      document-type: test-doc
      plugin: console
```

you can use this [Postman collection](jrcc-access-api/jrcc-document-api.postman_collection.json) to interact with the server.

For body, select form-data and input key value as "file" and select file.
set the http header to `Content-Type: multipart/form-data`.

![Postman config](docs/postman.body.png)


#### If you want to run the sample app using redis and rabbitmq do the following

Create a redis container

```bash
docker run --name some-redis -p 6379:6379 -d redis
```
Create a rabbit container

```bash
docker run -d --hostname some-rabbit --name some-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
```

update the [application.yml](jrcc-access-spring-boot-sample-app/src/main/resources/application.yml)

```properties
bcgov:
  access:
    input: http
      sender: bcgov
    output:
      document-type: test-doc
      plugin: rabbitmq
      rabbitmq:
         ttl: 1
logging:
  level:
    ca:
      gov:
        bc: DEBUG
```

To view the message in a queue, login to [rabbitmq management console](http://localhost:15672) with default guest/guest and create a binding to the `document.ready` exchange using `test-doc` routing key

![binding](docs/document.ready.bind.png)

#### If you want to run the sample app using sftp do the following:

step 1. Create a sftp server container (from WindowsPowerShell or GitBash)
```bash
docker run -p 22:22 -d atmoz/sftp myname:pass:::upload
```

step 2. User "myname" with password "pass" can login with sftp and upload files to a folder called "upload". We are forwarding the container's port 22 to the host's port 22.

step 3. Use a Sftp Client application ( such as Filezilla, WinSCP, coreFTP) to connect to the server.(use sftp protocal and ip: localhost, port:22)

step 4. If you do not want to unconditionally allow connecting to an unknown host or when a host's key has changed, you need to provide a known_hosts file.
Use the following command to generate a known_hosts file for started sftp server (from WindowsPowerShell or GitBash).
```bash
ssh-keyscan -v -p 22 localhost>>known_hosts
```
step 5. Update the [application.yml](jrcc-access-spring-boot-sample-app/src/main/resources/application.yml)
```properties
main:
      web-application-type: none
logging:
  level:
    ca:
      gov:
        bc: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} %X{transaction.filename} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} %X{transaction.filename} - %msg%n"
bcgov:
  access:
    input:
      sender: bcgov
      document-type: test-doc
      plugin: sftp
      sftp:
        host: localhost
        port: 22
        username: myname
        password: pass
        remote-directory: /upload
        max-message-per-poll: 5
        cron: 0/5 * * * * *
        allow-unknown-keys: false
        known-host-file: C:\Users\user\.ssh\known_hosts
    output:
      document-type: test-doc
      plugin: console
```
Then start the sample application and use Sftp client to drag a file from your local file system to remote upload folder. The sample application should process the file and output it.

## Release

To create a new release run on develop branch

```
mvn versions:set -DartifactId=*  -DgroupId=*
```

it will prompt you for the new version

do a pull request against dev
