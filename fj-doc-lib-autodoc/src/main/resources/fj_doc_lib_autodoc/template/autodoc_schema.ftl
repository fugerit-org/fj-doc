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
		<info name="doc-title">${autodocModel.title}</info>
		<info name="doc-language">en</info> 
		<!-- style -->
		<#if (params['html-css-link'])?? ><info name="html-css-link">params['html-css-link']</info></#if>
	</metadata>
	<body>
		
		<!-- headings -->
    	<h head-level="1">${autodocModel.title}<#if autodocModel.version??> ${autodocModel.version}</#if></h>

    	<!-- table for element summary -->    	
    	<table columns="3" colwidths="20;60;20"  width="100" id="autodoc-table-id" padding="2" alt="${autodocModel.title}">
      		<row header="true">
    			<cell colspan="3"><phrase style="bold" anchor="begin">XSD configuration reference</phrase></cell>
    		</row>
    		<row header="true">
    			<cell><phrase style="bold">Item</phrase></cell>
    			<cell><phrase style="bold">Description</phrase></cell>
    			<cell><phrase style="bold">Category</phrase></cell>
    		</row>
    		<#list autodocModel.elements as autodocElement>
    		<#assign xsdElement=autodocElement.xsdElement>
    		<#assign autodocType=autodocElement.autodocType>
       		<row>  
     			<cell><phrase link="#${xsdElement.rawName}">${xsdElement.rawName}</phrase></cell>
    			<cell><phrase>${annotationAsSingleStringFun(autodocElement.xsdAnnotationDeep)}</phrase></cell>
    			<cell><phrase>element</phrase></cell>
    		</row>
			</#list>
			<#list autodocModel.types as autodocType>
				<#assign xsdComplexType=autodocType.xsdComplexType>
				<row>
					<cell><phrase link="#${xsdComplexType.rawName}">${xsdComplexType.rawName}</phrase></cell>
					<cell><phrase>${annotationAsSingleStringFun(autodocType.xsdAnnotationDeep)}</phrase></cell>
					<cell><phrase>complex type</phrase></cell>
				</row>
			</#list>
			<#list autodocModel.simpleTypes as autodocSimpleType>
				<#assign xsdSimpleType=autodocSimpleType.xsdSimpleType>
				<row>
					<cell><phrase link="#${xsdSimpleType.rawName}">${xsdSimpleType.rawName}</phrase></cell>
					<cell><phrase>${annotationAsSingleStringFun(autodocSimpleType.xsdAnnotationDeep)}</phrase></cell>
					<cell><phrase>simple type</phrase></cell>
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

		<#list autodocModel.types as autodocType>
			<br/>
			<#assign xsdComplexType=autodocType.xsdComplexType>
			<phrase link="#begin">top</phrase>
			<table columns="3" colwidths="20;40;40"  width="100" id="autodoc-table-${autodocType.key}" padding="2" alt="Documentation for complex type ${xsdComplexType.rawName}">
				<row header="true">
					<cell colspan="3"><phrase style="bold" anchor="${xsdComplexType.rawName}">Complex type : ${xsdComplexType.rawName}</phrase></cell>
				</row>
				<#--
				<row header="true">
					<cell colspan="3"><phrase>${annotationAsSingleStringFun(autodocElement.xsdAnnotationDeep)}</phrase></cell>
				</row>
				-->
				<row header="true">
					<cell><phrase style="bold">Attribute</phrase></cell>
					<cell><phrase style="bold">Description</phrase></cell>
					<cell><phrase style="bold">Note</phrase></cell>
				</row>
			</table>
		</#list>

		<#list autodocModel.simpleTypes as autodocSimpleType>
			<br/>
			<#assign xsdSimpleType=autodocSimpleType.xsdSimpleType>
			<phrase link="#begin">top</phrase>
			<table columns="2" colwidths="30;70"  width="100" id="autodoc-table-${autodocSimpleType.key}" padding="2" alt="Documentation for simple type ${xsdSimpleType.rawName}">
				<row header="true">
					<cell colspan="2"><phrase style="bold" anchor="${xsdSimpleType.rawName}">Simple type : ${xsdSimpleType.rawName}</phrase></cell>
				</row>
				<row>
					<cell><phrase>Base</phrase></cell>
					<cell><phrase link="#${xsdSimpleType.restriction.base}">${xsdSimpleType.restriction.base}</phrase></cell>
				</row>
				<row>
					<cell><phrase>Documentation</phrase></cell>
					<cell><phrase>${annotationAsSingleStringFun(autodocSimpleType.xsdAnnotationDeep)}</phrase></cell>
				</row>
			</table>
		</#list>
		
	</body>
</doc>