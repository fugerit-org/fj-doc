# Fugerit Document Generation Framework (fj-doc)

## Core library : yaml extension (fj-doc-base-json)

[back to fj-doc index](../README.md)  

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-base-yaml.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-base-yaml) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-base-yaml/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-base-yaml)

*Description* :  
Add the option to use a yank as document generator instead of standard xml source provided by default.

*Status* :  
All basic features are implemented (yaml parsing, conversion from and to xml)

*Since* : fj-doc 0.7

*Native support*  :  
Disabled, native support will be added in a future release.

*Doc YAML format*  
The xml and yaml format have inherent differences. this is why is important to read the [conversion conventions used](../fj-doc-base-json/xml_conversion.md) [note : the conventions refers to the json format, but the same considerations has been used for YAML format).

src/test/resources/sample/doc_test_01.yaml

Examples : 
* [sample yaml doc](src/test/resources/sample/doc_test_01.yaml)
* [sample xml doc](src/test/resources/sample/doc_test_01.xml)