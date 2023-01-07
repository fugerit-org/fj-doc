package test.org.fugerit.java.doc.lib.autodoc.facade;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;

import org.fugerit.java.doc.lib.autodoc.AutodocDocConfig;
import org.fugerit.java.doc.lib.autodoc.VenusAutodocFacade;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAutodocDocConfig {

	private static final Logger logger = LoggerFactory.getLogger( TestAutodocDocConfig.class );
	
	private static final String TARGET = "src/main/docs/doc_xsd_config_ref.html";
	
	//private static final String TARGET = "target/doc_xsd_config_ref.html";
	
	@Test
	public void testParseXsdModel() {
		try ( FileOutputStream fos = new FileOutputStream( new File( TARGET ) ) )  {
			AutodocModel autodocModel = VenusAutodocFacade.parseLast();
			AutodocDocConfig docConfig = AutodocDocConfig.newConfig();
			docConfig.processAutodocHtmlDefault(autodocModel, fos);
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message );
		}
	}
	
}
