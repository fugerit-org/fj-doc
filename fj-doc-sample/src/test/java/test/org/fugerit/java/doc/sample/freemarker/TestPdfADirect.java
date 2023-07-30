package test.org.fugerit.java.doc.sample.freemarker;


import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

@Slf4j
public class TestPdfADirect {

	private void testWorker( String handlerRef ) {
		FreemarkerDocProcessConfig config = BasicFacadeTest.getPROCESSCONFIG();
		DocTypeHandler handler = config.getFacade().findHandler( handlerRef );
		log.info( "handler : {}, type : {}, format : {}", handler, handler.getType(), handler.getFormat() );
		DocProcessContext context = DocProcessContext.newContext();
		DocProcessData data = new DocProcessData();
		String chainId = "pdf_a_test";
		try ( OutputStream os = new FileOutputStream( new File( "target/", "direct1_"+chainId+"."+handler.getType() ) ) ) {
			config.process( chainId, context, data, handler, DocOutput.newOutput(os) );	
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail( message );
		}	
	}
	
	@Test
	public void testPDFADirectFormat() {
		this.testWorker( DocConfig.FORMAT_PDF_A_1A );	
	}
	
	@Test
	public void testPDFADirectId() {
		this.testWorker( "pdfa-fop" );	
	}
	
	@Test
	public void testPDFDirectType() {
		this.testWorker( DocConfig.TYPE_PDF );	
	}

}
