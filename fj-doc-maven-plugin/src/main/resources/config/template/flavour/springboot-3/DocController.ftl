<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/doc")
@Slf4j
public class DocController {

    <@fhm.createDocumentProcess context=context exceptionType='ConfigRuntimeException'/>

    <@fhm.createSpringBootPath context=context outputMime="text/markdown" outputExtension="md" outputDescription="Markdown"/>
    
    <@fhm.createSpringBootPath context=context outputMime="text/html" outputExtension="html" outputDescription="HTML"/>

    <#if context.asciidocFreemarkerHandlerAvailable>
    <@fhm.createSpringBootPath context=context outputMime="text/asciidoc" outputExtension="adoc" outputDescription="AsciiDoc"/>
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-fop")>
    <@fhm.createSpringBootPath context=context outputMime="application/pdf" outputExtension="pdf" outputDescription="PDF"/>
    </#if>
    
    <#if context.modules?seq_contains("fj-doc-mod-poi")>
    <@fhm.createSpringBootPath context=context outputMime="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" outputExtension="xlsx" outputDescription="Excel"/>
    </#if>
    
    <#if context.modules?seq_contains("fj-doc-mod-opencsv")>
    <@fhm.createSpringBootPath context=context outputMime="text/csv" outputExtension="csv" outputDescription="CSV"/>
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-openpdf-ext")>
    <@fhm.createSpringBootPathPrefix context=context outputMime="application/pdf" outputExtension="pdf" outputDescription="OpenPDF" pathPrefix='/openpdf'/>
    <@fhm.createSpringBootPathPrefix context=context outputMime="text/html" outputExtension="html" outputDescription="OpenPDFHTML" pathPrefix='/openpdf'/>
    </#if>
    <#if context.modules?seq_contains("fj-doc-mod-openrtf-ext")>
    <@fhm.createSpringBootPath context=context outputMime="application/rtf" outputExtension="rtf" outputDescription="RTF"/>
    </#if>

}