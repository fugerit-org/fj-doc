package test.org.fugerit.java.doc.sample.freemarker;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

public class TestPdfAUA {

	private static final String PDF_A_UA_FOP = "pdf-a-ua-fop";

	private static FreemarkerDocProcessConfig config =
			FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://config/freemarker-doc-process-pdf-a-ua.xml" );

	@Test
	public void testPDFAUA() throws Exception {
		File file = new File( "target/sample_out/", PDF_A_UA_FOP+"test-issue-52.pdf" );
		try (FileOutputStream fos = new FileOutputStream( file )) {
			config.fullProcess( "pdf_a_test", DocProcessContext.newContext(), PDF_A_UA_FOP, fos );
		}
		Assert.assertTrue( file.exists() );
	}

}
