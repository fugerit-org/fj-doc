package org.fugerit.java.doc.lib.autodoc.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.XsdElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XsdParserFacade {

	public AutodocModel parse(String filePath) throws ConfigException {
		return ConfigException.getWithMessage( () -> {
			AutodocModel autodocModel = null;
			XsdParser xsdParser = new XsdParser(filePath);
			autodocModel = new AutodocModel( xsdParser );
			List<XsdElement> elements = xsdParser.getResultXsdElements().collect(Collectors.toList());
			for (XsdElement currentElement : elements) {
				log.info("current element {} -> {}", currentElement.getName(), currentElement.getType());
				autodocModel.addElement( currentElement );
			}
			return autodocModel;
		} , "Error parsing xsd" );
	}


}
