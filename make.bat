REM Clean and install all the maven modules


call mvn clean install -U -f jrcc-document-access-libs\pom.xml
call mvn clean install -U -f jrcc-access-api\pom.xml
call mvn clean install -U -f jrcc-access-spring-boot-autoconfigure\pom.xml
call mvn clean install -U -f jrcc-access-spring-boot-starter\pom.xml
call mvn clean install -U -f jrcc-access-spring-boot-sample-app\pom.xml

REM Modules successfully installed
REM To run the sample app use [mvn spring-boot:run -f jrcc-access-spring-boot-sample-app\pom.xml]
