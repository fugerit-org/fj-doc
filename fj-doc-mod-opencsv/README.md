# Fugerit Document Generation Framework (fj-doc)

## OpenCSV Render (CSV)(fj-doc-mod-opencsv)

[back to fj-doc index](../README.md)  

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-mod-opencsv.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-mod-opencsv) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-mod-opencsv/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-mod-opencsv)
![GraalVM Ready](https://img.shields.io/badge/GraalVM-Ready-orange?style=plastic)

*Status* :  
Basic features implemented. (Sample JUnit [TestFreeMarker01](../fj-doc-sample/src/test/java/test/org/fugerit/java/doc/sample/freemarker/TestFreeMarker01.java) is now working).  
For the intrinsic limitations of the CSV format, is possible to choose a single table in the document and outputs it as CSV.  
  
*Since* : fj-doc 0.6
  
*Quickstart* :  
Basically this is only a type handler, see core library [fj-doc-base](../fj-doc-base/README.md).  
NOTE: If you have any special need you can open a pull request or create your own handler based on this.