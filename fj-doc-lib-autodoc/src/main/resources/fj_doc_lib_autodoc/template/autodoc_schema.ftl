<#import 'autodoc_macro_schema.ftl' as utilsSchema>
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
		<#-- <info name="html-charset">UTF-8</info> -->
	</metadata>
	<body>
		
		<!-- headings -->
    	<h head-level="1">${autodocModel.title}<#if autodocModel.version??> ${autodocModel.version}</#if></h>

		<list>
			<li><pl><phrase link="#autodoc-table-elementsIndex">${labels['autodoc.schema.category.elements']}</phrase></pl></li>
			<li><pl><phrase link="#autodoc-table-complexTypesIndex">${labels['autodoc.schema.category.complexTypes']}</phrase></pl></li>
			<li><pl><phrase link="#autodoc-table-simpleTypesIndex">${labels['autodoc.schema.category.simpleTypes']}</phrase></pl></li>
		</list>

    	<!-- table for element summary -->    	
    	<table columns="3" colwidths="20;40;40"  width="100" id="autodoc-table-elements" padding="2" alt="${autodocModel.title}">
      		<row header="true">
    			<cell colspan="3"><phrase id="autodoc-table-elementsIndex" style="bold" anchor="begin">${messageFormat(labels['autodoc.schema.mainTable.title'])} - ${messageFormat(labels['autodoc.schema.category.elements'])}</phrase></cell>
    		</row>
    		<row header="true">
    			<cell><phrase style="bold">${messageFormat(labels['autodoc.schema.mainTable.column.item'])}</phrase></cell>
    			<cell><phrase style="bold">${messageFormat(labels['autodoc.schema.mainTable.column.description'])}</phrase></cell>
				<cell><phrase style="bold">${messageFormat(labels['autodoc.schema.mainTable.column.children'])}</phrase></cell>
    		</row>
    		<#list autodocModel.elements as autodocElement>
    		<#assign xsdElement=autodocElement.xsdElement>
    		<#assign autodocType=autodocElement.autodocType>
       		<row>  
     			<cell><phrase id="element_${xsdElement.rawName}">${xsdElement.rawName}</phrase></cell>
    			<cell><phrase>${annotationAsSingleStringFun(autodocElement.xsdAnnotationDeep)}</phrase></cell>
				<cell><phrase><@utilsSchema.handleTypeChildren autodocType=autodocElement.autodocType/></phrase></cell>
    		</row>
			</#list>
    	</table>

		<!-- table for complexy type summary -->
		<table columns="3" colwidths="20;40;40"  width="100" id="autodoc-table-complexTypes" padding="2" alt="${autodocModel.title}">
			<row header="true">
				<cell colspan="3"><phrase id="autodoc-table-complexTypesIndex" style="bold" anchor="begin">${messageFormat(labels['autodoc.schema.mainTable.title'])} - ${messageFormat(labels['autodoc.schema.category.complexTypes'])}</phrase></cell>
			</row>
			<row header="true">
				<cell><phrase style="bold">${messageFormat(labels['autodoc.schema.mainTable.column.item'])}</phrase></cell>
				<cell><phrase style="bold">${messageFormat(labels['autodoc.schema.mainTable.column.description'])}</phrase></cell>
				<cell><phrase style="bold">${messageFormat(labels['autodoc.schema.mainTable.column.children'])}</phrase></cell>
			</row>
			<#list autodocModel.types as autodocType>
				<#assign xsdComplexType=autodocType.xsdComplexType>
				<row>
					<cell><phrase id="type_${xsdComplexType.rawName}">${xsdComplexType.rawName}</phrase></cell>
					<cell><phrase>${annotationAsSingleStringFun(autodocType.xsdAnnotationDeep)}</phrase></cell>
					<cell><phrase><@utilsSchema.handleTypeChildren autodocType=autodocType/></phrase></cell>
				</row>
			</#list>
		</table>

		<!-- table for complexy type summary -->
		<table columns="3" colwidths="20;40;40"  width="100" id="autodoc-table-simpleTypes" padding="2" alt="${autodocModel.title}">
			<row header="true">
				<cell colspan="3"><phrase id="autodoc-table-simpleTypesIndex" style="bold" anchor="begin">${messageFormat(labels['autodoc.schema.mainTable.title'])} - ${messageFormat(labels['autodoc.schema.category.simpleTypes'])}</phrase></cell>
			</row>
			<row header="true">
				<cell><phrase style="bold">${messageFormat(labels['autodoc.schema.mainTable.column.item'])}</phrase></cell>
				<cell><phrase style="bold">${messageFormat(labels['autodoc.schema.mainTable.column.description'])}</phrase></cell>
				<cell><phrase style="bold">${messageFormat(labels['autodoc.schema.mainTable.column.restrictions'])}</phrase></cell>
			</row>
			<#list autodocModel.simpleTypes as autodocSimpleType>
				<#assign xsdSimpleType=autodocSimpleType.xsdSimpleType>
				<row>
					<cell><phrase id="type_${xsdSimpleType.rawName}">${xsdSimpleType.rawName}</phrase></cell>
					<cell><phrase>${annotationAsSingleStringFun(autodocSimpleType.xsdAnnotationDeep)}</phrase></cell>
					<cell><phrase <#if (!autodocSimpleType.baseXsd)> link="#type_${autodocSimpleType.baseName}" </#if> >base: ${autodocSimpleType.baseName}</phrase><phrase>${autodocSimpleType.noteNoBase}</phrase></cell>
				</row>
			</#list>
		</table>

	</body>
</doc>