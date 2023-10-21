<#outputformat "XML"><#-- automatically escape variables as XML : https://freemarker.apache.org/docs/dgui_misc_autoescaping.html -->
<?xml version="1.0" encoding="utf-8"?>
<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" > 

  <!--
  	This is a Venus Fugerit Doc (https://github.com/fugerit-org/fj-doc) XML Source Document.
  	For documentation on how to write a valid Venus Doc XML Meta Model refer to : 
  	https://venusguides.fugerit.org/src/docs/common/doc_format_summary.html
  -->

  <metadata>
	<!-- Margin for document : left;right;top;bottom -->
	<info name="margins">10;10;10;30</info>  
	<!-- documenta meta information -->
	<info name="doc-title">${docTitle}</info>
	<info name="doc-author">fugerit79</info>
	<info name="doc-language">en</info>
	<!-- only apply to excep format -->
  	<info name="excel-table-id">data-table-1=sheet1,data-table-2=sheet2</info>
	<!-- font must be loaded -->
	<info name="default-font-name">TitilliumWeb</info>
	<footer-ext>
		<para align="right">${r"${currentPage}"} / ${r"${pageCount}"}</para>
	</footer-ext>
	<bookmark-tree>
		<bookmark ref="top">Full document sample demo</bookmark>
		<bookmark ref="sec_1">1. Sample tables</bookmark>
		<bookmark ref="sec_2">2. Sample lists</bookmark>
	</bookmark-tree>
  </metadata>
  
  <body>
	<h head-level="1" id="top" size="16">Full document sample demo</h>
	
	<para>This documents tries to show all the xml document format features.</para>
	
	<para>Current Apache FreeMarker output format : ${.output_format}.</para>

	<h head-level="2" id="sec_1" size="14">1. Sample tables</h>
	
	<para>This sections contains some sample tables.</para>
	
	<h head-level="3" size="12">1.1 Sample table : no special characteristics</h>
	
	<table colwidths="30;30;40" columns="3" width="100" id="data-table-1">
		<row header="true">
			<cell align="center"><para style="bold">Name</para></cell>
			<cell align="center"><para style="bold">Surname</para></cell>
			<cell align="center"><para style="bold">Title</para></cell>
		</row>
		<#list listPeople as current>
		<row>
			<cell><para>${current.name}</para></cell>
			<cell><para>${current.surname}</para></cell>
			<cell><para>${current.title}</para></cell>
		</row>
		</#list>
	</table>
	
	<br/>
	
	<h head-level="3" size="12">1.2 Sample table : special characteristics</h>
	
	<table colwidths="30;30;40" columns="3" width="100" id="data-table-2">
		<row header="true">
			<cell align="center"><para style="bold">Name</para></cell>
			<cell align="center"><para style="bold">Surname</para></cell>
			<cell align="center"><para style="bold">Title</para></cell>
		</row>
		<#list listPeople as current>
		<row>
			<cell><para>${current.name}</para></cell>
			<cell><para>${current.surname}</para></cell>
			<cell><para>${current.title}</para></cell>
		</row>
		</#list>
	</table>
	
	<br/>
	
	<h head-level="2" id="sec_2" size="14">2. Sample lists</h>
	
	<para>This sections contains some sample lists.</para>
	
	<#-- iterate over list types -->
	<#list listTests as listType>
	<br/>
	<h head-level="3" size="12">Sample list ${1+listType?index} : ${listType.value}</h>
	<list list-type="${listType.key}">
		<li><para>Marie Curie</para></li>
		<li><para>Alan Turing</para></li>
	</list>
	</#list>

	<page-break/>

  </body>

</doc>
</#outputformat>