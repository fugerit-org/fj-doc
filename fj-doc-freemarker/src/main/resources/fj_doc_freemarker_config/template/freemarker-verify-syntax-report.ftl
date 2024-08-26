<?xml version="1.0" encoding="utf-8"?>
<doc
        xmlns="http://javacoredoc.fugerit.org"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" >

    <#--
        This is a Venus Fugerit Doc (https://github.com/fugerit-org/fj-doc) FreeMarker Template XML (ftl[x]).
        For consideration of Venus Fugerit Doc and Apache FreeMarker integration see :
        https://venusguides.fugerit.org/src/docs/common/doc_format_freemarker.html
        The result will be a :
    -->
    <!--
        This is a Venus Fugerit Doc (https://github.com/fugerit-org/fj-doc) XML Source Document.
        For documentation on how to write a valid Venus Doc XML Meta Model refer to :
        https://venusguides.fugerit.org/src/docs/common/doc_format_summary.html
    -->

    <#assign defaultTitle="FreeMarker syntax verifier report">

    <metadata>
        <!-- Margin for document : left;right;top;bottom -->
        <info name="margins">10;10;10;30</info>
        <!-- documenta meta information -->
        <info name="doc-title">${docTitle!defaultTitle}</info>
        <info name="doc-subject">Output of the tool FreeMarker syntax verifier</info>
        <info name="doc-author">fugerit79</info>
        <info name="doc-language">en</info>
        <!-- property specific for xls/xlsx -->
        <info name="excel-table-id">data-table=print</info>
        <!-- property specific for csv -->
        <info name="csv-table-id">data-table</info>
        <footer-ext>
            <para align="right">${r"${currentPage}"} / ${r"${pageCount}"}</para>
        </footer-ext>
    </metadata>
    <body>
    <para head-level="1">${docTitle!defaultTitle} - ${output.creationTime?datetime?iso_utc}</para>
    <#if output?? && output.infos?? && output.infos?size != 0 >
        <para>Total number of templates verified : ${output.infos?size}</para>
        <para>Total number of templates with syntax errors : ${output.errors?size}</para>
        <#if output.errors?size != 0 ><para>Templates with syntax errors :<#list output.errorsTemplateIds as current> <phrase link="#${current}">${current}</phrase></#list></para></#if>
        <table columns="3" colwidths="30;20;50"  width="100" id="data-table" padding="2">
            <row header="true">
                <cell align="center"><para>Template</para></cell>
                <cell align="center"><para>Verify result</para></cell>
                <cell align="center"><para>Note</para></cell>
            </row>
            <#list output.infos as current>
                <#if current.resultCode == 0 ><#assign backcolor="#99ff99"/><#else><#assign backcolor="#ff9999"/></#if>
                <row>
                    <cell back-color="${backcolor}"><phrase anchor="${current.templateId}"></phrase><para>${current.templateId}</para></cell>
                    <cell back-color="${backcolor}"><para><#if current.resultCode == 0 >OK<#else>Fail</#if> (${current.resultCode})</para></cell>
                    <cell back-color="${backcolor}"><para><#if current.exception??><![CDATA[${current.exception.message}]]></#if></para></cell>
                </row>
            </#list>
        </table>
    <#else>
        <para>No data found</para>
    </#if>
    </body>

</doc>