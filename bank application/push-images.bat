@echo off
docker push docker.io/strazhkoe/accounts:latest
docker push docker.io/strazhkoe/cards:latest
docker push docker.io/strazhkoe/loans:latest
docker push docker.io/strazhkoe/configserver:latest
docker push docker.io/strazhkoe/eurekaserver:latest
docker push docker.io/strazhkoe/gatewayserver:latest