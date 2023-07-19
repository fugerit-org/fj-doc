# Venus - Fugerit Document Generation Framework (fj-doc)  

Framework to produce documents in different formats starting from an XML document meta model.  

The Core library (fj-doc-base) is all you need to start, even though typically you will use at least : 
* fj-doc-base
* fj-doc-freemarker
* One or more type handlers modules

Useful resources : [github pages documentation](https://venusdocs.fugerit.org/) | [project home page](https://www.fugerit.org/perm/venus) | docgen [home](https://www.fugerit.org/data/java/doc/venus/index.html) | [release notes](https://www.fugerit.org/data/java/doc/venus/release-notes.html) | [Doc XSD Configuration Reference](https://venusdocs.fugerit.org/fj-doc-base/src/main/docs/doc_xsd_config_ref.html) | [freemarker-doc-process-config XSD Configuration Reference](https://venusdocs.fugerit.org/fj-doc-freemarker/src/main/docs/fdp_xsd_config_ref.html)

A quick start is available in module [fj-doc-sample](fj-doc-sample/README.md)  

There are five kinds of components (each components README.md contains module status) : 

### 1. Framework core :
* [Core library (fj-doc-base)](fj-doc-base/README.md) (contains a simple renderer for [Markdowm BASIC](fj-doc-base/src/main/java/org/fugerit/java/doc/base/typehandler/markdown/SimpleMarkdownBasicTypeHandler.java) and [Markdown EXT](fj-doc-base/src/main/java/org/fugerit/java/doc/base/typehandler/markdown/SimpleMarkdownExtTypeHandler.java))
* [Json extension (fj-doc-base-json)](fj-doc-base-json/README.md) (allow for using json as document generator instead of standard xml generator) [since 0.7.0]
* [Yaml extension (fj-doc-base-yaml)](fj-doc-base-yaml/README.md) (allow for using yaml as document generator instead of standard xml generator) [since 0.7.0]
* Doc format XSD [public](https://www.fugerit.org/data/java/doc/xsd/doc-2-0.xsd) and [private](fj-doc-base/src/main/resources/config/doc-2-0.xsd)

### 2. Modules :
* [FreeMarker template, (fj-doc-freemarker)](fj-doc-freemarker/README.md) (contains a simple renderer for [HTML](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlTypeHandler.java) and [HTML FRAGMENT](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlFragmentTypeHandler.java))
* [Apache POI Module (fj-doc-mod-poi)](fj-doc-mod-poi/README.md) ([XLS](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsPoiTypeHandler.java)/[XLSX](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsPoiTypeHandler.java))
* [Apache FOP Module (fj-doc-mod-fop)](fj-doc-mod-fop/README.md) ([PDF](fj-doc-mod-fop/src/main/java/org/fugerit/java/doc/mod/fop/PdfFopTypeHandler.java)/[FO](fj-doc-mod-fop/src/main/java/org/fugerit/java/doc/mod/fop/FreeMarkerFopTypeHandler.java))
* [OpenCSV Module (fj-doc-mod-opencsv)](fj-doc-mod-opencsv/README.md) ([CSV](fj-doc-mod-opencsv/src/main/java/org/fugerit/java/doc/mod/opencsv/OpenCSVTypeHandler.java))


### 3. Available type handlers :
* [MD BASIC](fj-doc-base/src/main/java/org/fugerit/java/doc/base/typehandler/markdown/SimpleMarkdownBasicTypeHandler.java) - (fj-doc-core) output as Markdown basic language
* [MD EXT](fj-doc-base/src/main/java/org/fugerit/java/doc/base/typehandler/markdown/SimpleMarkdownExtTypeHandler.java) - (fj-doc-core) output as Markdown extended (include tables) language
* [HTML](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlTypeHandler.java) - (fj-doc-freemarker) output as html
* [HTML FRAGMENT](fj-doc-freemarker/src/main/java/org/fugerit/java/doc/freemarker/html/FreeMarkerHtmlFragmentTypeHandler.java) - (fj-doc-freemarker) output as html body content only (no html, head or body tags)
* [XLS](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsPoiTypeHandler.java) - (fj-doc-mod-poi) output as Microsoft XLS using Apache POI
* [XLSX](fj-doc-mod-poi/src/main/java/org/fugerit/java/doc/mod/poi/XlsPoiTypeHandler.java) - (fj-doc-mod-poi) output as Microsoft XLSX using Apache POI
* [PDF](fj-doc-mod-fop/src/main/java/org/fugerit/java/doc/mod/fop/PdfFopTypeHandler.java) - (fj-doc-mod-fop) - output as PDF using Apache FOP
* [FO](fj-doc-mod-fop/src/main/java/org/fugerit/java/doc/mod/fop/FreeMarkerFopTypeHandler.java) - (fj-doc-mod-fop) - output as FO using Apache FOP
* [CSV](fj-doc-mod-opencsv/src/main/java/org/fugerit/java/doc/mod/opencsv/OpenCSVTypeHandler.java) - (fj-doc-mod-opencsv) - output as CSV using OpenCSV

### 4. Bundled libraries :
* [Simple table (fj-doc-lib-singletable)](fj-doc-lib-simpletable/README.md) - offers a simple API for creating a document made of a table.
* [XSD Autodoc (fj-doc-lib-autodoc)](fj-doc-lib-autodoc/README.md) - offers a simple api for documenting the Venus library (to a limited extent some features can be used on any xsd).
* [Doc type validation (fj-doc-val)](fj-doc-val/README.md) - simple utilities for validating file type.

### 5. Tutorial :
* [Playground (fj-doc-playground-quarkus)](fj-doc-playground-quarkus/README.md) [since 0.7.0]
* [Samples and Quickstart (fj-doc-sample)](fj-doc-sample/README.md)

### 6. Extension Type Handlers (Extension renders) :
Basically the same as type handlers but based on libraries non available on Maven Repository Central (es. PDFLIB).  
You can find in them in a dedicated repository [fj-doc-ext](https://gitlab.com/fugerit-org/fj-doc-ext)  

### 7. Incubator Type Handlers (Are too far from being complete) :
* [Apache PdfBox Renderer (PDF)(fj-doc-mod-pdfbox)](https://github.com/fugerit-org/fj-doc-mod-pdfbox.git) (incubator since version 0.5.0)

### 8. Deprecated Type Handlers (Will not be maintained) :
* [JXL Renderer (XLS)(fj-doc-mod-jxl)](https://github.com/fugerit-org/fj-doc-mod-jxl.git) (deprecated as [jexcelapi](https://jexcelapi.sourceforge.net/) is no longer mantained, may be used 'AS IS') (deprecated since version 0.5.0)
* [Itext 2.X Renderer (PDF/RTF/HTML)(fj-doc-mod-jxl)](https://github.com/fugerit-org/fj-doc-mod-itext.git) (deprecated as [IText 2.1.X](https://mvnrepository.com/artifact/com.lowagie/itext/2.1.7) is no longer mantained, may be used 'AS IS') (deprecated since version 0.5.0)
* [Java EE extension (fj-doc-ent)](https://github.com/fugerit-org/fj-doc-ent.git)) (deprecated as not the module fj-mod-freemarker provided mostly the same features, but in a standalone mode) (deprecated since version 0.5.0)

*About javadoc*  
Javadoc are far from being complete, but you can find latest version at [https://www.fugerit.org](https://www.fugerit.org/data/java/javadoc/)  
Note that, being an open source project hosted on maven central, you can find release javadoc on [javadoc.io](https://javadoc.io/doc/org.fugerit.java/fj-doc-base/)