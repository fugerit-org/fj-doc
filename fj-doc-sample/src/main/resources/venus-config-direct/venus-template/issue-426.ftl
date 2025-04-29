<#ftl output_format="XML"><#-- Set the output format for XML, not mandatory but reccomended, otherwise escaping should be handled in a cusomt way, for example through CDATA sections -->
<?xml version="1.0" encoding="utf-8"?>
<doc
        xmlns="http://javacoredoc.fugerit.org"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" >

    <#--
        This is a Venus Fugerit Doc (https://github.com/fugerit-org/fj-doc) FreeMarker Template XML (ftl[x]).
        For consideration of Venus Fugerit Doc and Apache FreeMarker integration see :
        https://venusdocs.fugerit.org/guide/#doc-freemarker-entry-point
        The result will be a :
    -->
    <!--
        This is a Venus Fugerit Doc (https://github.com/fugerit-org/fj-doc) XML Source Document.
        For documentation on how to write a valid Venus Doc XML Meta Model refer to :
        https://venusdocs.fugerit.org/guide/#doc-format-entry-point
    -->

    <metadata>
        <!-- Margin for document : left;right;top;bottom -->
        <info name="margins">10;10;10;30</info>
        <!-- documenta meta information -->
        <info name="doc-title">${dataModel.docTitle}</info>
        <info name="doc-author">Matteo Franci</info>
        <info name="doc-language">it</info>
        <!-- only apply to xls/xlsx format -->
        <info name="excel-table-id">issue-426-table=issue-426-table</info>
        <!-- only apply to csv format -->
        <info name="csv-table-id">issue-426-table</info>
        <!-- font must be loaded -->
        <info name="default-font-name">TitilliumWeb</info>
        <!-- default table-border-collapse, can be 'separate' or 'collapse' -->
        <info name="table-border-collapse">collapse</info>
        <footer-ext>
            <para align="right">${r"${currentPage}"} / ${r"${pageCount}"}</para>
        </footer-ext>
        <bookmark-tree>
            <bookmark ref="top">${dataModel.docTitle}</bookmark>
        </bookmark-tree>
    </metadata>

    <body>

    <h head-level="1" id="top" size="16">${dataModel.docTitle}</h>

    <table colwidths="50;50" columns="2" width="100" id="issue-426-table">
        <row header="true">
            <cell align="center"><para style="bold">Field 1</para></cell>
            <cell align="center"><para style="bold">Field 2</para></cell>
        </row>
        <#list dataModel.listSample as current>
            <row>
                <cell><phrase>${current.field1}</phrase></cell>
                <cell><phrase>${current.field2}</phrase></cell>
            </row>
        </#list>
    </table>

    </body>

</doc>
