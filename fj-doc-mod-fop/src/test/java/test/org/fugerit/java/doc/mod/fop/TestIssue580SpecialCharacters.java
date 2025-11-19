package test.org.fugerit.java.doc.mod.fop;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
class TestIssue580SpecialCharacters {

    private static final String PATH_XML_SAMPLE = "issue/gh580/sample-gh580.xml";

    @Test
    void testGeneratedPdf() throws Exception {
        DocTypeHandlerDefault handler = new PdfFopTypeHandler(StandardCharsets.UTF_8);
        String xmlConfig = "<docHandlerCustomConfig charset=\"UTF-8\" " +
                "fop-config-mode=\"classloader\" " +
                "fop-config-classloader-path=\"issue/gh580/fop-config-issue-580.xml\" />\n";
        Element element = DOMIO.loadDOMDoc( xmlConfig ).getDocumentElement();
        handler.configure( element );

        log.info( "handler: {}, type: {}", handler.getClass().getName(), handler.getType() );
        File outputFile = new File( "target/issue-gh580.pdf" );
        log.info( "output file: {}, delete : {}", outputFile.getAbsolutePath(), outputFile.delete() );
        try (Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(PATH_XML_SAMPLE) );
             OutputStream os = new FileOutputStream( outputFile ) ) {
            handler.handle( DocInput.newInput( handler.getType(), reader ), DocOutput.newOutput( os ) );
        }
        Assertions.assertTrue( outputFile.exists() ) ;
    }

}
