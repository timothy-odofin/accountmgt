
@echo off
SET DEVELOPMENT_HOME=C:\Users\JIDE\Desktop\codingchallenge\springboot_dockers

cd %DEVELOPMENT_HOME%\account\

REM Integration testing
call mvn verify -Pfailsafe

REM Building artifacts
call mvn clean install -DskipTests

REM shutdown or stop any previous docker-compose that is running
call docker-compose down

REM Build the docker image using the Dockerfile 
call docker-compose build

REM Start  docker container in an interactive mode
call docker-compose up

REM press ctl+c to stop  docker container in an interactive mode
