<#outputformat "XML"><#-- automatically escape variables as XML : https://freemarker.apache.org/docs/dgui_misc_autoescaping.html -->

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
            <info name="doc-title">Simple json properties document</info>
            <info name="doc-author">fugerit79</info>
            <info name="doc-language">en</info>
            <!-- font must be loaded -->
            <info name="default-font-name">TitilliumWeb</info>
            <!-- property specific for xls/xlsx -->
            <info name="excel-table-id">data-table-1=properties;data-table-2=duplicates</info>
            <info name="excel-try-autoresize">true</info>

            <footer-ext>
                <para align="right">${r"${currentPage}"} / ${r"${pageCount}"}</para>
            </footer-ext>
        </metadata>
        <body>
        <!-- props table -->
        <table columns="4" colwidths="20;20;20;40"  width="100" id="data-table-1" padding="2">
            <row header="true">
                <cell align="center"><para>Key</para></cell>
                <cell align="center"><para>Value</para></cell>
                <cell align="center"><para>Note</para></cell>
                <cell align="center"><para>Description</para></cell>
            </row>
            <#if keys??>
                <#list keys as current>
                    <row>
                        <cell><para>${current.key}</para></cell>
                        <cell><para></para></cell>
                        <cell><para></para></cell>
                        <cell><para>${current.value}</para></cell>
                    </row>
                </#list>
            </#if>
        </table>
        <!-- duplicated values -->
        <table columns="2" colwidths="50;50"  width="100" id="data-table-2" padding="2">
            <row header="true">
                <cell align="center"><para>Duplicated keys</para></cell>
                <cell align="center"><para>Key</para></cell>
            </row>
            <#if duplications??>
                <#list duplications as current>
                    <row>
                        <cell><para>${current.key}</para></cell>
                        <cell><para>${current.value}</para></cell>
                    </row>
                </#list>
            </#if>
        </table>
        </body>

    </doc>
</#outputformat>