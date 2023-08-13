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
	<info name="excel-table-id">excel-table=print</info>
 	<!-- documenta meta information -->
	<info name="doc-title">Basic example for freemarker 02</info>
	<info name="doc-subject">fj doc venus sample source xml</info>
	<info name="doc-author">fugerit79</info>
	<info name="doc-language">en</info>
	<!-- xlsx / xls properties -->
	<info name="excel-try-autoresize">true</info> 
  </meta>
 
  <body>
  
    	<table columns="3" colwidths="30;30;40"  width="100" id="excel-table" padding="2">
    		<row>
    			<cell align="center" colspan="3" border-width="1"><para style="bold">People list</para></cell>
    		</row> 	
    		<row>
    			<cell align="center" border-width="1"><para style="bold">Name</para></cell>
    			<cell align="center" border-width="1"><para style="bold">Surname</para></cell>
    			<cell align="center" border-width="1"><para style="bold">Title</para></cell>
    		</row>
    		<#assign x = 0>
			<#list userList as user>
				<#assign x = x+1>
				<#include "/include/test_01_row.ftl">
			</#list>  
    		<row>
    			<cell align="center" valign="middle" colspan="2" rowspan="2"><para style="bold">Total</para></cell>
    			<cell align="center"><para style="bold">#</para></cell>
    		</row>
    		<row>
    			<cell align="center"><para style="bold">${x}</para></cell>
    		</row>			    					
    	</table>

  </body>
  
</doc>