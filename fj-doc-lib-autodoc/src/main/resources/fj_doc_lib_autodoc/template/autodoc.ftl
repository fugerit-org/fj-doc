<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org http://www.fugerit.org/data/java/doc/xsd/doc-1-10.xsd" > 	
    
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
	</metadata>
	<body>
		
    	<h head-level="1">Reference configuration for Venus Doc Generation (fj-doc)</h>
    	
    	<table columns="3" colwidths="20;40;40"  width="100" id="autodoc-table-id" padding="2">
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
       		<row>  
     			<cell><phrase link="#${xsdElement.rawName}">${xsdElement.rawName}</phrase></cell>
    			<cell><phrase>${autodocElement.annotationAsSingleString}!''</phrase></cell>
    			<cell><phrase></phrase></cell>
    		</row>
			</#list>
    	</table>
		
    	<#list autodocModel.elements as autodocElement>
			<br/>
    		<#assign xsdElement=autodocElement.xsdElement>
    		<#assign complexType=autodocElement.complexType>
    		<phrase link="#begin">top</phrase>
    		<table columns="3" colwidths="20;40;40"  width="100" id="autodoc-table-${xsdElement.rawName}" padding="2">
  	    		<row header="true">
	    			<cell colspan="3"><phrase style="bold" anchor="${xsdElement.rawName}">Element : ${xsdElement.rawName}</phrase></cell>
	    		</row>
	    		<row header="true">
	    			<cell><phrase style="bold">Attribute</phrase></cell>
	    			<cell><phrase style="bold">Description</phrase></cell>
	    			<cell><phrase style="bold">Children</phrase></cell>
	    		</row>
	    		<#list autodocElement.xsdAttributes as xsdAttribute>
	       		<row>  
	     			<cell><phrase>${xsdAttribute.rawName}</phrase></cell>
	    			<cell><phrase>${annotationAsSingleStringFun(xsdAttribute.xsdAnnotation)}</phrase></cell>
	    			<cell><phrase></phrase></cell>
	    		</row>
	    		</#list>
    		</table>
		</#list>
		
	</body>
</doc>