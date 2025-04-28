---
configId: 'venus-direct-config-sample'
templatePath: '${r"${projectBasedir}"}/src/main/resources/venus-direct-config/template/'
templateMode: 'folder'
createParentDirectory: true
handlerList:
  - type: org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerUTF8
  - type: org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandlerNoCommentsUTF8
  - type: org.fugerit.java.doc.freemarker.asciidoc.FreeMarkerAsciidocTypeHandlerUTF8
  - type: org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8<#if context.modules?seq_contains("fj-doc-mod-fop")>
  - type: org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandlerUTF8
  - type: org.fugerit.java.doc.mod.fop.PdfFopTypeHandler</#if><#if context.modules?seq_contains("fj-doc-mod-poi")>
  - type: org.fugerit.java.doc.mod.poi.XlsPoiTypeHandler
  - type: org.fugerit.java.doc.mod.poi.XlsxPoiTypeHandler</#if><#if context.modules?seq_contains("fj-doc-mod-opencsv")>
  - type: org.fugerit.java.doc.mod.opencsv.OpenCSVTypeHandler</#if><#if context.modules?seq_contains("fj-doc-mod-openpdf-ext")>
  - type: org.fugerit.java.doc.mod.openpdf.ext.PdfTypeHandler
  - type: org.fugerit.java.doc.mod.openpdf.ext.HtmlTypeHandler</#if><#if context.modules?seq_contains("fj-doc-mod-openrtf-ext")>
  - type: org.fugerit.java.doc.mod.openrtf.ext.RtfTypeHandler</#if>
chainList:  # a template named ${r"${chainId}"}.ftl or ${r"${useChainId}"}.ftl must exist in 'templatePath' folder
  - chainId: 'sample-doc'
    dataModel:  # inline data model definition
      docTitle: 'Venus Direct Extension - Test Doc'
      listPeople:
        - name: "Luthien"
          surname: "Tinuviel"
          title: "Queen"
        - name: "Thorin"
          surname: "Oakshield"
          title: "King"
  - chainId: 'sample-doc-json-data-model'
    useChainId: 'sample-doc'
    dataModelJson: '${r"${projectBasedir}"}/src/main/resources/venus-direct-config/data-model/sample-model.json'  # JSON file data model
  - chainId: 'sample-doc-yaml-data-model'
    useChainId: 'sample-doc'
    dataModelYaml: '${r"${projectBasedir}"}/src/main/resources/venus-direct-config/data-model/sample-model.yaml'  # YAML file data model
outputList:
  - outputId: 'sample-doc-html'
    chainId: 'sample-doc'
    handlerId: 'html' # a valid handler for this output type should be defined (i.e. org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerUTF8)
    file: '${r"${projectBasedir}"}/target/sample-doc.html'
  - outputId: 'sample-doc-md'
    chainId: 'sample-doc-json-data-model'
    handlerId: 'md'
    file: '${r"${projectBasedir}"}/target/sample-doc.md'
  - outputId: 'sample-doc-adoc'
    chainId: 'sample-doc-yaml-data-model'
    handlerId: 'adoc'
    file: '${r"${projectBasedir}"}/target/sample-doc.adoc'
<#if context.modules?seq_contains("fj-doc-mod-fop")>  - outputId: 'sample-doc-pdf'
    chainId: 'sample-doc'
    handlerId: 'pdf-fop'  # of simply use 'pdf' if it is the only pdf handler
    file: '${r"${projectBasedir}"}/target/sample-doc.pdf'</#if>
<#if context.modules?seq_contains("fj-doc-mod-poi")>  - outputId: 'sample-doc-xlsx'
    chainId: 'sample-doc'
    handlerId: 'xlsx-poi' # of simply use 'xlsx' if it is the only excel handler
    file: '${r"${projectBasedir}"}/target/sample-doc.xlsx'</#if>
<#if context.modules?seq_contains("fj-doc-mod-opencsv")>  - outputId: 'sample-doc-csv'
    chainId: 'sample-doc'
    handlerId: 'csv'
    file: '${r"${projectBasedir}"}/target/sample-doc.csv'</#if>
<#if context.modules?seq_contains("fj-doc-mod-openpdf-ext")>  - outputId: 'sample-doc-openpdf-ext'
    chainId: 'sample-doc'
    handlerId: 'pdf-openpdf-ext'
    file: '${r"${projectBasedir}"}/target/sample-doc-openpdf-ext.pdf'</#if>
<#if context.modules?seq_contains("fj-doc-mod-openrtf-ext")>  - outputId: 'sample-doc-openrtf-ext'
    chainId: 'sample-doc'
    handlerId: 'rtf-openrtf-ext'
    file: '${r"${projectBasedir}"}/target/sample-doc-openrtf-ext.rtf'</#if>