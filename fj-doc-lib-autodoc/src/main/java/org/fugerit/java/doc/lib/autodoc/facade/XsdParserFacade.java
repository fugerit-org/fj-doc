package org.fugerit.java.doc.lib.autodoc.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.lib.autodoc.parser.model.AutodocModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.XsdElement;

public class XsdParserFacade {

	private static final Logger logger = LoggerFactory.getLogger(XsdParserFacade.class);

	public AutodocModel parse(String filePath) throws ConfigException {
		AutodocModel autodocModel = null;
		try {
			XsdParser xsdParser = new XsdParser(filePath);
			autodocModel = new AutodocModel( xsdParser );
			List<XsdElement> elements = xsdParser.getResultXsdElements().collect(Collectors.toList());
			for (XsdElement currentElement : elements) {
				logger.info("current element {} -> {}", currentElement.getName(), currentElement.getType());
				autodocModel.addElement( currentElement );
			}
		} catch (Exception e) {
			throw new ConfigException("Error parsing xsd : " + e, e);
		}
		return autodocModel;
	}


}
