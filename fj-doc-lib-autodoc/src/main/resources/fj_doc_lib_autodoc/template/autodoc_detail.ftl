<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org http://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" > 	
    
    <#assign adProps=autodocDetailModel.adProperties>
	<#assign autodocDetail=autodocDetailModel.autodocDetail>

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
		<info name="doc-title">${adProps['output-title']}</info>
		<info name="doc-language">en</info> 
		<!-- style -->
		<info name="html-css-link">https://venusguides.fugerit.org/src/css/default_venus_docs_style.css</info> 		 
	</metadata>
	<body>
		
		<!-- headings -->
    	<h head-level="1">${adProps['output-title']}</h>
		
		<!-- tables for element detail and attributes -->
    	<#list autodocDetail.adElement as adElement>
			<br/>
    		<table columns="3" colwidths="20;40;40"  width="100" id="autodoc-table-${adElement.name}" padding="2" caption="Documentation for element ${adElement.name}">
  	    		<row header="true">
	    			<cell colspan="3"><phrase style="bold" anchor="${adElement.name}">Element : ${adElement.name}</phrase></cell>
	    		</row>
  	    		<row header="true">
	    			<cell colspan="3"><phrase>${adElement.description}</phrase></cell>
	    		</row>
  	    		<row header="true">
	    			<cell colspan="3"><phrase>${adElement.detail}</phrase></cell>
	    		</row>
	    		<row header="true">
	    			<cell><phrase style="bold">Attribute</phrase></cell>
	    			<cell><phrase style="bold">Description</phrase></cell>
	    			<cell><phrase style="bold">Detail</phrase></cell>
	    		</row>
	    		<#if adElement.adAttribute?size gt 0>
	    		<#list adElement.adAttribute as adAttribute>
	       		<row>  
	     			<cell><phrase>${adAttribute.name}</phrase></cell>
	    			<cell><phrase>${adAttribute.description}</phrase></cell>
	    			<cell><phrase>${adAttribute.detail}</phrase></cell>
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