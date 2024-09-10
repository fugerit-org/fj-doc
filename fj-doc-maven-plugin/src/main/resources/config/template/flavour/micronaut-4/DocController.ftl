<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import io.micronaut.http.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("/doc")
public class DocController {

    <@fhm.createDocumentProcess context=context exceptionType='ConfigRuntimeException'/>

    <@fhm.createMicronautPath context=context outputMime="text/markdown" outputExtension="md" outputDescription="Markdown"/>
    
    <@fhm.createMicronautPath context=context outputMime="text/html" outputExtension="html" outputDescription="HTML"/>
    
    <#if context.modules?seq_contains("fj-doc-mod-fop")>
    <@fhm.createMicronautPath context=context outputMime="application/pdf" outputExtension="pdf" outputDescription="PDF"/>
    </#if>
    
    <#if context.modules?seq_contains("fj-doc-mod-poi")>
    <@fhm.createMicronautPath context=context outputMime="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" outputExtension="xlsx" outputDescription="Excel"/>
    </#if>
    
    <#if context.modules?seq_contains("fj-doc-mod-opencsv")>
    <@fhm.createMicronautPath context=context outputMime="text/csv" outputExtension="csv" outputDescription="CSV"/>
    </#if>

}