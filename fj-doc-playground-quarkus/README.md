# fj-doc-playground-quarkus Project


## Running the application in dev mode as a single package

You can run the application in live mode : 
shell script  

```
cd fj-doc-playground-quarkus
mvn compile quarkus:dev -P buildreact
```

And go to the home page link [http://localhost:8080/fj-doc-playground/home](http://localhost:8080/fj-doc-playground/home)

## Running the back end (quarkus) and front end (react) separately : 

launch quarkus : 

```
cd fj-doc-playground-quarkus
mvn compile quarkus:dev
```

launch react : 

```
cd fj-doc-playground-quarkus/src/main/react
npm run start
```

## required software
* jdk 11 (tested with oracle jdk 11.0.16.1)
* apache maven (tested with apache maven 3.8.7)
* node (tested with node js 18.12.1)

## modules currently supported in playground : 
* fj-doc-val (and all its extensions)
* fj-doc-base (and all its extensions)

## functions available
* Doc Xml Editor : allow for writing documents in XML/JSON/YAML, validating them and generating some output formats (currently PDF/XLSX/HTML) [covers fj-doc-mod* modules]
* Doc Conversion : allow source conversion from/to XML/JSON/YAML [convers fj-doc-base* modules]
* Doc Type Validator : allow validation of documents in various formats [covers fj-doc-val* modules]

**NOTE**: A catalog of sample documents has been added to the Doc Xml Editor and Doc Conversion functionalities.