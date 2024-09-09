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

    byte[] processDocument(String handlerId) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // creates the doc helper
            DocHelper docHelper = new DocHelper();
            // create custom data for the fremarker template 'document.ftl'
            List<People> listPeople = Arrays.asList(new People("Luthien", "Tinuviel", "Queen"), new People("Thorin", "Oakshield", "King"));
            // output generation
            docHelper.getDocProcessConfig().fullProcess("document", DocProcessContext.newContext("listPeople", listPeople), handlerId, baos);
            // return the output
            return baos.toByteArray();
        } catch (Exception e) {
            log.error(String.format("Error processing %s, error:%s", handlerId, e), e);
            throw new WebApplicationException(e);
        }
    }


    @APIResponse(responseCode = "200", description = "The Markdown document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "markdown" ) } )
    @Operation( operationId = "markdownExample", summary =  "Generated an example Markdown document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/markdown")
    @Path("/example.md")
    public byte[] markdownExample() {
        return processDocument(DocConfig.TYPE_MD);
    }

    @APIResponse(responseCode = "200", description = "The HTML document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "html" ) } )
    @Operation( operationId = "htmlExample", summary =  "Generated an example HTML document using Fugerit Venus Doc handler" )
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/example.html")
    public byte[] htmlExample() {
        return processDocument(DocConfig.TYPE_HTML);
    }

    <#if context.modules?seq_contains("fj-doc-mod-fop")>
    @APIResponse(responseCode = "200", description = "The PDF document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "pdf" ) } )
    @Operation( operationId = "pdfExample", summary =  "Generated an example PDF document using Fugerit Venus Doc handler" )
    @GET
    @Produces("application/pdf")
    @Path("/example.pdf")
    public byte[] pdfExample() {
        return processDocument(DocConfig.TYPE_PDF);
    }
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-poi")>
    @APIResponse(responseCode = "200", description = "The Excel document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "excel" ) } )
    @Operation( operationId = "xlsxExample", summary =  "Generated an example Excel document using Fugerit Venus Doc handler" )
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Path("/example.xlsx")
    public byte[] xlsxExample() {
        return processDocument(DocConfig.TYPE_XLSX);
    }
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-opencsv")>
    @APIResponse(responseCode = "200", description = "The CSV document content" )
    @APIResponse(responseCode = "500", description = "In case of an unexpected error" )
    @Tags( { @Tag( name = "document" ), @Tag( name = "csv" ) } )
    @Operation( operationId = "csvExample", summary =  "Generated an example CSV document using Fugerit Venus Doc handler" )
    @GET
    @Produces("text/csv")
    @Path("/example.csv")
    public byte[] csvExample() {
        return processDocument(DocConfig.TYPE_CSV);
    }
    </#if>

}
