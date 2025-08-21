# Fugerit Document Generation Framework (fj-doc)

## OpenPDF Renderer (PDF)(fj-doc-mod-openpdf-ext)

[back to fj-doc index](../README.md)  

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-mod-openpdf-ext.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-mod-openpdf-ext) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-mod-openpdf-ext/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-mod-openpdf-ext)

Renderer for PDF format, based on [OpenPDF](https://github.com/LibrePDF/OpenPDF).

*Status* :  
Basic features implemented. (Sample JUnit [TestFreeMarker01](../fj-doc-sample/src/test/java/test/org/fugerit/java/doc/sample/freemarker/TestFreeMarker01.java) is now working).  
  
*Since* : fj-doc 3.4
  
*Native support*  :  
Disabled, native support will be added in a future release. openpdf-ext native support must be verified.
  
*Quickstart* :  
Basically this is only a type handler, see core library [fj-doc-base](../fj-doc-base/README.md).  
NOTE: If you have any special need you can open a pull request or create your own handler based on this.

## OpenRTF version compatibility matrix

Starting with version [2.2.0](https://github.com/LibrePDF/OpenPDF/releases/tag/2.2.0) OpenRTF moved to Java 21.

| OpenPDF | Java | Notes                            |
|---------|------|----------------------------------|
| 1.3+    | 8+   | latest OpenRTF version is 1.3.43 |
| 1.4+    | 11+  | latest OpenRTF version is 1.4.2  |
| 2.0+    | 17+  | latest OpenRTF version is 2.0.5  |
| 2.2+    | 21+  | latest OpenRTF version is 2.4.0  |

NOTE: with release [3.0.0](https://github.com/LibrePDF/OpenPDF/releases/tag/3.0.0) the original IText package has been dropped.