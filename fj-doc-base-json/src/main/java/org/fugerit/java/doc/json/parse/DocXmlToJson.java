package org.fugerit.java.doc.json.parse;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.core.xml.dom.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DocXmlToJson {
	
	public DocXmlToJson() {
		this( new ObjectMapper() );
	}
	
	public DocXmlToJson(ObjectMapper mapper) {
		super();
		this.mapper = mapper;
	}

	private ObjectMapper mapper;

	private ObjectNode create( Element currentTag, ObjectNode currentNode ) throws Exception {
		// mapping normal properties
		Properties props = DOMUtils.attributesToProperties( currentTag );
		for ( Object k : props.keySet() ) {
			String key = String.valueOf( k );
			currentNode.put( key , props.getProperty( key ));
		}
		// adding special properties
		currentNode.put( DocObjectMapperHelper.PROPERTY_TAG , currentTag.getTagName() );
		if ( currentTag.hasChildNodes() ) {
			NodeList list = currentTag.getChildNodes();
			List<ObjectNode> kids = new ArrayList<>();
			StringBuilder textBuffer = new StringBuilder();
			for ( int k=0; k<list.getLength(); k++ ) {
				Node currentTagChild = list.item( k );
				if ( currentTagChild instanceof Element ) {
					kids.add( this.create((Element)currentTagChild, this.mapper.createObjectNode()) );
				} else if ( currentTagChild instanceof Text ) {
					textBuffer.append( ((Text)currentTagChild).getTextContent() );
				}
			}
			if ( !kids.isEmpty() ) {
				ArrayNode kidsNode = this.mapper.createArrayNode();
				for ( ObjectNode currentKid : kids ) {
					kidsNode.add( currentKid );
				}
				currentNode.set( DocObjectMapperHelper.PROPERTY_ELEMENTS, kidsNode );
			}
			String text = textBuffer.toString();
			if ( StringUtils.isNotEmpty( text ) ) {
				currentNode.put( DocObjectMapperHelper.PROPERTY_TEXT, text );
			}
		}
		return currentNode;
	}
	
	public JsonNode convertToJsonNode( Reader xml ) throws ConfigException {
		JsonNode tree;
		try {
			Document doc = DOMIO.loadDOMDoc( xml );
			Element root = doc.getDocumentElement();
			tree = this.convert( root );
		} catch (Exception e) {
			throw new ConfigException( "Errore converting xml to json node : "+e, e );
		}
		return tree;
	}
	
	public JsonNode convert( Element root ) throws ConfigException {
		JsonNode tree = null;
		try {
			tree = this.create( root, this.mapper.createObjectNode() );
		} catch (Exception e) {
			throw  new ConfigException( "Conversion error : "+e, e );
		}
		return tree;
		
	}
	
}
