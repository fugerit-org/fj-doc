<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org http://www.fugerit.org/data/java/doc/xsd/doc-1-10.xsd" > 	
    
	<metadata>
		<info name="default-font-size">10</info>
		<!-- for xlsx format -->
		<info name="excel-table-id">simple-table=${simpleTableModel.sheetName}</info>
		<info name="excel-try-autoresize">true</info>
		<!-- for cvs format -->
		<info name="csv-table-id">simple-table</info>
		<!-- for fixed size formats, like pdf -->
		<info name="page-width">29.7cm</info>
		<info name="page-height">21cm</info>
		<!-- language -->
		<info name="doc-language">${simpleTableModel.docLanguage}</info> 		 
	</metadata>
	<body>
		
    	<table columns="${simpleTableModel.columns}" colwidths="${simpleTableModel.colwidths}"  width="${simpleTableModel.tableWidth}" id="simple-table" padding="2">
			<#list simpleTableModel.rows as simpleRow>
       		<row header="${simpleRow.head}">
       			<#list simpleRow.cells as simpleCell>
    			<cell <#if (simpleCell.align)?? > align="${simpleCell.align}" </#if> border-width="${simpleCell.borderWidth}"><para <#if (simpleCell.style)?? > style="${simpleCell.style}" </#if>  ><#if (simpleCell.content)??>${simpleCell.content}</#if></para></cell>
    			</#list>  
    		</row>
			</#list>   		
    	</table>
		
	</body>
</doc>