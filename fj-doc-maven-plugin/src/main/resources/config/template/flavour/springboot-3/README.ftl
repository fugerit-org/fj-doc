# ${context.artifactId}

## Quickstart

Requirement :

* maven 3.9.x
* java ${context.javaRelease}+

1. Start the app

```shell
mvn spring-boot:run
```

2. Try the app

Open the [openapi-explorer](http://localhost:8080/swagger-ui/index.html)

Test available routes (for instance : [/doc/example.md](http://localhost:8080/doc/example.md))

## Projects notes

the project used as template has been created with the
[springboot initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.3.3&packaging=jar&jvmVersion=21&groupId=org.fugerit.java.template&artifactId=fugerit-springboot3-template&name=fugerit-springboot3-template&description=Fugerit%20SpringBoot%20Template&packageName=org.fugerit.java.template.fugerit-springboot3-template&dependencies=web,native,devtools,lombok).

Springdoc openapi dependency has been added :

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>${r"${springdoc-openapi-ui-version}"}</version>
</dependency>
```

## Spring Boot documentation

Read the original [HELP.md](HELP.md)
