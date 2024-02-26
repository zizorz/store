# Store Application

This is a repo for trying out distributed systems technologies. We are building a simple store application using a microservices architecture.
We are playing with gRPC, Kafka, circuit breakers, API composition and Kubernetes. There is also a small React UI. 

## Backend

There are four backend services. 
* **api** -> A Rest API that uses gRPC to fetch data from the other services. Accessible on Port 8080.
* **creditcard-service** -> A service that validates credit cards.
* **shopping-service** -> A service for listing and buying products.
* **analytics-service** -> A service for analyzing shopping data.


The backend services are written in Java and uses the Spring Boot framework.

The contracts module contains the gRPC contracts.


## Frontend

The frontend is written in JavaScript using the React library. It uses Material-UI for the user interface components.

## Technologies Used

- Java
- Spring Boot
- gRPC
- Kafka
- JavaScript
- React
- Material-UI

## Setup

To run this project, you need to have Java, npm and docker installed. Clone the repository, navigate to the project directory, and follow the instructions below:

1. Running Kafka:
```bash
cd docker/kafka
docker-compose up
```


2. Run backend:  
For each backend project:
```bash
cd {project}
./gradlew bootRun
```

3. Install the frontend dependencies and start the development server:
```bash
cd react-ui
npm install
npm start
```

The application should now be running at `http://localhost:3000`.


## Running in k8s

1. Build the modules:
```bash
./gradlew build
```

2. Build the docker images:
```bash
./docker-build.sh
```
3. Apply k8s configuration:
```bash
kubectl apply -f k8s
```