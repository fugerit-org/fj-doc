# fj-doc-native-quarkus

![GraalVM Ready](https://img.shields.io/badge/GraalVM-Ready-orange?style=plastic)

## Quickstart

Requirement :

- maven 3.9.x
- GraalVM 21+

1. Build the app

```shell
git clone https://github.com/fugerit-org/fj-doc
mvn clean install
cd fj-doc-native-quarkus
mvn install -Dnative -Ptest
```

2. Run the app

```shell
./target/fj-doc-native-quarkus*-runner
```

3. Test the app

Open available test paths, for instance : 

* <http://localhost:8080/doc/example.md>
* <http://localhost:8080/doc/example.html>
* <http://localhost:8080/doc/example.adoc>
