<?xml version="1.0" encoding="utf-8"?>
<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" >

  <!-- 
  	Sample Apache FreeMarker template for Fugerit Doc.
  	Note : this example has no intention of being a guid to FreeMarker
  			(In case check FreeMarker documentation https://freemarker.apache.org/docs/index.html)
   -->

  <meta>
  
  	<!-- Margin for document : left;right;top;bottom -->
  	<info name="margins">10;10;10;30</info>  
  
	<!-- id table to be used for xlsx output -->  
	<info name="excel-table-id">excel-table=print</info>
	<info name="excel-width-multiplier">450</info>
	<!-- id table to be used for xsv output -->
	<info name="csv-table-id">excel-table</info> 
	
	<!-- you need to escape free marker expression for currentPage --> 
	<footer-ext>
		<para align="right">${r"${currentPage}"} / ${r"${pageCount}"}</para>
	</footer-ext>
	 
  </meta>
 
  <body>
  
  		<h head-level="1">Heading test level 1 default font</h>
  		
  		<h head-level="1">Heading test level 1 TitilliumWeb</h>
  		
  		<h head-level="2">Heading test level 2</h>
  		
  		<h head-level="3" align="right">Heading test level 3</h>
  
  		<para align="right">Test right
			<h head-level="4">not allowed here!</h>
		</para>

		<para>${cleanXml('test clean xml')}</para>

		<para>${cleanText('test clean text', 'text')}</para>

		<para align="right" attribute-not-allowed="1">${messageFormat('test format -> {0} {1}', 'param1', 'param2')}</para>

		<para align="right">${textWrap('test text wrap')}</para>

  
  		<br/>
  		<br/>
  		<br/>
  
  		<phrase>Test template page params free marker</phrase>
  
  		<image url="cl://test/img_test_green.png" scaling="100"/>
  
  		<image url="cl://test/img_test_red.png" scaling="50"/>

  </body>
  
</doc>