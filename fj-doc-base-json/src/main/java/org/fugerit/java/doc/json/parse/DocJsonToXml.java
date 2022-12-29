package org.fugerit.java.doc.json.parse;

import java.io.Reader;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.facade.DocFacade;
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

	private Element create( Document doc, Element parent, JsonNode current ) throws Exception {
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
			Iterator<String> itNames = current.fieldNames();
			while ( itNames.hasNext() ) {
				String currentName = itNames.next();
				if ( !DocObjectMapperHelper.isSpecialProperty( currentName ) ) {
					tag.setAttribute( currentName , current.get( currentName ).asText() );
				}
			}
		}
		return tag;
	}
	
	public Element convertToElement( Reader jsonReader ) throws ConfigException {
		Element root = null;
		try {
			JsonNode node = this.mapper.readTree( jsonReader );
			root = this.convert(node);
		} catch (Exception e) {
			throw new ConfigException( "Errore converting json to xml : "+e, e );
		}
		return root;
	}
	
	public Element convert( JsonNode json ) throws ConfigException {
		Element root = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware( true );
			dbf.setValidating( false );
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.newDocument();
			root = this.create(doc, null, json);
			root.setAttribute( "xmlns" , DocFacade.SYSTEM_ID );
			root.setAttribute( "xmlns:xsi" , "http://www.w3.org/2001/XMLSchema-instance" );
			String xsdVwersion = DocObjectMapperHelper.findVersion(json, DocFacade.CURRENT_VERSION) ;
			root.setAttribute( "xsi:schemaLocation" , "http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-"+xsdVwersion+".xsd" );
		} catch (Exception e) {
			throw  new ConfigException( "Conversion error : "+e, e );
		}
		return root;
		
	}
	
}
