package org.fugerit.java.doc.lib.autodoc;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.lib.autodoc.facade.XsdParserFacade;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;

/**
 * Parse via autodoc library the Venus xsd.
 * 
 * @author fugerit
 *
 */
public class VenusFreemarkerAutodocFacade {

	private VenusFreemarkerAutodocFacade() {}
	
	public static final String CURRENT_VERSION = "1.0.0-rc.004";
	
	public static final String TITLE = "Reference xsd documentation for Venus - Fugerit Document Generation Framework (fj-doc) - Freemarker Configuration";
	
	public static final String XSD_PREFIX = "xsd:";
	
	public static final String AUTODOC_PREFIX = "fdp:";
	
	public static AutodocModel parseLast() throws ConfigException {
		String path =  "../fj-doc-freemarker/src/main/resources/config_fm_xsd/freemarker-doc-process-1-0.xsd";
		XsdParserFacade xsdParserFacade = new XsdParserFacade();
		AutodocModel autodocModel = xsdParserFacade.parse( path );
		autodocModel.setVersion( CURRENT_VERSION );
		autodocModel.setTitle( TITLE );
		autodocModel.setXsdPrefix( XSD_PREFIX );
		autodocModel.setAutodocPrefix( AUTODOC_PREFIX );
		return autodocModel;
	}
	
}
