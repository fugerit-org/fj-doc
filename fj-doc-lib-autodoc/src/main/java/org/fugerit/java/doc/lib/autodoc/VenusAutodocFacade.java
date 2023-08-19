package org.fugerit.java.doc.lib.autodoc;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocVersion;
import org.fugerit.java.doc.lib.autodoc.facade.XsdParserFacade;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;

/**
 * Parse via autodoc library the Venus xsd.
 * 
 * @author fugerit
 *
 */
public class VenusAutodocFacade {

	private VenusAutodocFacade() {}
	
	public static final String CURRENT_VERSION = "2.1.0-rc.1";
	
	public static final String TITLE = "Reference xsd documentation for Venus - Fugerit Document Generation Framework (fj-doc)";
	
	public static final String XSD_PREFIX = "xsd:";
	
	public static final String AUTODOC_PREFIX = "doc:";
	
	public static AutodocModel parseLast() throws ConfigException {
		String path =  "../fj-doc-base/src/main/resources/config/doc-"+DocVersion.CURRENT_VERSION+".xsd";
		XsdParserFacade xsdParserFacade = new XsdParserFacade();
		AutodocModel autodocModel = xsdParserFacade.parse( path );
		autodocModel.setVersion( CURRENT_VERSION );
		autodocModel.setTitle( TITLE );
		autodocModel.setXsdPrefix( XSD_PREFIX );
		autodocModel.setAutodocPrefix( AUTODOC_PREFIX );
		return autodocModel;
	}
	
}
