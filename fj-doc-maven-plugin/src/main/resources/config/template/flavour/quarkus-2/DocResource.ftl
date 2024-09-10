<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

@Slf4j
@Path("/doc")
public class DocResource {

    <@fhm.createDocumentProcess context=context exceptionType='WebApplicationException'/>

    <@fhm.createQuarkusPath context=context outputMime="text/markdown" outputExtension="md" outputDescription="Markdown"/>

    <@fhm.createQuarkusPath context=context outputMime="text/html" outputExtension="html" outputDescription="HTML"/>

    <#if context.modules?seq_contains("fj-doc-mod-fop")>
    <@fhm.createQuarkusPath context=context outputMime="application/pdf" outputExtension="pdf" outputDescription="PDF"/>
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-poi")>
    <@fhm.createQuarkusPath context=context outputMime="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" outputExtension="xlsx" outputDescription="Excel"/>
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-opencsv")>
    <@fhm.createQuarkusPath context=context outputMime="text/csv" outputExtension="csv" outputDescription="CSV"/>
    </#if>

}
