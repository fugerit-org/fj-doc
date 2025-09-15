package test.org.fugerit.java.doc.sample.spec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

@Slf4j
class TestJsonInput {

    // uses
    // template document-01 (configured for fj-doc-sample/src/main/resources/free_marker/document-01.ftl)
    // data model based on JSON : fj-doc-sample/src/test/resources/spec/test-json-input.json
    //
    // to convert json string in a freemarker data model use (as seen in document-01.ftl) :
    //        <#assign data = data?eval>
    @Test
    void testJsonInput() throws IOException {
        FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://config/freemarker-doc-process.xml" );
        String chainId = "document-01";
        String attName = "data";
        String dataMap = FileIO.readString( "src/test/resources/spec/test-json-input.json" );
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            config.fullProcess( chainId, DocProcessContext.newContext( attName, dataMap ), DocConfig.TYPE_HTML, baos );
            log.info( "html output : {}", baos.toString() );
        }
        File outputPdf = new File( "target/test-json-input.pdf" );
        outputPdf.delete();
        Assertions.assertFalse( outputPdf.exists() );
        try ( FileOutputStream fos = new FileOutputStream( outputPdf ) ) {
            config.fullProcess( chainId, DocProcessContext.newContext( attName, dataMap ), DocConfig.TYPE_PDF, fos );
        }
        Assertions.assertTrue( outputPdf.exists() );
    }

}
