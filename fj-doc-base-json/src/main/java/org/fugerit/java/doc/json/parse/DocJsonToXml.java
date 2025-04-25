package org.fugerit.java.doc.json.parse;

import java.io.Reader;
import java.io.Writer;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocVersion;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.parser.DocParserContext;
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
	
	private static final String ATT_XMLNS = "xmlns";
	
	private static final String ATT_XMLNS_XSI = "xmlns:xsi";
	
	private static final String ATT_XSD_LOC = "xsi:schemaLocation";
	
	private void setIfNotFound( Element tag, String name, String value ) {
		if ( StringUtils.isEmpty( tag.getAttribute( name ) ) ) {
			tag.setAttribute( name , value );
		}
	}
	
	public Element convert( JsonNode json ) throws ConfigException {
		Element root = this.handler.convert(json);
		String xsdVersion = root.getAttribute( DocObjectMapperConstants.PROPERTY_XSD_VERSION );
		if ( StringUtils.isNotEmpty( xsdVersion ) ) {
			root.removeAttribute( DocObjectMapperConstants.PROPERTY_XSD_VERSION );
		}
		// finishing touches
		xsdVersion = StringUtils.valueWithDefault( xsdVersion  , DocVersion.CURRENT_VERSION.stringVersion() );	
		this.setIfNotFound(root, ATT_XMLNS, DocFacade.SYSTEM_ID);
		this.setIfNotFound(root, ATT_XMLNS_XSI, "http://www.w3.org/2001/XMLSchema-instance");
		this.setIfNotFound(root, ATT_XSD_LOC, DocParserContext.createXsdVersionXmlns( xsdVersion ));
		return root;
	}
	
}
