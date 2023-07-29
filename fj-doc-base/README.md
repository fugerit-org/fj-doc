# Fugerit Document Generation Framework (fj-doc)

## Core library (fj-doc-base)

[back to fj-doc index](../README.md)  

*Description* :  
Basic infrastructure for generation of [fj-doc XML format](https://www.fugerit.org/data/java/doc/xsd/doc-1-1.xsd).

*Status* :  
All basic features are implemented (plus helpers for other modules).

*Since* : fj-doc 0.1
  
*Native support* :  
Enabled since version 1.4.0-rc.001, This module should fully support native image. (*reflect-config.json* and *resources-config.json* have been added and there is no dependent library without native support)
  
*Quickstart* :

This module is based on [DocHandlerFacade](src/main/java/org/fugerit/java/doc/base/facade/DocHandlerFacade.java)
/ [DocHandlerFactory](src/main/java/org/fugerit/java/doc/base/facade/DocHandlerFactory.java)
for configuring type handlers and on [DocFacade](src/main/java/org/fugerit/java/doc/base/facade/DocFacade.java) for actual processing.  

You must write a xml meta model using the [fj-doc XML format](../fj-doc-sample/src/test/resources/sample_docs/basic.xml) :   
(It's possible to do it even in a dynamic way, for instance through [Apache FreeMarker extension](../fj-doc-freemarker/README.md))

```
<?xml version="1.0" encoding="utf-8"?>
<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-1-1.xsd" > 
  <meta>
	<info name="excel-table-id">excel-table=print</info>
	<info name="excel-width-multiplier">450</info> 
  </meta>
  <body>
    	<table columns="3" colwidths="30;30;40"  width="100" id="excel-table" padding="2">
    		<row>
    			<cell align="center"><para style="bold">Name</para></cell>
    			<cell align="center"><para style="bold">Surname</para></cell>
    			<cell align="center"><para style="bold">Title</para></cell>
    		</row>
       		<row>
    			<cell><para>Luthien</para></cell>
    			<cell><para>Tinuviel</para></cell>
    			<cell><para>Queen</para></cell>
    		</row>
       		<row>
    			<cell><para>Thorin</para></cell>
    			<cell><para>Oakshield</para></cell>
    			<cell><para>King</para></cell>
    		</row>    		
    	</table>
  </body>
</doc>
```

Here you can find a [doc-handler-sample.xml](../fj-doc-sample/src/main/resources/config/doc-handler-sample.xml) example :  

```
	<factory id="default-complete">
		<data id="pdf-itext" info="pdf" type="org.fugerit.java.doc.mod.itext.PdfTypeHandler" />
		<data id="rtf-itext" info="rtf" type="org.fugerit.java.doc.mod.itext.RtfTypeHandler" />
		<data id="html-itext" info="html" type="org.fugerit.java.doc.mod.itext.HtmlTypeHandler" />
		<data id="xls-poi" info="xls" type="org.fugerit.java.doc.mod.poi.XlsPoiTypeHandler" />
		<data id="xlsx-poi" info="xlsx" type="org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler" />
	</factory>
```

Which can be used this configured this way (see also [SampleFacade](../fj-doc-sample/src/main/java/org/fugerit/java/doc/sample/facade/SampleFacade.java)) : 

```
	private static DocHandlerFacade init( String path, String name ) {
		DocHandlerFacade facade = null;
		try {
			facade = DocHandlerFactory.register( path, name );
		} catch (Exception e) {
			throw new RuntimeException( e );
		}
		return facade;
	}
	
	private static DocHandlerFacade FACADE_MAIN = init( "cl://config/doc-handler-sample.xml", "default-complete" );
```

And a document can actually be generated this way : (see also [BasicFacadeTest](../fj-doc-sample/src/test/java/test/org/fugerit/java/doc/sample/facade/BasicFacadeTest.java)) 

```
	public static void produce( File outputFolder, DocBase docBase, String baseName, String type ) throws Exception {
		File file = new File( outputFolder, baseName + "." + type);
		logger.info("Create file {}", file.getCanonicalPath());
		try (FileOutputStream fos = new FileOutputStream(file)) {
			DocInput input = DocInput.newInput( type , docBase );
			DocOutput output = DocOutput.newOutput( fos );
			DocHandlerFacade facade = SampleFacade.getInstance(); 
			facade.handle( input , output );
		}
	}
```