# ${context.artifactId}

## Quickstart

Requirement :

* Gradle 8+
* java ${context.javaRelease}+ (GraalVM for native version)

1. Verify the app

```shell
gradle build
```

2. Start the app

```shell
gradle quarkusDev
```

3. Try the app

Open the [swagger-ui](http://localhost:8080/q/swagger-ui/)

Test available paths (for instance : [/doc/example.md](http://localhost:8080/doc/example.md))

NOTE:

* Powered by Quarkus ${context.flavourVersion}
* Using Fugerit Venus Doc ${context.version} (extensions : ${context.extensions})

## Native version

If you picked only native modules, you should be able to build and run the AOT version (GraalVM 21+ needed).

Further documentation :

* [List of modules and native support](https://venusdocs.fugerit.org/guide/#available-extensions)
* [Fugerit Venus Doc native support introduction](https://venusdocs.fugerit.org/guide/#doc-native-support)

1. Build and verify

```shell
gradle -Dquarkus.native.enabled=true -Dquarkus.package.jar.enabled=false
```

2. Start

```shell
./build/${context.artifactId}-${context.projectVersion}-runner
```

## Overview

This project has been initialized using [fj-doc-maven-plugin init goal](https://venusdocs.fugerit.org/guide/#maven-plugin-goal-init).

The quarkus 3 structure is similar to running the quarkus create goal : 

```shell
mvn io.quarkus.platform:quarkus-maven-plugin:${context.flavourVersion}:create \
-DprojectGroupId=${context.groupId} \
-DprojectArtifactId=${context.artifactId} \
-Dextensions='rest,rest-jackson,config-yaml,smallrye-openapi'
```

## Quarkus readme

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/fj-doc-quarkus-tutorial-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
