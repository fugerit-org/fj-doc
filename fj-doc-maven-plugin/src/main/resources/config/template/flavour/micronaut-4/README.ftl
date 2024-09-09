# ${context.artifactId}

## Quickstart

Requirement :
- maven 3.9.x
- java ${context.javaRelease}+

1. Start the app

```shell
mvn mn:run
```

2. Try the app

Open the [openapi-explorer](http://localhost:8080/swagger/views/openapi-explorer)

Test available routes (for instance : [/doc/example.md](http://localhost:8080/doc/example.md))

## Projects notes

the project used as template has been created with the command :

```shell
mn create-app --build=maven --jdk=${context.javaRelease} --lang=java --test=junit \
--features=openapi,openapi-explorer,yaml,graalvm,micronaut-aot,netty-server,serialization-jackson \
${context.groupId}.${context.artifactId}
```

## Micronaut 4.5.0 Documentation

- [User Guide](https://docs.micronaut.io/4.5.0/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.5.0/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.5.0/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Micronaut Maven Plugin documentation](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/)
## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)


## Feature openapi-explorer documentation

- [Micronaut OpenAPI Explorer View documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/#openapiExplorer)

- [https://github.com/Authress-Engineering/openapi-explorer](https://github.com/Authress-Engineering/openapi-explorer)


## Feature openapi documentation

- [Micronaut OpenAPI Support documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://www.openapis.org](https://www.openapis.org)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature maven-enforcer-plugin documentation

- [https://maven.apache.org/enforcer/maven-enforcer-plugin/](https://maven.apache.org/enforcer/maven-enforcer-plugin/)


