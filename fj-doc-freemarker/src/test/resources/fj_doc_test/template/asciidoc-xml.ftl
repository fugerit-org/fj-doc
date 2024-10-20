<?xml version="1.0" encoding="utf-8"?>
<doc
	xmlns="http://javacoredoc.fugerit.org"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" > 

  <metadata>
  	<!-- Margin for document : left;right;top;bottom -->
  	<info name="margins">10;10;10;30</info>  
  	<info name="excel-table-id">excel-table=print</info>
 	<!-- documenta meta information -->
	<info name="doc-title">The asciidoc chronicles</info>
	<info name="doc-subject">fj doc venus sample source xml</info>
	<info name="doc-author">fugerit79</info>
	<info name="doc-language">en</info>
	<!-- additional properties -->
	<info name="set-total-page">true</info>
	<info name="html-css-link">/css/test.css</info>
	<!-- CSV options -->
	<info name="csv-table-id">excel-table</info>
	<header-ext>
		<para align="center" fore-color="#eeeeee">header test</para>
	</header-ext>
	<footer-ext>
		<para align="left">test</para>
		<!--
			if going throw freemarker processing should be :
			${r"${currentPage}"} / ${r"${pageCount}"}
		-->
		<para align="center">${currentPage} / ${pageCount}</para>
		<para align="right">test</para>
	</footer-ext>
	<bookmark-tree>
		<bookmark ref="title">Test</bookmark>
	</bookmark-tree>
  </metadata>
  <body>

  		<h id="title" head-level="1">The asciidoc chronicles</h>

	    <para>This is a normal paragraph</para>
  		<para style="bold">This is a bold paragraph</para>
	 	<para style="italic">This is an italic paragraph</para>
	    <para style="bolditalic">This is a bold italic paragraph</para>

	  <table columns="3" colwidths="30;30;40" width="100" id="excel-table" padding="2">
		  <row header="true">
			  <cell>
				  <para style="bold">Name</para>
			  </cell>
			  <cell>
				  <para style="bold">Surname</para>
			  </cell>
			  <cell>
				  <para style="bold">Title</para>
			  </cell>
		  </row>
		  <row>
			  <cell>
				  <para><![CDATA[Luthien]]></para>
			  </cell>
			  <cell>
				  <para><![CDATA[Tinuviel]]></para>
			  </cell>
			  <cell>
				  <para style="italic"><![CDATA[Queen]]></para>
			  </cell>
		  </row>
		  <row>
			  <cell>
				  <para><![CDATA[Thorin]]></para>
			  </cell>
			  <cell>
				  <para><![CDATA[Oakshield]]></para>
			  </cell>
			  <cell>
				  <para style="italic"><![CDATA[King]]></para>
			  </cell>
		  </row>
		  <row>
			  <cell colspan="2">
				  <phrase><![CDATA[Galadriel]]></phrase>
			  </cell>
			  <cell rowspan="2">
				  <phrase style="italic"><![CDATA[Queen]]></phrase>
			  </cell>
		  </row>
		  <row>
			  <cell colspan="2">
				  <phrase><![CDATA[Arwen]]></phrase>
			  </cell>
		  </row>
	  </table>

	  <page-break/>

	  <list list-type="ol">
		  <li>
			  <para>Linux</para>
			  <list list-type="ol">
				  <li><para>Debian</para></li>
				  <li><para>Red Hat</para></li>
				  <li><para>Slackware</para></li>
			  </list>
		  </li>
		  <li><para>Mac OS</para></li>
		  <li><para>Windows</para>
			  <list list-type="ul">
				  <li><para>Windows 10</para></li>
				  <li><para>Windows 11</para></li>
			  </list>
		  </li>
	  </list>

	  <image alt="Fugerit Org Logo" type="png" base64="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"/>

  </body>
  
</doc>