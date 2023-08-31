# Fugerit Document Generation Framework (fj-doc)

## Core library : json extension (fj-doc-base-json)

[back to fj-doc index](../README.md)  

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-base-json.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-base-json) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-base-json/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-base-json)

*Description* :  
Add the option to use a json as document generator instead of standard xml source provided by default.

*Status* :  
All basic features are implemented (json parsing, conversion from and to xml)

*Since* : fj-doc 0.7

*Native support*  :  
Disabled, native support will be added in a future release.

*Doc Json format*  
The xml and json format have inherent differences. this is why is important to read the [conversion conventions used](xml_conversion.md)

Examples : 
* [sample json doc](src/test/resources/sample/doc_test_01.json)
* [sample xml doc](src/test/resources/sample/doc_test_01.xml)