package org.fugerit.java.doc.json.parse;

import java.io.Reader;
import java.io.Writer;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.xml2json.XmlToJsonHandler;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DocJsonToXml {
	
	private XmlToJsonHandler handler;
	
	public DocJsonToXml() {
		this( new ObjectMapper() );
	}
	
	public DocJsonToXml(ObjectMapper mapper) {
		this( new XmlToJsonHandler( mapper ) );
	}

	public DocJsonToXml(XmlToJsonHandler handler) {
		super();
		this.handler = handler;
	}
	
	public void writerAsXml( Reader jsonReader, Writer writer ) throws ConfigException {
		ConfigException.apply( () -> {
			Element root = this.convertToElement(jsonReader);
			DOMIO.writeDOMIndent( root , writer );
		} );
	}
	
	public Element convertToElement( Reader jsonReader ) throws ConfigException {
		return ConfigException.get( () -> {
			JsonNode node = this.handler.getMapper().readTree( jsonReader );
			return this.convert(node);
		} );
	}
	
	public Element convert( JsonNode json ) throws ConfigException {
		Element root = this.handler.convert(json);
		String xsdVersion = root.getAttribute( DocObjectMapperHelper.PROPERTY_XSD_VERSION );
		if ( StringUtils.isNotEmpty( xsdVersion ) ) {
			root.removeAttribute( DocObjectMapperHelper.PROPERTY_XSD_VERSION );
		}
		return root;
	}
	
}
