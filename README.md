# Venus - Fugerit Document Generation Framework (fj-doc) 2

Framework to produce documents in different output formats starting from an XML document metamodel. (options for json and yaml source models are also available)  

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](CHANGELOG.md) 
[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc.svg)](https://central.sonatype.com/artifact/org.fugerit.java/fj-doc)
[![license](https://img.shields.io/badge/License-Apache%20License%202.0-teal.svg)](https://opensource.org/licenses/Apache-2.0)
[![code of conduct](https://img.shields.io/badge/conduct-Contributor%20Covenant-purple.svg)](https://github.com/fugerit-org/fj-universe/blob/main/CODE_OF_CONDUCT.md)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-doc&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-doc)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-doc&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-doc)
[![Known Vulnerabilities](https://snyk.io/test/github/fugerit-org/fj-doc/badge.svg)](https://snyk.io/test/github/fugerit-org/fj-doc)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/58d93495dce94c618c4299cd80eb19f1)](https://app.codacy.com/gh/fugerit-org/fj-doc/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Docker images](https://img.shields.io/badge/dockerhub-images-important.svg?logo=Docker)](https://hub.docker.com/repository/docker/fugeritorg/fj-doc-playground-quarkus/general)

![build and scan](https://github.com/fugerit-org/fj-doc/actions/workflows/build_maven_package.yml/badge.svg)
[![CI maven compatibility check](https://github.com/fugerit-org/fj-doc/actions/workflows/build_maven_compatibility.yml/badge.svg?branch=branch-compatibility)](https://github.com/fugerit-org/fj-doc/actions/workflows/build_maven_compatibility.yml)
[![CI native modules build and test](https://github.com/fugerit-org/fj-doc/actions/workflows/build_fj-doc-native-quarkus_test.yml/badge.svg?branch=develop)](https://github.com/fugerit-org/fj-doc/actions/workflows/build_fj-doc-native-quarkus_test.yml)

[![Java runtime version](https://img.shields.io/badge/run%20on-java%208+-%23113366.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/java8.html)
[![Java build version](https://img.shields.io/badge/build%20on-java%2017+-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/java17.html)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-3.9.0+-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)](https://universe.fugerit.org/src/docs/versions/maven3_9.html)
[![Online playground](https://img.shields.io/badge/Try%20Now-Online%20Playground-1AC736?style=for-the-badge&logo=Onlinect%20Playground&logoColor=white)](https://docs.fugerit.org/fj-doc-playground/home/)
[![Fugerit Github Project Conventions](https://img.shields.io/badge/Fugerit%20Org-Project%20Conventions-1A36C7?style=for-the-badge&logo=Onlinect%20Playground&logoColor=white)](https://universe.fugerit.org/src/docs/conventions/index.html)

As of version 8.10.2 most information previously contained in [README](LEGACY_README.md) are available through the new guide : 

[![HTML - Guide](https://img.shields.io/badge/HTML-Guide-blue?style=for-the-badge)](https://venusdocs.fugerit.org/guide/ "Go to project HTML documentation")
[![PDF - Guide](https://img.shields.io/badge/PDF-Guide-red?style=for-the-badge)](https://venusdocs.fugerit.org/guide/fj-doc-guide.pdf "Go to project PDF documentation")

The Core library (fj-doc-base) is all you need to start, even though typically you will use at least : 
* [fj-doc-base](fj-doc-base/README.md)
* [fj-doc-freemarker](fj-doc-freemarker/README.md)
* One or more type handlers modules

## Quickstart

### Existing project

The easiest way to add the minimum configuration to an existing project is with the 
[add](https://venusdocs.fugerit.org/guide/#maven-plugin-goal-add) 
goal of maven plugin, for example, running in a maven project base folder : 

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:add \
-Dextensions=base,freemarker,mod-fop
```

### New project

Alternatively it is possible to create a project from scratch with the  
[init](https://venusdocs.fugerit.org/guide/#maven-plugin-goal-init)
goal of maven plugin, for example, running the command : 

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:init \
-DgroupId=org.example.doc \
-DartifactId=fugerit-demo-quarkus-3 \
-Dflavour=quarkus-3
```
It will create a new folder as for the {artifactId} parameter 
(in our example : *fugerit-demo-quarkus-3*)

## Usage

For more information on framework usage, refer to the 
[guide](https://venusdocs.fugerit.org/guide/), especially : 

* [Doc Source Format](https://venusdocs.fugerit.org/guide/#doc-format-entry-point)
* [Dynamic Data](https://venusdocs.fugerit.org/guide/#doc-freemarker-entry-point)
* [Doc Handlers](https://venusdocs.fugerit.org/guide/#doc-handlers)
* [FAQ](https://venusdocs.fugerit.org/guide/#doc-faq)

Which provide the reference to customized existing and new projects.

## Bugs and requests

In case of bugs or requests please open an issue on the 
[GitHub repository issue tracker](https://github.com/fugerit-org/fj-doc/issues).

## Special thanks

**Special thanks** to [JetBrains](https://www.jetbrains.com/) 
for accepting this project in the 
[Licenses for Open Source Development - Community Support](https://jb.gg/OpenSourceSupport) program.

[![JetBrains](https://universe.fugerit.org/src/docs/thanks/jetbrains.png)](https://universe.fugerit.org/src/docs/thanks/jetbrains.html)

**Special thanks** to [Sonar Cloud](https://sonarcloud.io/), 
[Codacy](https://www.codacy.com/) and 
[Snyk](https://snyk.io/) 
too for their code review platforms.

Last but not least **thank** to all the OpenSource projects and developers whose software are the building bricks of this and many other repositories.
