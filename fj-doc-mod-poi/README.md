# Fugerit Document Generation Framework (fj-doc)

## Apache POI 4 Renderer (XLS/XLSX)(fj-doc-mod-poi)

[back to fj-doc index](../README.md)  

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-mod-poi.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-mod-poi) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-mod-poi/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-mod-poi)

*Status* :  
Most basic features implemented. (proper color and font handling missing).  
For the intrinsic limitations of the XLS/XLSX format, it is possible to choose a some tables in the document and output it as excel sheets.  
  
*Since* : fj-doc 0.1
  
*Native support*  :  
Disabled, native support will be added in a future release. Apache POI 4.X native support must be verified.
  
*Quickstart* :  
Basically this is only a type handler, see core library [fj-doc-base](../fj-doc-base/README.md). 
 
*Compatibility*  
Starting with version 2.0.0 this module is based on Apache POI 4.X.
It is still possible to override this configuration by changing the poi-version property to : 

`<poi-version>4.1.2</poi-version>`

NOTE: If you have any special need you can open a pull request or create your own handler based on this.