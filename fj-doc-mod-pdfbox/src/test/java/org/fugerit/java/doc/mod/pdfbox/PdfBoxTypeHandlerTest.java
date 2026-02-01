package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.typehandler.core.DocTypeHandlerCoreXMLUTF8;
import org.fugerit.java.doc.mod.openpdf.ext.PdfTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
class PdfBoxTypeHandlerTest {

    @Test
    void testPdfBox() {
        boolean res = this.testDocWorker( new PdfBoxTypeHandler(), "default_doc", "pdfbox");
        Assertions.assertTrue( res );
    }

    @Test
    void testOpenPDF() {
        boolean res = this.testDocWorker( new PdfTypeHandler(), "default_doc", "openpdf");
        Assertions.assertTrue( res );
    }

    @Test
    void testXML() {
        boolean res = this.testDocWorker( new DocTypeHandlerCoreXMLUTF8(), "default_doc", "openpdf");
        Assertions.assertTrue( res );
    }

    @Test
    void testPdfBoxA() throws ConfigException {
        Properties props = new Properties();
        props.setProperty( PdfBoxConfig.PDFA_ENABLED, "true" );
        PdfBoxTypeHandler handler = new PdfBoxTypeHandler();
        handler.configure( props );
        boolean res = this.testDocWorker( handler , "default_doc", "pdfbox-a-ua");
        Assertions.assertTrue( res );
    }

    @Test
    void testPdfBoxUA() throws ConfigException {
        Properties props = new Properties();
        props.setProperty( PdfBoxConfig.PDFUA_ENABLED, "true" );
        PdfBoxTypeHandler handler = new PdfBoxTypeHandler();
        handler.configure( props );
        boolean res = this.testDocWorker( handler , "default_doc", "pdfbox-a-ua");
        Assertions.assertTrue( res );
    }

    protected boolean testDocWorker(  DocTypeHandler handler, String testCase, String append ) {
        boolean ok = false;
        String inputXml = "xml/"+testCase+".xml" ;
        File outputFile = new File( "target", testCase+"_"+append+"."+handler.getType() );
        log.info( "inputXml:{}, outputFile:{}", inputXml, outputFile );
        try (InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( inputXml ) );
             OutputStream os = Files.newOutputStream(outputFile.toPath())) {
            handler.handle( DocInput.newInput( handler.getType() , reader ) , DocOutput.newOutput(os) );
            ok = true;
        } catch (Exception e) {
            String message = "Error : "+e.getMessage();
            log.error( message , e );
            fail( message );
        }
        return ok;
    }

}
