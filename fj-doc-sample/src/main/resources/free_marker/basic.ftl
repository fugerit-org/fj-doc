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
	<!-- id table to be used for xlsx output -->  
	<info name="excel-table-id">excel-table=print</info>
	<info name="excel-width-multiplier">450</info>
	<!-- id table to be used for xsv output -->
	<info name="csv-table-id">excel-table</info> 
  </meta>
 
  <body>
  
  		<phrase>Test template page apache free marker</phrase>
  
    	<table columns="3" colwidths="30;30;40"  width="100" id="excel-table" padding="2">
    		<row>
    			<cell align="center" border-color="#000000" border-width="1"><para style="bold">Name</para></cell>
    			<cell align="center"><para style="bold">Surname</para></cell>
    			<cell align="center"><para style="bold">Title</para></cell>
    		</row>
			<#list userList as user>
       		<row>
    			<cell><para><#if (user.name)??>${user.name}</#if></para></cell>
    			<cell><para><#if (user.surname)??>${user.surname}</#if></para></cell>
    			<cell><para><#if (user.title)??>${user.title}</#if></para></cell>
    		</row>
			</#list>   		
    	</table>
  </body>
  
</doc>