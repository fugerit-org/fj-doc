# Fugerit Document Generation Framework (fj-doc)

## Apache FOP Render (PDF)(fj-doc-mod-fop)

[back to fj-doc index](../README.md)  

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-mod-fop.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-mod-fop) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-mod-fop/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-mod-fop)

*Status* :  
Most feature implemented. (Sample JUnit [TestFreeMarker01Fop](../fj-doc-sample/src/test/java/test/org/fugerit/java/doc/sample/freemarker/TestFreeMarker01Fop.java) is now working).
  
*Since* : fj-doc 0.3

*Native support*  :  
Disabled, native support will be added in a future release. Apache FOP native support must be verified.
  
*Quickstart* :  
Basically this is only a type handler, see core library [fj-doc-base](../fj-doc-base/README.md).  
NOTE: If you have any special need you can open a pull request or create your own handler based on this.

[Useful resources on Apache FOP](src/main/docs/apache-fop-ref.md)

## Handler configuration

| attribute                   | required | default | note                                                                   | 
|-----------------------------|----------|---------|------------------------------------------------------------------------|
| charset                     | no       | UTF-8   | Set the charset to be used for the handler                             |
| fop-config-mode             | no       |         | Custom configuration method : 'classloader', 'inline'                  |
| fop-config-classloader-path | no       |         | Class loader path to fop-config.xml                                    |
| pdf-a-mode                  | no       |         | pdf a profile : PDF/A-1b, PDF/A-1a, PDF/A-2a, PDF/A-3a                 |
| pdf-ua-mode                 | no       |         | pdf ua profile : PDF/UA-1                                              |
| fop-suppress-events         | no       | false   | if set to true, it will support some fop logging                       |
| fop-pool-min                | no       | 0       | Integer, if higher than 0, fop configuration will be pooled and resued |
| fop-pool-max                | no       | 0       | Integer, maximum number of fop configuration                           |




### Example of classloader configuration : 

```xml
<docHandler id="pdf-fop" info="pdf" type="org.fugerit.java.doc.mod.fop.PdfFopTypeHandler">
    <docHandlerCustomConfig charset="UTF-8" 
        fop-config-mode="classloader" 
        fop-config-classloader-path="fop-config.xml" />
</docHandler>
```

### Example of inline configuration :

```xml
<docHandler id="pdf-fop" info="pdf" type="org.fugerit.java.doc.mod.fop.PdfFopTypeHandler">
    <docHandlerCustomConfig charset="UTF-8" 
        fop-config-mode="inline" >
        <fop version="1.0">
            <strict-configuration>true</strict-configuration>
            <strict-validation>true</strict-validation>
            <renderers>
                <renderer mime="application/pdf">
                    <version>1.4</version>
                </renderer>
            </renderers>
        </fop>
    </docHandlerCustomConfig>
</docHandler>
```
