## Workflow platform

### Prerequisites
* postgresql
* jdk 11 , go to [https://adoptopenjdk.net/](https://adoptopenjdk.net/)
* maven

### How to build

```
mvn clean install package
```

### Build image

```
./deploy/build.sh
or
./deploy/build.sh image_name
```

### Run with container
```
docker run -it --rm -p 8080:8080 \
    -e spring.datasource.url=jdbc:postgresql://your_postgresql_host:5432/automation \
    -e spring.datasource.username=postgres \
    -e spring.datasource.password=test \
    automation
```