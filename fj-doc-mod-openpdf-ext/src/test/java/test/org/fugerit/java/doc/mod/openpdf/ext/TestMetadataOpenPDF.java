package test.org.fugerit.java.doc.mod.openpdf.ext;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.openpdf.ext.PdfTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

@Slf4j
class TestMetadataOpenPDF {

    private static final String TEST_TITLE = "Module OpenPDF Metadata Test";
    private static final String TEST_SUBJECT = "Simple document to test PDF metadata";
    private static final String TEST_AUTHOR = "fugerit79";
    private static final String TEST_CREATOR = "My Creator";
    private static final String TEST_PRODUCER = "My Producer";

	@Test
	void testProducer() throws Exception {
		DocTypeHandler handler = PdfTypeHandler.HANDLER;
		String fileName = "doc_producer";
		File outputFile = new File( String.format( "target/%s.%s", fileName, handler.getType() ) );
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader(  String.format( "xml/%s.xml", fileName ) ) );
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
            String title = info.getTitle();
            String subject = info.getSubject();
            log.info( "title : {}, subject : {}", title, subject );
            Assertions.assertEquals( TEST_TITLE, title );
            Assertions.assertEquals( TEST_SUBJECT, subject );
            String author = info.getAuthor();
            String language = document.getDocumentCatalog().getLanguage();
            log.info( "author : {}, language : {}", author, language );
            Assertions.assertEquals( TEST_AUTHOR, author );
        }
	}
	
}
