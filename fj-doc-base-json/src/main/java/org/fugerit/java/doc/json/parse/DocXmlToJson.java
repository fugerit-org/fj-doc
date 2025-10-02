package org.fugerit.java.doc.json.parse;

import java.io.Reader;
import java.io.Writer;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.parser.DocConvert;
import org.fugerit.java.xml2json.XmlToJsonHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DocXmlToJson implements DocConvert {
	
	private XmlToJsonHandler handler;
	
	public DocXmlToJson() {
		this( JsonConstants.getDefaultMapper() );
	}
	
	public DocXmlToJson(ObjectMapper mapper) {
		this( new XmlToJsonHandler( mapper ) );
	}
	
	public DocXmlToJson(XmlToJsonHandler handler) {
		super();
		this.handler = handler;
	}
	
	public JsonNode convertToJsonNode( Reader xml ) throws ConfigException {
		return ConfigException.get( () -> {
			Document doc = DOMIO.loadDOMDoc( xml );
			Element root = doc.getDocumentElement();
			return this.convert( root );
		} );
	}
	
	public JsonNode convert( Element root ) throws ConfigException {
		return ConfigException.get( () -> this.handler.convert( root ) );
	}

	@Override
	public void convert(Reader from, Writer to) throws ConfigException {
		ConfigException.apply( () -> this.handler.getMapper().writerWithDefaultPrettyPrinter().writeValue( to, this.convertToJsonNode( from ) ) );
	}
}
