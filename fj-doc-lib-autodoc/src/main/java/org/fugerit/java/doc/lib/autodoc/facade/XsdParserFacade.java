package org.fugerit.java.doc.lib.autodoc.facade;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.xmlet.xsdparser.core.XsdParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XsdParserFacade {

	public AutodocModel parse(String filePath) throws ConfigException {
		return ConfigException.getWithMessage( () -> {
			XsdParser xsdParser = new XsdParser(filePath);
			final AutodocModel autodocModel = new AutodocModel( xsdParser );
			xsdParser.getResultXsdElements().forEach( currentElement -> {
				log.info("current element {} -> {}", currentElement.getName(), currentElement.getType());
				autodocModel.addElement( currentElement );
			} );
			xsdParser.getResultXsdSchemas().forEach( currentSchema -> {
				log.info("current schema {} -> {}", currentSchema.getFinalDefault(), currentSchema.getTargetNamespace() );
			} );
			xsdParser.
			return autodocModel;
		} , "Error parsing xsd" );
	}

}
