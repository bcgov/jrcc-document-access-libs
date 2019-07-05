REM Clean and install all the maven modules

cd jrcc-document-access-libs\
call mvn clean install -U
cd ..
cd jrcc-access-api\
call mvn clean install -U
cd ..
cd jrcc-access-spring-boot-autoconfigure\
call mvn clean install -U
cd ..
cd jrcc-access-spring-boot-starter\
call mvn clean install -U
cd ..
cd jrcc-access-spring-boot-sample-app\
call mvn clean install -U
