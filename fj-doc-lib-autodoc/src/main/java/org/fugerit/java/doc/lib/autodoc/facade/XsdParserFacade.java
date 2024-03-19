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
				log.debug("current element {} -> {}", currentElement.getName(), currentElement.getType());
				autodocModel.addElement( currentElement );
			} );
			xsdParser.getResultXsdSchemas().forEach( currentSchema -> {
				log.debug("current schema {} -> {}", currentSchema.getTargetNamespace(), currentSchema.getNamespaces() );
				currentSchema.getNamespaces().entrySet().forEach( e -> log.debug( "namespace : {} : {}", e.getKey(), e.getValue().getName() ) );
				currentSchema.getChildrenComplexTypes().forEach( autodocModel::addType );
				currentSchema.getChildrenSimpleTypes().forEach( autodocModel::addSimpleType );
			} );
			return autodocModel;
		} , "Error parsing xsd" );
	}

}
