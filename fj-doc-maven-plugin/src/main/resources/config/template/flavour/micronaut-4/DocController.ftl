<#import
'../flavour-macro.ftl' as fhm>
package <@fhm.toProjectPackage context=context/>;

import io.micronaut.http.annotation.*;

import org.fugerit.java.core.cfg.ConfigRuntimeException;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller("/doc")
public class DocController {

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
            throw new ConfigRuntimeException(e);
        }
    }

    @Get(uri="/example.md", produces="text/markdown")
    public byte[] markdownExample() {
        return processDocument(DocConfig.TYPE_MD);
    }

    @Get(uri="/example.html", produces="text/html")
    public byte[] markdownHtml() {
        return processDocument(DocConfig.TYPE_HTML);
    }

    <#if context.modules?seq_contains("fj-doc-mod-fop")>
    @Get(uri="/example.pdf", produces="application/pdf")
    public byte[] pdfExample() {
        return processDocument(DocConfig.TYPE_PDF);
    }
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-poi")>
    @Get(uri="/example.xlsx", produces="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public byte[] xlsxExample() {
        return processDocument(DocConfig.TYPE_XLSX);
    }
    </#if>

    <#if context.modules?seq_contains("fj-doc-mod-opencsv")>
    @Get(uri="/example.csv", produces="text/csv")
    public byte[] csvExample() {
        return processDocument(DocConfig.TYPE_CSV);
    }
    </#if>

}