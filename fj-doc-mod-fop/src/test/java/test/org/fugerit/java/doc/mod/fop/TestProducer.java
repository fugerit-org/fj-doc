package test.org.fugerit.java.doc.mod.fop;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandlerUTF8;
import org.fugerit.java.doc.mod.fop.InitFopHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.org.fugerit.java.BasicTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

@Slf4j
class TestProducer extends BasicTest {

    private static final String TEST_CREATOR = "My Creator";
    private static final String TEST_PRODUCER = "My Producer";

	@Test
	void testProducer() throws Exception {
		DocTypeHandler handler = PdfFopTypeHandler.HANDLER;
		String fileName = "doc_producer";
		File outputFile = new File( String.format( "target/%s.%s", fileName, handler.getType() ) );
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(  String.format( "sample/%s.xml", fileName ) ) );
			  FileOutputStream fos = new FileOutputStream( outputFile ) ) {
			handler.handle( DocInput.newInput( handler.getType(), reader ) , DocOutput.newOutput( fos ) );
			log.info( "file {}", outputFile.getCanonicalFile() );
		}
        try (PDDocument document = PDDocument.load(outputFile)) {
            PDDocumentInformation info = document.getDocumentInformation();
            String producer = info.getProducer();
            String creator = info.getCreator();
            log.info( "producer : {}, creator : {}", producer, creator );
            Assertions.assertEquals( TEST_PRODUCER, producer );
            Assertions.assertEquals( TEST_CREATOR, creator );
        }
	}
	
}
