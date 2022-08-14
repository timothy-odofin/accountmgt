#!/usr/bin/env bash
echo "Running integration and unit testing"
mvn verify -Pfailsafe

echo "Building artifact"
mvn clean install

echo "docker-compose operations"
 docker-compose down
 docker-compose build
 docker-compose up

#sudo docker-compose down
#sudo docker-compose build
#sudo docker-compose up
