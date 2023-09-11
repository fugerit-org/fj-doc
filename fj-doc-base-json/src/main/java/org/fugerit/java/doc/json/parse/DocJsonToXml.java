package org.fugerit.java.doc.json.parse;

import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DocJsonToXml {
	
	public DocJsonToXml() {
		this( new ObjectMapper() );
	}
	
	public DocJsonToXml(ObjectMapper mapper) {
		super();
		this.mapper = mapper;
	}

	private ObjectMapper mapper;

	private void iterateElement( JsonNode current, Document doc, Element tag ) throws ConfigException {
		JsonNode elementsNode = current.get( DocObjectMapperHelper.PROPERTY_ELEMENTS );
		if ( elementsNode != null ) {
			if ( elementsNode.isArray() ) {
				Iterator<JsonNode> itElements = elementsNode.elements();
				while ( itElements.hasNext() ) {
					JsonNode currentElement = itElements.next();
					this.create(doc, tag, currentElement);
				}
			} else {
				throw new ConfigException( "Property must be an array : "+elementsNode );
			}
		}
	}
	
	private void iterateAttribute( JsonNode current, Element tag ) {
		Iterator<String> itNames = current.fieldNames();
		while ( itNames.hasNext() ) {
			String currentName = itNames.next();
			if ( !DocObjectMapperHelper.isSpecialProperty( currentName ) ) {
				tag.setAttribute( currentName , current.get( currentName ).asText() );
			}
		}
	}
	
	private Element create( Document doc, Element parent, JsonNode current ) throws ConfigException {
		Element tag = null;
		JsonNode tagNode = current.get( DocObjectMapperHelper.PROPERTY_TAG );
		if ( tagNode == null ) {
			throw new ConfigException( "Tag node is null : "+DocObjectMapperHelper.PROPERTY_TAG );
		} else {
			String tagName = tagNode.asText();
			tag = doc.createElement( tagName );
			if ( parent != null ) {
				parent.appendChild( tag );
			}
			JsonNode textNode = current.get( DocObjectMapperHelper.PROPERTY_TEXT );
			if ( textNode != null ) {
				tag.appendChild( doc.createTextNode( textNode.asText() ) );
			}
			this.iterateElement(current, doc, tag);
			this.iterateAttribute(current, tag);
		}
		return tag;
	}
	
	public void writerAsXml( Reader jsonReader, Writer writer ) throws ConfigException {
		ConfigException.apply( () -> {
			Element root = this.convertToElement(jsonReader);
			DOMIO.writeDOMIndent( root , writer );
		} );
	}
	
	public Element convertToElement( Reader jsonReader ) throws ConfigException {
		return ConfigException.get( () -> {
			JsonNode node = this.mapper.readTree( jsonReader );
			return this.convert(node);
		} );
	}
	
	public Element convert( JsonNode json ) throws ConfigException {
		return ConfigException.get( () -> {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware( true );
			dbf.setValidating( false );
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element root = this.create(doc, null, json);
			root.setAttribute( "xmlns" , DocFacade.SYSTEM_ID );
			root.setAttribute( "xmlns:xsi" , "http://www.w3.org/2001/XMLSchema-instance" );
			String xsdVersion = DocObjectMapperHelper.findVersion(json, DocFacade.CURRENT_VERSION) ;
			root.setAttribute( "xsi:schemaLocation" , DocParserContext.createXsdVersionXmlns(xsdVersion) );
			return root;
		} );
		
	}
	
}
