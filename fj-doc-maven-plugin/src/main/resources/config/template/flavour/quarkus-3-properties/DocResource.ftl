<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Slf4j
@ApplicationScoped
@Path("/doc")
public class DocResource {

    DocHelper docHelper;

    public DocResource(DocHelper docHelper) {
        this.docHelper = docHelper;
    }

    <@fhm.createDocumentProcessNoHelper context=context exceptionType='WebApplicationException'/>

    <@fhm.createQuarkusPath context=context outputMime="text/markdown" outputExtension="md" outputDescription="Markdown"/>

    <@fhm.createQuarkusPath context=context outputMime="text/html" outputExtension="html" outputDescription="HTML"/>

    <#if context.asciidocFreemarkerHandlerAvailable>
    <@fhm.createQuarkusPath context=context outputMime="text/asciidoc" outputExtension="adoc" outputDescription="AsciiDoc"/>
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-fop")>
    <@fhm.createQuarkusPath context=context outputMime="application/pdf" outputExtension="pdf" outputDescription="PDF"/>
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-poi")>
    <@fhm.createQuarkusPath context=context outputMime="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" outputExtension="xlsx" outputDescription="Excel"/>
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-opencsv")>
    <@fhm.createQuarkusPath context=context outputMime="text/csv" outputExtension="csv" outputDescription="CSV"/>
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-openpdf-ext")>
    <@fhm.createQuarkusPathPrefix context=context outputMime="application/pdf" outputExtension="pdf" outputDescription="OpenPDF" pathPrefix='/openpdf'/>
    <@fhm.createQuarkusPathPrefix context=context outputMime="text/html" outputExtension="html" outputDescription="OpenPDFHTML" pathPrefix='/openpdf'/>
    </#if>
    <#if context.modules?seq_contains("fj-doc-mod-openrtf-ext")>
    <@fhm.createQuarkusPath context=context outputMime="application/rtf" outputExtension="rtf" outputDescription="RTF"/>
    </#if>

}
