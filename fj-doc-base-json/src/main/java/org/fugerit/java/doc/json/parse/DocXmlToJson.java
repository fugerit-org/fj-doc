package org.fugerit.java.doc.json.parse;

import java.io.Reader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.xml2json.XmlToJsonHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DocXmlToJson {
	
	private XmlToJsonHandler hanlder;
	
	public DocXmlToJson() {
		this( new ObjectMapper() );
	}
	
	public DocXmlToJson(ObjectMapper mapper) {
		this( new XmlToJsonHandler( mapper ) );
	}
	
	public DocXmlToJson(XmlToJsonHandler handler) {
		super();
		this.hanlder = handler;
	}
	
	public JsonNode convertToJsonNode( Reader xml ) throws ConfigException {
		return ConfigException.get( () -> {
			Document doc = DOMIO.loadDOMDoc( xml );
			Element root = doc.getDocumentElement();
			return this.convert( root );
		} );
	}
	
	public JsonNode convert( Element root ) throws ConfigException {
		return ConfigException.get( () -> this.hanlder.convert( root ) );
	}
	
}
