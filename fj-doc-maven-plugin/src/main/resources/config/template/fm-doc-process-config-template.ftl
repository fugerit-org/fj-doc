<?xml version="1.0" encoding="utf-8"?>
<freemarker-doc-process-config
        xmlns="https://freemarkerdocprocess.fugerit.org"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="https://freemarkerdocprocess.fugerit.org https://www.fugerit.org/data/java/doc/xsd/freemarker-doc-process-1-0.xsd" >

    <#-- configuration version 001 -->
    <!--
    Documentation :
    https://venusguides.fugerit.org/

    Configuration reference :
    https://venusdocs.fugerit.org/fj-doc-freemarker/src/main/docs/fdp_xsd_config_ref.html
    -->

    <docHandlerConfig registerById="true">
        <!-- Type handler for markdown format -->
        <docHandler id="md-ext" info="md" type="org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler" />
        <!-- Type henalder for xml format, generates the source xml:doc -->
        <docHandler id="xml-doc" info="xml" type="org.fugerit.java.doc.base.config.DocTypeHandlerXMLUTF8" />
        <!-- Type handler for html using freemarker -->
        <docHandler id="html-fm" info="html" type="org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerEscapeUTF8" />
        <!-- Type handler for html using freemarker (fragment version, only generates body content no html or head part -->
        <docHandler id="html-fragment-fm" info="fhtml" type="org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8" />
        <#if context.modules?seq_contains("fj-doc-mod-fop")>
        <!-- Type handler generating xls:fo style sheet -->
        <docHandler id="fo-fop" info="fo" type="org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandlerUTF8" />
        <!-- Type handler generating pdf -->
        <docHandler id="pdf-fop" info="pdf" type="org.fugerit.java.doc.mod.fop.PdfFopTypeHandler">
            <docHandlerCustomConfig charset="UTF-8" fop-config-mode="classloader" fop-config-classloader-path="${context.artificatIdForFolder}/fop-config.xml" fop-suppress-events="1"/>
        </docHandler>
        </#if>
        <#if context.modules?seq_contains("fj-doc-mod-poi")>
        <!-- Type handler generating xls -->
        <docHandler id="xls-poi" info="xls" type="org.fugerit.java.doc.mod.poi.XlsPoiTypeHandler" />
        <!-- Type handler generating xlsx -->
        <docHandler id="xlsx-poi" info="xlsx" type="org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler" />
        </#if>
        <#if context.modules?seq_contains("fj-doc-mod-opencsv")>
        <!-- Type handler generating csv -->
        <docHandler id="csv-opencsv" info="csv" type="org.fugerit.java.doc.mod.opencsv.OpenCSVTypeHandler"/>
        </#if>
    </docHandlerConfig>

    <docChain id="shared">
        <chainStep stepType="config">
            <config
                    id="fj_doc_config_fm_${context.artificatIdForName}"
                    class="${context.docConfigPackage}.${context.docConfigClass}"
                    exception-handler="RETHROW_HANDLER"
                    fallback-on-null-loop-variable="false"
                    log-exception="false"
                    mode="class"
                    path="/${context.artificatIdForFolder}/${context.templateSubPath}/"
                    version="${context.freemarkerVersion}"
                    wrap-unchecked-exceptions="true"
                    load-bundled-functions="true"
            />
        </chainStep>
    </docChain>

    <!-- example document chain -->
    <docChain id="document" parent="shared">
        <chainStep stepType="complex" map-atts="listPeople" template-path="${r"${chainId}"}.ftl"/>
    </docChain>

</freemarker-doc-process-config>