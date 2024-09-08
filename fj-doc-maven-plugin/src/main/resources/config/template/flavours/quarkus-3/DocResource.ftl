<#import '../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

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


    @GET
    @Produces("text/markdown")
    @Path("/example.md")
    public byte[] markdownExample() {
        return processDocument(DocConfig.TYPE_MD);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/example.html")
    public byte[] htmlExample() {
        return processDocument(DocConfig.TYPE_HTML);
    }

    <#if context.modules?seq_contains("fj-doc-mod-fop")>
    @GET
    @Produces("application/pdf")
    @Path("/example.pdf")
    public byte[] pdfExample() {
        return processDocument(DocConfig.TYPE_PDF);
    }
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-poi")>
    @GET
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Path("/example.xlsx")
    public byte[] xslxExample() {
        return processDocument(DocConfig.TYPE_XLSX);
    }
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-opencsv")>
    @GET
    @Produces("text/csv")
    @Path("/example.csv")
    public byte[] csvExample() {
        return processDocument(DocConfig.TYPE_CSV);
    }
    </#if>

}
