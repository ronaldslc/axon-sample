# axon-sample
Just a learning project on Axon

## Getting Started
Project was created for vscode devcontainer environment and generated from https://start.spring.io/ then customised to use 
webflux, kotlin, axon with added unit and integration test separation

### Requirements
- vscode
- docker

### Run development mode
```shell
docker-compose -f docker/dev/docker-compose.yml up -d
mvn spring-boot:run
```

devtools will hot reload the application

Note: if you get an error, try `mvn clean` first.

### Running unit tests
```shell
mvn test
```

### Running integration tests
```shell
mvn integration-test
```
