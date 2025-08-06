<#ftl output_format="XML"><#-- Set the output format for XML, not mandatory but reccomended, otherwise escaping should be handled in a cusomt way, for example through CDATA sections -->
<?xml version="1.0" encoding="utf-8"?>
<doc
		xmlns="http://javacoredoc.fugerit.org"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" >

	<metadata>
		<!-- Margin for document : left;right;top;bottom -->
		<info name="margins">10;10;10;30</info>
		<!-- documenta meta information -->
		<info name="doc-title">${docTitle!defaultTitle}</info>
		<info name="doc-subject">fj doc venus sample source FreeMarker Template XML - ftlx</info>
		<info name="doc-author">fugerit79</info>
		<info name="doc-language">en</info>
		<!-- font must be loaded -->
		<info name="default-font-name">TitilliumWeb</info>
		<!-- property specific for xls/xlsx -->
		<info name="excel-table-id">data-table=print</info>
		<!-- property specific for csv -->
		<info name="csv-table-id">data-table</info>
		<footer-ext>
			<para align="right">${r"${currentPage}"} / ${r"${pageCount}"}</para>
		</footer-ext>
	</metadata>
	<body>
	<para>${docTitle!defaultTitle}</para>
	<table columns="3" colwidths="30;30;40"  width="100" id="data-table" padding="2">
		<row header="true">
			<cell rowspan="2" align="center"><para>Title</para></cell>
			<cell colspan="2" align="center"><para>Data</para></cell>
		</row>
		<row header="true">
			<cell align="center"><para>Name</para></cell>
			<cell align="center"><para>Surname</para></cell>
		</row>
		<#if listPeople??>
			<#list listPeople as current>
				<row>
					<cell><para>${current.title}</para></cell>
					<cell><para>${current.name}</para></cell>
					<cell><para>${current.surname}</para></cell>
				</row>
			</#list>
		</#if>
	</table>
	</body>

</doc>