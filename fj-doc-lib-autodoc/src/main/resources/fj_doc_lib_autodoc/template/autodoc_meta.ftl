<#import 'autodoc_common.ftl' as common>
<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org http://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" > 	
    
  <#--
  	This is a Venus Fugerit Doc (https://github.com/fugerit-org/fj-doc) XML Source Document.
  	For documentation on how to write a valid Venus Doc XML Meta Model refer to : 
  	https://venusguides.fugerit.org/src/docs/common/doc_format_summary.html
  -->
    
	<#assign admProps=autodocMetaModel.admProperties>
	<#assign autodocMeta=autodocMetaModel.autodocMeta>
    
	<metadata>
		<info name="default-font-size">10</info>
		<@common.printMetaInfo admProps 'doc-title' />
		<@common.printMetaInfo admProps 'doc-author' />
		<info name="doc-creator">${admProps['doc-creator']!'Fugetit Venus Autodoc Meta Template'}</info>
		<!-- for xlsx format -->
		<info name="excel-table-id">autodoc-table-id=Documentation</info>
		<info name="excel-try-autoresize">true</info>
		<info name="excel-fail-on-autoresize-error">false</info>
		<!-- for cvs format -->
		<info name="csv-table-id">autodoc-table-id</info>
		<!-- for fixed size formats, like pdf -->
		<info name="page-width">29.7cm</info>
		<info name="page-height">21cm</info>
		<@common.printMetaInfo admProps 'html-css-link' />
		<!-- language -->
		<info name="doc-language">en</info> 		 
	</metadata>
	<body>
		
		<!-- headings -->
    	<phrase anchor="top"></phrase><h head-level="1"><![CDATA[${admProps['doc-title']}]]></h>

  	    <#if (admProps['doc-version'])??><para><![CDATA[Version : ${admProps['doc-version']}]]></para></#if>
  	    <#if (admProps['doc-subject'])??><para><![CDATA[${admProps['doc-subject']}]]></para></#if>
  	
		<list>
		<#list autodocMeta.admSection as admSection>
	  		<li><pl><phrase link="#${admSection.name}"><![CDATA[${admSection.description}]]></phrase></pl></li>
		</#list>
		</list>

		<phrase link="#top">top</phrase>

		<#list autodocMeta.admSection as admSection>
	  		
	  	<phrase anchor="${admSection.name}"></phrase><h head-level="2">${admSection.description}</h>
  	
	    <table columns="5" colwidths="20;40;25;10;5" id="excel-table" padding="2" width="100" caption="${admSection.description}">
	      <row header="true">
	        <cell align="center"><para style="bold">Name</para></cell>
	        <cell align="center"><para style="bold">Description</para></cell>
	        <cell align="center"><para style="bold">Supporting handlers</para></cell>
	        <cell align="center"><para style="bold">Default</para></cell>
	        <cell align="center"><para style="bold">Since</para></cell>
	      </row>
	  	  <#list admSection.admMetaInfo as admMetaInfo>
	      <row>
	        <cell><para style="italic"><![CDATA[${admMetaInfo.name}]]></para><phrase anchor="${admMetaInfo.name}"></phrase></cell>
	        <cell><para><![CDATA[${admMetaInfo.description}]]></para></cell>
	        <cell><para><![CDATA[<#list admMetaInfo.supportedHandler as handler>${handler}, </#list>]]></para></cell>
	        <cell><para><![CDATA[${admMetaInfo.defaultValue!''}]]></para></cell>
	        <cell><para><![CDATA[${admMetaInfo.since!''}]]></para></cell>
	      </row>
	  	  </#list>
	  	</table>
	  	
	  	<phrase link="#top">top</phrase>
		</#list>
									
	</body>
</doc>