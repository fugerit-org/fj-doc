package test.org.fugerit.java.doc.lib.autodoc.facade;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.lib.autodoc.AutodocDocConfig;
import org.fugerit.java.doc.lib.autodoc.facade.XsdParserFacade;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import static org.junit.Assert.fail;

public class TestAutodocDocSchemaConfig {

	private static final Logger logger = LoggerFactory.getLogger( TestAutodocDocSchemaConfig.class );
	
	private static final String TARGET = "target";

	private static AutodocModel parseSample1() throws ConfigException {
		String path =  "src/test/resources/sample_xsd/sample_1_main.xsd";
		XsdParserFacade xsdParserFacade = new XsdParserFacade();
		AutodocModel autodocModel = xsdParserFacade.parse( path );
		autodocModel.setVersion( "1.0.0" );
		autodocModel.setTitle( "Sample 1 XSD" );
		autodocModel.setXsdPrefix( "xsd:" );
		autodocModel.setAutodocPrefix( "sam1:" );
		return autodocModel;
	}

	@Test
	public void testParseXsdModel() {
		try ( FileOutputStream fos = new FileOutputStream( new File( TARGET, "xsd_ref_sample_1.html" ) ) )  {
			AutodocModel autodocModel = parseSample1();
			AutodocDocConfig docConfig = AutodocDocConfig.newConfig();
			Properties params = new Properties();
			docConfig.processAutodocSchemaHtmlDefault(autodocModel, fos, params);
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message );
		}
	}
	
}
