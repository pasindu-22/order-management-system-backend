bash
```
mvn clean package
```
* This will create the jar file in the target directory.
* But we need the database and other dependencies to run the application.
* We can use Docker compose to run the application with all the dependencies.
* We will create a docker-compose file to run the application with all the dependencies.
* Currently we use only the mysql database.
* After creating the docker-compose file we will run the application using docker-compose.
bash
```
docker-compose up
```
* This will create the docker image and run the application with all the dependencies.

# About docker-compose.yml
* The docker-compose file is used to run the application with all the dependencies.
* A service is an abstract definition of a computing resource within an application which can be scaled or replaced independently from other components.
