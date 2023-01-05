package test.org.fugerit.java.doc.lib.autodoc.facade;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocVersion;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.fugerit.java.doc.lib.autodoc.facade.AutodocModelToSinpleTableFacade;
import org.fugerit.java.doc.lib.autodoc.facade.XsdParserFacade;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXsdParserFacade {

	private final static Logger logger = LoggerFactory.getLogger( TestXsdParserFacade.class );
	
	@Test
	public void testParseXsdModel() {
		String path =  "../fj-doc-base/src/main/resources/config/doc-"+DocVersion.CURRENT_VERSION+".xsd";
		logger.info( "Try parsing xsd : {}", path );
		DocTypeHandler handler = FreeMarkerHtmlTypeHandler.HANDLER;
		try ( FileOutputStream fos = new FileOutputStream( new File( "target/autodoc."+handler.getType() ) ) )  {
			XsdParserFacade xsdParserFacade = new XsdParserFacade();
			AutodocModel autodocModel = xsdParserFacade.parse( path );
			AutodocModelToSinpleTableFacade autodocModelToSinpleTableFacade = new AutodocModelToSinpleTableFacade();
			SimpleTable simpleTable = autodocModelToSinpleTableFacade.toSimpleTable(autodocModel);
			SimpleTableDocConfig config = SimpleTableDocConfig.newConfig();
			config.processSimpleTable(simpleTable, handler, fos);
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message );
		}
	}
	
}
