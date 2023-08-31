# Fugerit Document Generation Framework (fj-doc)

## Apache POI 5+ Renderer (XLS/XLSX)(fj-doc-mod-poi5)

[back to fj-doc index](../README.md)  

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-mod-poi5.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-mod-poi5) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-mod-poi5/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-mod-poi5)

*Description* :
This module is used for using importing Apache POI 5+, but at core it still use the normal [fj-doc-mod-poi module](../fj-doc-mod-poi/README.md). 

*Status* :  
Most basic features implemented. (proper color and font handling missing).  
For the intrinsic limitations of the XLS/XLSX format, it is possible to choose a some tables in the document and output it as excel sheets.   

*Since* : fj-doc 1.4
  
*Quickstart* :  
Basically this is only a type handler, see core library [fj-doc-base](../fj-doc-base/README.md).  

*Native support*  :  
Disabled, native support will be added in a future release. Apache POI 5.X native support must be verified.

*Compatibility*  
This module is based on Apache POI 5.X, which is compatible with java 11+. If you are using java 8 you can use the [fj-doc-mod-poi](../fj-doc-mod-poi/README.md)   module. 

NOTE: If you have any special need you can open a pull request or create your own handler based on this.