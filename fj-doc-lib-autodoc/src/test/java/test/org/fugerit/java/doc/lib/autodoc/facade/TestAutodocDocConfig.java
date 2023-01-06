package test.org.fugerit.java.doc.lib.autodoc.facade;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;

import org.fugerit.java.doc.base.config.DocVersion;
import org.fugerit.java.doc.lib.autodoc.AutodocDocConfig;
import org.fugerit.java.doc.lib.autodoc.facade.XsdParserFacade;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAutodocDocConfig {

	private final static Logger logger = LoggerFactory.getLogger( TestAutodocDocConfig.class );
	
	@Test
	public void testParseXsdModel() {
		String path =  "../fj-doc-base/src/main/resources/config/doc-"+DocVersion.CURRENT_VERSION+".xsd";
		logger.info( "Try parsing xsd : {}", path );
		try ( FileOutputStream fos = new FileOutputStream( new File( "target/autodoc_doc_output.html" ) ) )  {
			XsdParserFacade xsdParserFacade = new XsdParserFacade();
			AutodocModel autodocModel = xsdParserFacade.parse( path );
			AutodocDocConfig docConfig = AutodocDocConfig.newConfig();
			docConfig.processAutodocHtmlDefault(autodocModel, fos);
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message );
		}
	}
	
}
