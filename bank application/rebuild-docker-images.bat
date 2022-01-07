@echo off
cd accounts
call mvn package
docker build . -t strazhkoe/accounts
cd ../cards
call mvn spring-boot:build-image
cd ../loans
call mvn spring-boot:build-image
cd ../eurekaserver
call mvn spring-boot:build-image
cd ../configserver
call mvn spring-boot:build-image
cd ../gatewayserver
call mvn spring-boot:build-image
cd ..