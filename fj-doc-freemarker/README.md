# Fugerit Document Generation Framework (fj-doc)

## FreeMarker template (fj-doc-freemarker)

[back to fj-doc index](../README.md)

[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-doc-freemarker.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-doc-freemarker) 
[![javadoc](https://javadoc.io/badge2/org.fugerit.java/fj-doc-freemarker/javadoc.svg)](https://javadoc.io/doc/org.fugerit.java/fj-doc-freemarker)
![GraalVM Ready](https://img.shields.io/badge/GraalVM-Ready-orange?style=plastic)

*Description* :  
Allow generation of [fj-doc XML format](https://www.fugerit.org/data/java/doc/xsd/doc-1-1.xsd) processing Apache FreeMarker templates.

*Status* :  
All basic features are implemented.  
  
*Since* : fj-doc 0.1
  
*Native support* :  
Enabled since version 1.4.0-rc.001, This module should fully support native image. (*reflect-config.json* and *resources-config.json* have been added and there is no dependent library without native support)
  
*Quickstart* :

This module is based on a step pipeline implemented through [DocProcessConfig](../fj-doc-base/src/main/java/org/fugerit/java/doc/base/process/DocProcessConfig.java). 

Create a doc-handler.xml and doc-process.xml configuration.  
(see [fj-doc-base](../fj-doc-base/README.md),
[doc-handler-sample.xml](../fj-doc-sample/src/main/resources/config/doc-handler-sample.xml)
and [doc-process-sample.xml](../fj-doc-sample/src/main/resources/config/doc-process-sample.xml))
 
For every chain you will need one configuration step and one or more processing step. 

You can place configuration step in a basic chain to extends : 

```
	<chain id="shared">
		<step id="step-01" defaultBehaviour="CONTINUE"
			description="FreeMarker Configuration step, only one FreeMarker configuration instance is created for every key under 'param01'" 
			type="org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep"
			param01="DEFAULT_CONFIG">
			<properties 
				version="2.3.29"
				path="/free_marker"
				mode="class" 
				class="org.fugerit.java.doc.sample.facade.SampleFacade"
				exception-handler="RETHROW_HANDLER"
				log-exception="false"
				wrap-unchecked-exceptions="true"
				fallback-on-null-loop-variable="false" />
		</step>
	</chain>
```

Here is an example of full hcain with a data step and a processing step.

```
	<chain id="free-marker-01" extends="shared">
		<step id="step-data" defaultBehaviour="CONTINUE"
			description="Creates data necessary to FreeMarker" 
			type="test.org.fugerit.java.doc.sample.freemarker.TestFreeMarker01DataStep"/>	
		<step id="step-data" defaultBehaviour="CONTINUE"
			description="Map items from DocContext to FreeMarker Data" 
			type="org.fugerit.java.doc.freemarker.config.FreeMarkerMapStep">		
			<properties list="userList" />		
		</step>			
		<step id="step-process" defaultBehaviour="CONTINUE"
			description="Apply FreeMarker template to get the full XML" 
			type="org.fugerit.java.doc.freemarker.config.FreeMarkerProcessStep"
			param01="test_01.xml"/>
	</chain>
```

And here is sample code to put all together up to XML composition : 

```
	private static DocProcessConfig init() {
		DocProcessConfig config = null;
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "config/doc-process-sample.xml" ) ) {
			config = DocProcessConfig.loadConfig( is );
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Exception on init : "+e, e );
		}
		return config;
	}
	
	private static DocProcessConfig PROCESS_CONFIG = init();

	public DocBase process( String chainId ) throws Exception {
		// required : access to che processing chain
		MiniFilterChain chain = PROCESS_CONFIG.getChainCache( chainId );
		DocProcessContext context = new DocProcessContext();
		DocProcessData data = new DocProcessData();
		int res = chain.apply( context , data );
		logger.info( "RES {} ", res );
		// optional : validate and print XSD errors : 
		try ( Reader input = data.getCurrentXmlReader() ) {
			DocValidator.logValidation( input , logger );
		}
		// required : parsing the XML for model to be passed to DocFacade
		DocBase docBase = null;
		try ( Reader input = data.getCurrentXmlReader() ) {
			 docBase = DocFacade.parse( input );
		}
		return docBase;
	}
```