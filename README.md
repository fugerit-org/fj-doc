# Venus - Fugerit Document Generation Framework (fj-doc)  

Framework to produce documents in different formats starting from an XML document meta model.  

The Core library (fj-doc-base) is all you need to start, even though typically you will use at least : 
* fj-doc-base
* fj-doc-freemarker
* One or more type handlers

A quick start is available in module [fj-doc-sample](fj-doc-sample/README.md)  

There are five kinds of components (each components README.md contains module status) : 

### 1. Framework core :
* [Core library (fj-doc-base)](fj-doc-base/README.md)
* Doc format XSD [public](https://www.fugerit.org/data/java/doc/xsd/doc-1-1.xsd) and [private](fj-doc-base/src/main/resources/config/doc-1-1.xsd)

### 2. Feature extensions :
* [FreeMarker template, (fj-doc-freemarker)](fj-doc-freemarker/README.md) (containes a simple renderer for [HTML](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlTypeHandler.java) and [HTML FRAGMENT](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlFragmentTypeHandler.java))
* [Java EE extension (fj-doc-ent)](fj-doc-ent/README.md)

### 3. Type Handlers (Render) :
* [Apache POI Renderer (fj-doc-mod-poi)](fj-doc-mod-poi/README.md) ([XLS](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsPoiTypeHandler.java)/[XLSX](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsxPoiTypeHandler.java))
* [Apache PdfBox Renderer (PDF)(fj-doc-mod-pdfbox)](fj-doc-mod-pdfbox/README.md)
* [Itext 2.X Renderer (PDF/RTF/HTML)(fj-doc-mod-itext)](fj-doc-mod-itext/README.md)
* [JXL Renderer (XLS)(fj-doc-mod-jxl)](fj-doc-mod-jxl/README.md)

### 4. Tutorial :
* [Samples and Quickstart (fj-doc-sample)](fj-doc-sample/README.md)

### 5. Extension Type Handlers (Extension renders) :
Basically the same as type handlers but based on libraries non available on Maven Repository Central (es. PDFLIB).  
You can find in them in a dedicated repository [fj-doc-ext](https://gitlab.com/fugerit-org/fj-doc-ext)  

*About javadoc*  
Javadoc are far from being complete, but you can find latest version at [https://www.fugerit.org](https://www.fugerit.org/data/java/javadoc/)  
Note that, being an open source project hosted on maven central, you can find release javadoc on [javadoc.io](https://javadoc.io/doc/org.fugerit.java/fj-doc-base/)