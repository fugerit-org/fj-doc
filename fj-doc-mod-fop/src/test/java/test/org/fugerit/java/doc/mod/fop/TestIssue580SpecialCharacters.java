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

    private static final String PATH_XML_SAMPLE_ALT_FONT = "issue/gh580/sample-gh580-alt-font.xml";

    private File testWorker( DocTypeHandler handler, String xmlPath, String outputPath ) throws Exception {
        log.info( "xml path : {}, handler: {}, type: {}", xmlPath, handler.getClass().getName(), handler.getType() );
        File outputFile = new File( outputPath );
        log.info( "output file: {}, delete : {}", outputFile.getAbsolutePath(), outputFile.delete() );
        try (Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(xmlPath) );
             OutputStream os = new FileOutputStream( outputFile ) ) {
            handler.handle( DocInput.newInput( handler.getType(), reader ), DocOutput.newOutput( os ) );
        }
        return outputFile;
    }

    /*
     * working example using SVG :
     * <svg xmlns="http://www.w3.org/2000/svg" fill="#000000" width="10px" height="10px" viewBox="0 0 24 24"><path d="M21,2H3A1,1,0,0,0,2,3V21a1,1,0,0,0,1,1H21a1,1,0,0,0,1-1V3A1,1,0,0,0,21,2ZM20,20H4V4H20Z"/></svg>
     */
    @Test
    void testGeneratedPdf() throws Exception {
        DocTypeHandler handler = PdfFopTypeHandler.HANDLER_UTF8;
        File outputFile = this.testWorker( handler, PATH_XML_SAMPLE, "target/issue-gh580.pdf" );
        Assertions.assertTrue( outputFile.exists() ) ;
    }

    /*
     * working example using DejaVu Font https://dejavu-fonts.github.io/
     */
    @Test
    void testGeneratedPdfWithAlternateFont() throws Exception {
        DocTypeHandlerDefault handler = new PdfFopTypeHandler(StandardCharsets.UTF_8);
        String xmlConfig = "<config><docHandlerCustomConfig charset=\"UTF-8\" " +
                "fop-config-mode=\"classloader\" " +
                "fop-config-classloader-path=\"issue/gh580/fop-config-issue-580.xml\" /></config>\n";
        Element element = DOMIO.loadDOMDoc( xmlConfig ).getDocumentElement();
        handler.configure( element );
        File outputFile = this.testWorker( handler, PATH_XML_SAMPLE_ALT_FONT, "target/issue-gh580-alt-font.pdf" );
        Assertions.assertTrue( outputFile.exists() ) ;
    }

}
