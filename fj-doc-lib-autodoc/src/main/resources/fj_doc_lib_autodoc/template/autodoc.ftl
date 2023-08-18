<#import 'autodoc_macro.ftl' as utils>
<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org http://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" > 	
    
	<metadata>
		<info name="default-font-size">10</info>
		<!-- for xlsx format -->
		<info name="excel-table-id">autodoc-table-id=Documentation</info>
		<info name="excel-try-autoresize">true</info>
		<info name="excel-fail-on-autoresize-error">false</info>
		<!-- for cvs format -->
		<info name="csv-table-id">autodoc-table-id</info>
		<!-- for fixed size formats, like pdf -->
		<info name="page-width">29.7cm</info>
		<info name="page-height">21cm</info>
		<!-- language -->
		<info name="doc-language">en</info> 
		<!-- style -->
		<info name="html-css-link">https://venusguides.fugerit.org/src/css/default_venus_docs_style.css</info> 
	</metadata>
	<body>
		
		<!-- headings -->
    	<h head-level="1">${autodocModel.title}<#if autodocModel.version??> ${autodocModel.version}</#if></h>

    	<!-- table for element summary -->    	
    	<table columns="3" colwidths="20;40;40"  width="100" id="autodoc-table-id" padding="2" alt="${autodocModel.title}">
      		<row header="true">
    			<cell colspan="3"><phrase style="bold" anchor="begin">Elements configuration reference</phrase></cell>
    		</row>
    		<row header="true">
    			<cell><phrase style="bold">Element</phrase></cell>
    			<cell><phrase style="bold">Description</phrase></cell>
    			<cell><phrase style="bold">Children</phrase></cell>
    		</row>
    		<#list autodocModel.elements as autodocElement>
    		<#assign xsdElement=autodocElement.xsdElement>
    		<#assign autodocType=autodocElement.autodocType>
       		<row>  
     			<cell><phrase link="#${xsdElement.rawName}">${xsdElement.rawName}</phrase></cell>
    			<cell><phrase>${annotationAsSingleStringFun(autodocElement.xsdAnnotationDeep)}</phrase></cell>
    			<cell><#if autodocElement.complexType.mixed><phrase>mixed </phrase></#if> <@utils.handleTypeChildren autodocType/></cell>
    		</row>
			</#list>
    	</table>
		
		<!-- tables for element detail and attributes -->
    	<#list autodocModel.elements as autodocElement>
			<br/>
    		<#assign xsdElement=autodocElement.xsdElement>
    		<#assign complexType=autodocElement.complexType>
    		<phrase link="#begin">top</phrase>
    		<table columns="3" colwidths="20;40;40"  width="100" id="autodoc-table-${xsdElement.rawName}" padding="2" alt="Documentation for element ${xsdElement.rawName}">
  	    		<row header="true">
	    			<cell colspan="3"><phrase style="bold" anchor="${xsdElement.rawName}">Element : ${xsdElement.rawName}</phrase></cell>
	    		</row>
  	    		<row header="true">
	    			<cell colspan="3"><phrase>${annotationAsSingleStringFun(autodocElement.xsdAnnotationDeep)}</phrase></cell>
	    		</row>
	    		<row header="true">
	    			<cell><phrase style="bold">Attribute</phrase></cell>
	    			<cell><phrase style="bold">Description</phrase></cell>
	    			<cell><phrase style="bold">Note</phrase></cell>
	    		</row>
	    		<#if autodocElement.autodocAttributes?size gt 0>
	    		<#list autodocElement.autodocAttributes as attribute>
	       		<row>  
	     			<cell><phrase>${attribute.xsdAttribute.rawName}</phrase></cell>
	    			<cell><phrase>${annotationAsSingleStringFun(attribute.xsdAnnotationDeep)}</phrase></cell>
	    			<cell><phrase>${attribute.note}</phrase></cell>
	    		</row>
	    		</#list>
	    		<#else>
  	    		<row>
	    			<cell colspan="3"><phrase>This element does not have attributes</phrase></cell>
	    		</row>	    		
	    		</#if>
    		</table>
		</#list>
		
	</body>
</doc>