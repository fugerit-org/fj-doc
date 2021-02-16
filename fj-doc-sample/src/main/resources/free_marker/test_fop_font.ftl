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

  <#assign baseFontSize='20'>

  <meta>
  
  	<!-- Margin for document : left;right;top;bottom -->
  	<info name="margins">10;10;10;30</info>  
  
	<info name="excel-table-id">excel-table=print</info>
	<info name="excel-width-multiplier">450</info>
	
	<info name="default-font-name">Times New Roman</info>
	<info name="default-font-size">${baseFontSize}</info>
	<!--
	<info name="default-font-style">italic</info>
	-->

	<!--
	<info name="default-font-name">Courier New</info>
	-->  
	
	<!-- you need to escape free marker expression for currentPage --> 
	<footer-ext numbered="true" align="right">
		<para>${r"${currentPage}"}</para>
	</footer-ext>
	 
  </meta>
 
  <body>
  
  		<h head-level="1">Heading test level 1 default font</h>
  		
  		<h head-level="1" font-name="TitilliumWeb">Heading test level 1 TitilliumWeb</h>
  		
  		<h head-level="2">Heading test level 2</h>
  		
  		<h head-level="3" align="right">Heading test level 3</h>
  
  		<para align="right">Test right</para>
  		
  		<para align="left" white-space-collapse="false">             Test preserve</para>
  		
  		<para align="left" white-space-collapse="true">             Test no preserve</para>
  		
  		<para align="center" size="${sumLong(baseFontSize, 10)}">Test center</para>
  		
  		<para align="right">${messageFormat('test format -> {0} {1}', 'param1', 'param2')}</para>
  
  		<br/>
  		<br/>
  		<br/>
  
  		<list>
  			<li>
  				<liLabel>&amp;#183;</liLabel>
  				<liBody>item 1</liBody>
  			</li>
  			<li>
  				<liLabel>&amp;#183;</liLabel>
  				<liBody>item 2</liBody>
  			</li>  			
  		</list>
  
  		<phrase>Test template page apache free marker</phrase>
  		
  		<phrase font-name="Courier">Test template page apache free marker</phrase>
  
  		<image url="cl://test/img_test_green.png" scaling="100"/>
  
  		<image url="cl://test/img_test_red.png" scaling="50"/>
  
  		<image url="png" base64="${testBase64Img}" scaling="25"/>	
  
  		<para style="bold">italic</para>
  		
  		<para style="italic">bold</para>
  		
  		<para style="bolditalic">bold italic</para>
  
    	<table columns="3" colwidths="30;30;40"  width="100" id="excel-table" padding="2">
    		<row>
    			<cell align="center" border-color="#ee0000" border-width="1"><para style="bold">Name</para></cell>
    			<cell align="center"><para style="bold">Surname</para></cell>
    			<cell align="center"><para style="bold">Title</para></cell>
    		</row>
			<#list userList as user>
				<#include "/include/test_01_row.ftl">
			</#list>   		
    	</table>
  </body>
  
</doc>