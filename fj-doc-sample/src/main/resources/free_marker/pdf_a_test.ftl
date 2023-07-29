<?xml version="1.0" encoding="utf-8"?>
<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-0.xsd" > 

  <!-- 
  	Sample Apache FreeMarker template for Fugerit Doc.
  	Note : this example has no intention of being a guid to FreeMarker
  			(In case check FreeMarker documentation https://freemarker.apache.org/docs/index.html)
   -->

  <meta>
  
  	<!-- Margin for document : left;right;top;bottom -->
  	<info name="margins">10;10;10;30</info>  
  
 	<!-- documenta meta information -->
	<info name="doc-title">Basic example</info>
	<info name="doc-subject">fj doc venus sample source xml</info>
	<info name="doc-author">fugerit79</info>
	<info name="doc-language">en</info>
  
	<!-- id table to be used for xlsx output -->  
	<info name="excel-table-id">excel-table=print</info>
	<info name="excel-width-multiplier">450</info>
	<!-- id table to be used for xsv output -->
	<info name="csv-table-id">excel-table</info> 
	
	 <info name="default-font-name">TitilliumWeb</info>
 
 		<bookmark-tree>
			<bookmark ref="b1">Bookmark 1</bookmark>
		</bookmark-tree>
 
  </meta>
 
  <body>
  
  		<h head-level="1" id="b1">Heading test level 1 default font</h>

  </body>
  
</doc>