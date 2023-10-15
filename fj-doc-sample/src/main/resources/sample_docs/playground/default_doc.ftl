<#outputformat "XML"><#-- automatically escape variables as XML : https://freemarker.apache.org/docs/dgui_misc_autoescaping.html -->
<?xml version="1.0" encoding="utf-8"?>
<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" > 

  <#--
  	This is a Venus Fugerit Doc (https://github.com/fugerit-org/fj-doc) FreeMarker Template XML (ftlx).
  	
  	For consideration of Venus Fugerit Doc and Apache FreeMarker integration see :
  	https://venusguides.fugerit.org/src/docs/common/doc_format_freemarker.html
  	
  	For documentation on how to write a valid Venus Doc XML Meta Model refer to : 
  	https://venusguides.fugerit.org/src/docs/common/doc_format_summary.html
  -->

  <metadata>
  	<!-- Margin for document : left;right;top;bottom -->
  	<info name="margins">10;10;10;30</info>  
  	<info name="excel-table-id">excel-table=print</info>
 	<!-- documenta meta information -->
	<info name="doc-title">Basic example</info>
	<info name="doc-subject">fj doc venus sample source FreeMarker Template XML - ftlx</info>
	<info name="doc-author">fugerit79</info>
	<info name="doc-language">en</info>
	<!-- font must be loaded -->
	<info name="default-font-name">TitilliumWeb</info>
	<footer-ext>
		<para align="right">${r"${currentPage}"} / ${r"${pageCount}"}</para>
	</footer-ext>
  </metadata>
  <body>
  		<para>${ftlData.docTitle!'My sample title'}</para>
    	<table columns="3" colwidths="30;30;40"  width="100" id="excel-table" padding="2">
    		<row>
    			<cell align="center" border-color="#000000" border-width="1"><para style="bold">Name</para></cell>
    			<cell align="center"><para style="bold">Surname</para></cell>
    			<cell align="center"><para style="bold">Title</para></cell>
    		</row>
       		<row>
    			<cell><para><![CDATA[Luthien]]></para></cell>
    			<cell><para><![CDATA[Tinuviel]]></para></cell>
    			<cell><para><![CDATA[Queen]]></para></cell>
    		</row>
       		<row>
    			<cell><para><![CDATA[Thorin]]></para></cell>
    			<cell><para><![CDATA[Oakshield]]></para></cell>
    			<cell><para><![CDATA[King]]></para></cell>
    		</row>     	
    	</table>
  </body>
  
</doc>
</#outputformat>