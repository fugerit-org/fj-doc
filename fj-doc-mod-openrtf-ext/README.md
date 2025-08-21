# Fugerit Document Generation Framework (fj-doc)

## OpenRTF Renderer (rtf)(fj-doc-mod-openrtf-ext)

[back to fj-doc index](../README.md)  

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-mod-openrtf-ext.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-mod-openrtf-ext) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-mod-openrtf-ext/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-mod-openrtf-ext)

Renderer for rtf format, based on [OpenRTF](https://github.com/Librertf/OpenRTF).

*Status* :  
Basic features implemented. (Sample JUnit [TestFreeMarker01](../fj-doc-sample/src/test/java/test/org/fugerit/java/doc/sample/freemarker/TestFreeMarker01.java) is now working).  
  
*Since* : fj-doc 3.4
  
*Native support*  :  
Disabled, native support will be added in a future release. openrtf-ext native support must be verified.
  
*Quickstart* :  
Basically this is only a type handler, see core library [fj-doc-base](../fj-doc-base/README.md).  
NOTE: If you have any special need you can open a pull request or create your own handler based on this.

Depends on renderer : 

``` 
		<dependency>
			<groupId>org.fugerit.java</groupId>
			<artifactId>fj-doc-mod-openpdf-ext</artifactId>
		</dependency>	
```

## OpenRTF version compatibility matrix

Starting with version [2.0.0](https://github.com/LibrePDF/OpenRTF/releases/tag/2.0.0) OpenRTF moved to Java 21.

| OpenRTF | Java | Notes                                                                  |
|---------|------|------------------------------------------------------------------------|
| 1.x     | 8+   | latest OpenRTF version is 1.2.1                                        |
| 2.x     | 21+  | upgraded [OpenPDF](https://github.com/LibrePDF/OpenPDF) dependency too |