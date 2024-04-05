# fj-doc-playground-quarkus Project

*Since* : fj-doc 0.7

Playground to demonstrate Fugerit Venus Doc project features.

## 1. Quickstart : Run built container image

A public docker repository is available (since version 3.1.5) : 

`https://hub.docker.com/repository/docker/fugeritorg/fj-doc-playground-quarkus/general`

Running the project is as simple as : 

```shell
docker run -it -p 8080:8080 --name fj-doc-playground-quarkus fugeritorg/fj-doc-playground-quarkus:latest
```

Open : [http://localhost:8080/fj-doc-playground/home/](http://localhost:8080/fj-doc-playground/home/)

## 2. Quickstart : Running the application in dev mode as a single package

You can run the application in live mode : 
shell script  

```shell
cd fj-doc-playground-quarkus
mvn compile quarkus:dev -P buildreact
```

Open : <http://localhost:8080/fj-doc-playground/home/>

## 3. Quickstart : Running the back end (quarkus) and front end (react) separately : 

launch quarkus (java 17+): 

```shell
cd fj-doc-playground-quarkus
mvn compile quarkus:dev
```

launch react frontend (node 20+) : 

```shell
cd fj-doc-playground-quarkus/src/main/react
npm run start
```

Open : [http://localhost:3000/fj-doc-playground/home/](http://localhost:3000/fj-doc-playground/home/)

## 4. required software

- jdk (tested with oracle graalvm 21.0.2)
- apache maven (tested with apache maven 3.9.6)
- node (tested with node js v20.11.0)

## 5. modules currently supported in playground : 

- fj-doc-val (and all its extensions)
- fj-doc-base (and all its extensions)

## 5. functions available

- Doc Editor and Generator : allow for writing documents in FTLX/XML/JSON/YAML, validating them and generating some output formats (currently PDF/XLSX/HTML) [covers fj-doc-mod* modules]
- Doc Conversion : allow source conversion from/to XML/JSON/YAML [convers fj-doc-base* modules]
- Doc Type Validator : allow validation of documents in various formats [covers fj-doc-val* modules]

**NOTE**: A catalog of sample documents has been added to the Doc Xml Editor and Doc Conversion functionalities.