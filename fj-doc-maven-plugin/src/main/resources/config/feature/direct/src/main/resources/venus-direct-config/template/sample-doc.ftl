<#ftl output_format="XML">
<?xml version="1.0" encoding="utf-8"?>
<doc
        xmlns="http://javacoredoc.fugerit.org"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd" >

    <#--
        Sample template, see documentation at https://venusdocs.fugerit.org/
     -->

    <meta>

    <!-- Margin for document : left;right;top;bottom -->
    <info name="margins">10;10;10;30</info>

    <!-- id table to be used for xlsx output -->
    <info name="excel-table-id">excel-table=print</info>
    <info name="excel-width-multiplier">450</info>
    <!-- id table to be used for xsv output -->
    <info name="csv-table-id">excel-table</info>

    <!-- you need to escape free marker expression for currentPage -->
    <footer-ext>
        <para align="right">${r"${currentPage}"} / ${r"${pageCount}"}</para>
    </footer-ext>

    </meta>

    <body>
    <h head-level="1" >${dataModel.docTitle}</h>
    <table columns="3" colwidths="30;30;40"  width="100" id="excel-table" padding="2">
        <row>
            <cell align="center"><para style="bold">Name</para></cell>
            <cell align="center"><para style="bold">Surname</para></cell>
            <cell align="center"><para style="bold">Title</para></cell>
        </row>
        <#list dataModel.listPeople as current>
            <row>
                <cell><para>${current.name}</para></cell>
                <cell><para>${current.surname}</para></cell>
                <cell><para>${current.title}</para></cell>
            </row>
        </#list>
    </table>
    </body>

</doc>