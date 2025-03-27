package test.org.fugerit.java.doc.sample.freemarker;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;

/*
 * Test case or issue : https://github.com/fugerit-org/fj-doc/issues/52
 */
@Slf4j
class TestDocHandlerFacade {

	private static final String PDF_A_UA_FOP = "pdf-a-ua-fop";

	private static FreemarkerDocProcessConfig config =
			FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://config/freemarker-doc-process-test-doc-handler.xml" );

	@Test
	void testDocHandlers() throws Exception {
		DocHandlerFacade facade = config.getFacade();
		for ( DocTypeHandler handler : new LinkedHashSet<>( facade.handlers() ) ) {
			log.info( "current handler : {}", handler );
		}
		log.info( "type test : {} -> {}", PDF_A_UA_FOP, config.getFacade().findHandler( PDF_A_UA_FOP ) );
		File file = new File( "target/sample_out/", PDF_A_UA_FOP+"test-issue-52-doc-handler.pdf" );
		try (FileOutputStream fos = new FileOutputStream( file )) {
			config.fullProcess( "pdf_a_test", DocProcessContext.newContext(), PDF_A_UA_FOP, fos );
		}
		Assertions.assertTrue( file.exists() );
	}

}
