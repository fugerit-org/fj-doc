package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
class PdfBoxTypeHandlerTest {

    @Test
    void test() {
        boolean res = this.testDocWorker( "default_doc", DocConfig.TYPE_PDF);
        Assertions.assertTrue( res );
    }

    protected boolean testDocWorker( String testCase, String type ) {
        boolean ok = false;
        String inputXml = "xml/"+testCase+".xml" ;
        DocTypeHandler handler = new PdfBoxTypeHandler();
        File outputFile = new File( "target", testCase+"."+handler.getType() );
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
