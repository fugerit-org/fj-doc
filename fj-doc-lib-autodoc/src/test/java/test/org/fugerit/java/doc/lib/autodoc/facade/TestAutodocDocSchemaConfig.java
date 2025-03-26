package test.org.fugerit.java.doc.lib.autodoc.facade;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.lib.autodoc.AutodocDocConfig;
import org.fugerit.java.doc.lib.autodoc.facade.XsdParserFacade;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
class TestAutodocDocSchemaConfig {

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
	void testParseXsdModel() {
		File outputFile = new File( TARGET, "xsd_ref_sample_1.html" );
		try ( FileOutputStream fos = new FileOutputStream( outputFile ) )  {
			AutodocModel autodocModel = parseSample1();
			AutodocDocConfig docConfig = AutodocDocConfig.newConfig();
			Properties params = new Properties();
			docConfig.processAutodocSchemaHtmlDefault(autodocModel, fos, params);
			autodocModel.getTypes().forEach( t -> log.info( "key : {}", t.getKey() ) );
			autodocModel.getSimpleTypes().forEach( st -> log.info( "note : {}", st.getNote() ) );
			Assertions.assertTrue( outputFile.exists() );
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail( message );
		}
	}
	
}
