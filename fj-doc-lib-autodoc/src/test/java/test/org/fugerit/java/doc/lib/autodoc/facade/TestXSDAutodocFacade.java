package test.org.fugerit.java.doc.lib.autodoc.facade;

import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.Reader;

import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.lib.autodoc.facade.XSDAutodocFacade;
import org.fugerit.java.doc.lib.autodoc.model.XsdDocModel;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXSDAutodocFacade {

	private final static Logger logger = LoggerFactory.getLogger( TestXSDAutodocFacade.class );
	
	@Test
	public void testParseXsdModel() {
		String path =  "../fj-doc-base/src/main/resources/config/doc-"+DocFacade.CURRENT_VERSION+".xsd";
		logger.info( "Try parsing xsd : {}", path );
		try ( Reader xsdReader = new FileReader( path ) ) {
			XSDAutodocFacade facade = new XSDAutodocFacade();
			XsdDocModel xsdModel = facade.parse( xsdReader );
			Assert.assertNotNull( "Xsd parsed" , xsdModel );
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message );
		}
	}
	
}
