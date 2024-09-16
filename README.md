# Venus - Fugerit Document Generation Framework (fj-doc)  

Framework to produce documents in different output formats starting from an XML document metamodel. (options for json and yaml source models are also available)  

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](CHANGELOG.md) 
[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc)
[![license](https://img.shields.io/badge/License-Apache%20License%202.0-teal.svg)](https://opensource.org/licenses/Apache-2.0)
[![code of conduct](https://img.shields.io/badge/conduct-Contributor%20Covenant-purple.svg)](https://github.com/fugerit-org/fj-universe/blob/main/CODE_OF_CONDUCT.md)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-doc&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-doc)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-doc&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-doc)
[![Known Vulnerabilities](https://snyk.io/test/github/fugerit-org/fj-doc/badge.svg)](https://snyk.io/test/github/fugerit-org/fj-doc)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/58d93495dce94c618c4299cd80eb19f1)](https://app.codacy.com/gh/fugerit-org/fj-doc/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
![build and scan](https://github.com/fugerit-org/fj-doc/actions/workflows/build_maven_package.yml/badge.svg)
[![Docker images](https://img.shields.io/badge/dockerhub-images-important.svg?logo=Docker)](https://hub.docker.com/repository/docker/fugeritorg/fj-doc-playground-quarkus/general)

[![Java runtime version](https://img.shields.io/badge/run%20on-java%208+-%23113366.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/java8.html)
[![Java build version](https://img.shields.io/badge/build%20on-java%2017+-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://universe.fugerit.org/src/docs/versions/java17.html)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-3.9.0+-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)](https://universe.fugerit.org/src/docs/versions/maven3_9.html)
[![Online playground](https://img.shields.io/badge/Try%20Now-Online%20Playground-1AC736?style=for-the-badge&logo=Onlinect%20Playground&logoColor=white)](https://docs.fugerit.org/fj-doc-playground/home/)
[![Fugerit Github Project Conventions](https://img.shields.io/badge/Fugerit%20Org-Project%20Conventions-1A36C7?style=for-the-badge&logo=Onlinect%20Playground&logoColor=white)](https://universe.fugerit.org/src/docs/conventions/index.html)

The Core library (fj-doc-base) is all you need to start, even though typically you will use at least : 
* [fj-doc-base](fj-doc-base/README.md)
* [fj-doc-freemarker](fj-doc-freemarker/README.md)
* One or more type handlers modules

## Quickstart

### Build

Clone : 

`git clone https://github.com/fugerit-org/fj-doc`

Full build is made by : 

`mvn clean install -P full,test`

Or with the maven wrapper for linux : 

`./mvnw clean install -P full,test`

Or with the maven wrapper for windows : 

`./mvnw clean install -P full,test`

### Run

The Fugerit Venus Doc project is a library for generating documents to be included.

The module [fj-doc-playground-quarkus](fj-doc-playground-quarkus/README.md) demonstrates some of the main features.

After building, it is possible to just run it by launching : 

```
cd fj-doc-playground-quarkus
mvn compile quarkus:dev -P buildreact
```

Open : [http://localhost:8080/fj-doc-playground/home/](http://localhost:8080/fj-doc-playground/home/)

NOTE  : The playground demo single page application currently runs on [node 20](https://nodejs.org/en)


## Useful resources

- [github pages documentation](https://venusdocs.fugerit.org/)
- [project home page](https://www.fugerit.org/perm/venus)
- docgen [home](https://www.fugerit.org/data/java/doc/venus/index.html)
- [release notes](https://www.fugerit.org/data/java/doc/venus/release-notes.html)
- [Doc XSD Configuration Reference](https://venusdocs.fugerit.org/fj-doc-base/src/main/docs/doc_xsd_config_ref.html)
- [freemarker-doc-process-config XSD Configuration Reference](https://venusdocs.fugerit.org/fj-doc-freemarker/src/main/docs/fdp_xsd_config_ref.html)
- [Venus Doc Meta Information reference](https://venusdocs.fugerit.org/docs/html/doc_meta_info.html)
- [Venus Guides](https://venusguides.fugerit.org/)
- [Online playground](https://www.fugerit.org/fj-doc-playground/home/)

A quick start is available in module [fj-doc-sample](fj-doc-sample/README.md)  


## Modules

There are five kinds of components (each components README.md contains module status) : 

### 1. Framework core :

* [Core library (fj-doc-base)](fj-doc-base/README.md) (contains a simple renderer for [Markdown BASIC](fj-doc-base/src/main/java/org/fugerit/java/doc/base/typehandler/markdown/SimpleMarkdownBasicTypeHandler.java) and [Markdown EXT](fj-doc-base/src/main/java/org/fugerit/java/doc/base/typehandler/markdown/SimpleMarkdownExtTypeHandler.java))
* [Json extension (fj-doc-base-json)](fj-doc-base-json/README.md) (allow for using json as document generator instead of standard xml generator) [since 0.7.0]
* [Yaml extension (fj-doc-base-yaml)](fj-doc-base-yaml/README.md) (allow for using yaml as document generator instead of standard xml generator) [since 0.7.0]
* Doc format [![xsd](https://img.shields.io/badge/venus%20xsd-doc%202.1-purple.svg)](https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd)

### 2. Modules :
* [FreeMarker template, (fj-doc-freemarker)](fj-doc-freemarker/README.md) (contains a simple renderer for [HTML](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlTypeHandler.java) and [HTML FRAGMENT](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlFragmentTypeHandler.java) and [ADOC](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/asciidoc/FreeMarkerAsciidocTypeHandler.java) )
* [Apache POI Module (fj-doc-mod-poi)](fj-doc-mod-poi/README.md) ([XLS](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsPoiTypeHandler.java)/[XLSX](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsPoiTypeHandler.java))
* [Apache FOP Module (fj-doc-mod-fop)](fj-doc-mod-fop/README.md) ([PDF](fj-doc-mod-fop/src/main/java/org/fugerit/java/doc/mod/fop/PdfFopTypeHandler.java)/[FO](fj-doc-mod-fop/src/main/java/org/fugerit/java/doc/mod/fop/FreeMarkerFopTypeHandler.java))
* [OpenCSV Module (fj-doc-mod-opencsv)](fj-doc-mod-opencsv/README.md) ([CSV](fj-doc-mod-opencsv/src/main/java/org/fugerit/java/doc/mod/opencsv/OpenCSVTypeHandler.java))
* [OpenPDF EXT Module (fj-doc-mod-openpdf-ext)](fj-doc-mod-openpdf-ext/README.md) ([PDF](fj-doc-mod-openpdf-ext/src/main/java/org/fugerit/java/doc/mod/openpdf/ext/PdfTypeHandler.java)/[HTML](fj-doc-mod-openpdf-ext/src/main/java/org/fugerit/java/doc/mod/openpdf/ext/HtmlTypeHandler.java))
* [OpenRTF EXT Module (fj-doc-mod-openrtf-ext)](fj-doc-mod-openrtf-ext/README.md) ([RTF](fj-doc-mod-openrtf-ext/src/main/java/org/fugerit/java/doc/mod/openrtf/ext/RtfTypeHandler.java))


### 3. Available type handlers :
* [MD BASIC](fj-doc-base/src/main/java/org/fugerit/java/doc/base/typehandler/markdown/SimpleMarkdownBasicTypeHandler.java) - (fj-doc-base) output as Markdown basic language
* [MD EXT](fj-doc-base/src/main/java/org/fugerit/java/doc/base/typehandler/markdown/SimpleMarkdownExtTypeHandler.java) - (fj-doc-base) output as Markdown extended (include tables) language
* [HTML](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlTypeHandler.java) - (fj-doc-freemarker) output as html
* [HTML FRAGMENT](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlFragmentTypeHandler.java) - (fj-doc-freemarker) output as html body content only (no html, head or body tags)
* [ADOC](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/asciidoc/FreeMarkerAsciidocTypeHandler.java) - (fj-doc-freemarker) output as asciidoc
* [XLS](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsPoiTypeHandler.java) - (fj-doc-mod-poi) output as Microsoft XLS using Apache POI
* [XLSX](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsPoiTypeHandler.java) - (fj-doc-mod-poi) output as Microsoft XLSX using Apache POI
* [PDF](fj-doc-mod-fop/src/main/java/org/fugerit/java/doc/mod/fop/PdfFopTypeHandler.java) - (fj-doc-mod-fop) - output as PDF using Apache FOP
* [FO](fj-doc-mod-fop/src/main/java/org/fugerit/java/doc/mod/fop/FreeMarkerFopTypeHandler.java) - (fj-doc-mod-fop) - output as FO using Apache FOP
* [CSV](fj-doc-mod-opencsv/src/main/java/org/fugerit/java/doc/mod/opencsv/OpenCSVTypeHandler.java) - (fj-doc-mod-opencsv) - output as CSV using OpenCSV
* [PDF](fj-doc-mod-openpdf-ext/src/main/java/org/fugerit/java/doc/mod/openpdf/ext/PdfTypeHandler.java) - (fj-doc-mod-openpdf-ext) - output as PDF using OpenPDF
* [HTML](fj-doc-mod-openpdf-ext/src/main/java/org/fugerit/java/doc/mod/openpdf/ext/HtmlTypeHandler.java) - (fj-doc-mod-openpdf-ext) - output as HTML using OpenPDF
* [RTF](fj-doc-mod-openrtf-ext/src/main/java/org/fugerit/java/doc/mod/openrtf/ext/RtfTypeHandler.java) - (fj-doc-mod-openrtf-ext) - output as RTF using OpenRTF


### 4. Bundled libraries :
* [Simple table (fj-doc-lib-singletable)](fj-doc-lib-simpletable/README.md) - offers a simple API for creating a document made of a table.
* [XSD Autodoc (fj-doc-lib-autodoc)](fj-doc-lib-autodoc/README.md) - offers a simple api for documenting the Venus library (to a limited extent some features can be used on any xsd).
* [Doc type validation (fj-doc-val)](fj-doc-val/README.md) - simple utilities for validating file type.

### 5. Tutorial :
* [Samples and Quickstart (fj-doc-sample)](fj-doc-sample/README.md)
* [Playground (fj-doc-playground-quarkus)](fj-doc-playground-quarkus/README.md) [since 0.7.0]
* [Playground online version](https://www.fugerit.org/fj-doc-playground/home/)

### 6. Extension Type Handlers (Extension renders) :
Basically the same as type handlers but based on libraries not available on Maven Repository Central (es. PDFLIB).  
You can find in them in a dedicated repository [fj-doc-ext](https://gitlab.com/fugerit-org/fj-doc-ext)  

### 7. Incubator Type Handlers (Are too far from being complete) :
* [Apache PdfBox Renderer (PDF)(fj-doc-mod-pdfbox)](https://github.com/fugerit-org/fj-doc-mod-pdfbox.git) (incubator since version 0.5.0)

### 8. Deprecated Type Handlers (Will not be maintained) :
* [JXL Renderer (XLS)(fj-doc-mod-jxl)](https://github.com/fugerit-org/fj-doc-mod-jxl.git) (deprecated as [jexcelapi](https://jexcelapi.sourceforge.net/) is no longer maintained, may be used 'AS IS') (deprecated since version 0.5.0)
* [Itext 2.X Renderer (PDF/RTF/HTML)(fj-doc-mod-jxl)](https://github.com/fugerit-org/fj-doc-mod-itext.git) (deprecated as [IText 2.1.X](https://mvnrepository.com/artifact/com.lowagie/itext/2.1.7) is no longer maintained, may be used 'AS IS') (deprecated since version 0.5.0)
* [Java EE extension (fj-doc-ent)](https://github.com/fugerit-org/fj-doc-ent.git) (deprecated as not the module fj-mod-freemarker provided mostly the same features, but in a standalone mode) (deprecated since version 0.5.0)

### 9. GraalVM native support  

Beginning with version 1.4.0-rc.001, *GraalVM* metadata started to be added (*reflect-config.json* and *resources-config.json*). Initially only the *fj-doc-base* and *fj-doc-freemarker* have full support.
Actual support for other module will be added as soon as possible, but sometimes is dependent on underlying dependencies support (for example *Apache FOP* for *fj-doc-mod-fop*).
It is possible to check the current status on the module documentation, in the section *native support*

### 10. Maven Plugin

A [maven plugin](fj-doc-maven-plugin/README.md) is also available. It allows to configure a maven project for document generation, it is as simple as : 

```shell
mvn org.fugerit.java:fj-doc-maven-plugin:add \
-Dextensions=base,freemarker,mod-fop
```

See [Documentation](fj-doc-maven-plugin/README.md) for further details.

*About javadoc*  
Javadoc are far from being complete, but you can find latest version at [https://www.fugerit.org](https://www.fugerit.org/data/java/javadoc/)  
Note that, being an open source project hosted on maven central, you can find release javadoc on [javadoc.io](https://javadoc.io/doc/org.fugerit.java/fj-doc-base/)

*Special thanks*

Special thanks to **JetBrains** for accepting this project in the [Licenses for Open Source Development - Community Support](https://jb.gg/OpenSourceSupport) program.

[![JetBrains](https://universe.fugerit.org/src/docs/thanks/jetbrains.png)](https://universe.fugerit.org/src/docs/thanks/jetbrains.html)

Special thanks to **Sonar Cloud** too for their code review platform.
