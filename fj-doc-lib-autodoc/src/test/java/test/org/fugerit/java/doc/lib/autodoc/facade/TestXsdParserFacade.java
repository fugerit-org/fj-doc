package test.org.fugerit.java.doc.lib.autodoc.facade;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.fugerit.java.doc.lib.autodoc.VenusAutodocFacade;
import org.fugerit.java.doc.lib.autodoc.facade.AutodocModelToSinpleTableFacade;
import org.fugerit.java.doc.lib.autodoc.facade.XsdParserFacade;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocElement;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.fugerit.java.doc.lib.simpletable.SimpleTableDocConfig;
import org.fugerit.java.doc.lib.simpletable.model.SimpleTable;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestXsdParserFacade {

	@Test
	public void testParseXsdModel() {
		DocTypeHandler handler = FreeMarkerHtmlTypeHandler.HANDLER;
		try ( FileOutputStream fos = new FileOutputStream( new File( "target/autodoc."+handler.getType() ) ) )  {
			AutodocModel autodocModel = VenusAutodocFacade.parseLast();
			AutodocModelToSinpleTableFacade autodocModelToSinpleTableFacade = new AutodocModelToSinpleTableFacade();
			SimpleTable simpleTable = autodocModelToSinpleTableFacade.toSimpleTable(autodocModel);
			SimpleTableDocConfig config = SimpleTableDocConfig.newConfig();
			config.processSimpleTable(simpleTable, handler, fos);
			log.info( "xsdParser : {}", autodocModel.getXsdParser() );
			// iterate elements
			for ( String eName : autodocModel.getElementNames() ) {
				if ( autodocModel.containsName( eName ) ) {
					AutodocElement element = autodocModel.getElement(eName);
					log.info( "current element model : {}, attributes : {}", element.getAutodocModel(), element.getXsdAttributes() );
				}
			}
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail( message );
		}
	}
	
	private static final String CURRENT_VERSION = "custom";
	
	private static final String TITLE = "Reference xsd documentation for Venus - Fugerit Document Generation Framework (fj-doc)";
	
	private static final String XSD_PREFIX = "xsd:";
	
	private static final String AUTODOC_PREFIX = "doc:";
	
	private static AutodocModel parseCustom() throws ConfigException {
		String path =  "./src/test/resources/docs/doc-custom.xsd";
		XsdParserFacade xsdParserFacade = new XsdParserFacade();
		AutodocModel autodocModel = xsdParserFacade.parse( path );
		autodocModel.setVersion( CURRENT_VERSION );
		autodocModel.setTitle( TITLE );
		autodocModel.setXsdPrefix( XSD_PREFIX );
		autodocModel.setAutodocPrefix( AUTODOC_PREFIX );
		return autodocModel;
	}
	
	@Test
	public void testParseXsdModelCustom() {
		DocTypeHandler handler = FreeMarkerHtmlTypeHandler.HANDLER;
		try ( FileOutputStream fos = new FileOutputStream( new File( "target/autodoc."+handler.getType() ) ) )  {
			AutodocModel autodocModel = parseCustom();
			AutodocModelToSinpleTableFacade autodocModelToSinpleTableFacade = new AutodocModelToSinpleTableFacade();
			SimpleTable simpleTable = autodocModelToSinpleTableFacade.toSimpleTable(autodocModel);
			SimpleTableDocConfig config = SimpleTableDocConfig.newConfig();
			config.processSimpleTable(simpleTable, handler, fos);
			log.info( "xsdParser : {}", autodocModel.getXsdParser() );
			// iterate elements
			for ( String eName : autodocModel.getElementNames() ) {
				if ( autodocModel.containsName( eName ) ) {
					AutodocElement element = autodocModel.getElement(eName);
					log.info( "current element model : {}, attributes : {}", element.getAutodocModel(), element.getXsdAttributes() );
					element.getAutodocAttributes().stream().forEach( a -> log.info( "current att notes : {}", a.getNote() ) ) ;
				}
			}
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail( message );
		}
	}
	
}
