<?xml version="1.0" encoding="utf-8"?>
<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-1-1.xsd" > 

  <!-- 
  	Sample Apache FreeMarker template for Fugerit Doc.
  	Note : this example has no intention of being a guid to FreeMarker
  			(In case check FreeMarker documentation https://freemarker.apache.org/docs/index.html)
   -->

  <meta>
	<info name="excel-table-id">excel-table=print</info>
	<!-- 
	<info name="excel-width-multiplier">450</info>
	 -->
	<info name="excel-try-autoresize">true</info> 
  </meta>
 
  <body>
  
    	<table columns="3" colwidths="30;30;40"  width="100" id="excel-table" padding="2"> 	
    		<row>
    			<cell align="center"><para style="bold" type="number">10.1</para></cell>
    			<cell align="center"><para style="bold" type="number">20.5</para></cell>
    			<cell align="center"><para style="bold" type="number">14.4</para></cell>
    		</row>
    		<row>
    			<cell align="center"><para style="bold" type="date">2019-11-15</para></cell>
    			<cell align="center"><para style="bold" type="date" format="yyyy-MM-dd">2019-11-14</para></cell>
    			<cell align="center"><para style="bold" type="date" format="yyyy-MM-dd hh:mm:ss">2019-11-13 10:11:05</para></cell>
    		</row>    		
    	</table>

  </body>
  
</doc>