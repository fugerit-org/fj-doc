package org.fugerit.java.doc.json.ng;

import java.util.Iterator;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocInfo;
import org.fugerit.java.xml2json.XmlToJsonConverter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class XmlToJsonConverterNG extends XmlToJsonConverter {

	private boolean isMetaSection( String tagName ) {
		return DocContainer.TAG_NAME_META.equals( tagName ) || DocContainer.TAG_NAME_METADATA.equals( tagName );
	}
	
	@Override
	public ObjectNode handleTag(ObjectMapper mapper, Element currentTag, ObjectNode currentNode) {
		ObjectNode resultNode = currentNode;
		String tagName = currentTag.getTagName();
		// special handling of metadata section
		if ( this.isMetaSection(tagName) ) {
			currentNode.set( this.getPropertyTag() , new TextNode( tagName ) );
			NodeList list = currentTag.getChildNodes();
			ArrayNode kidsNode = mapper.createArrayNode();
			ObjectNode infoNode = mapper.createObjectNode();
			for ( int k=0; k<list.getLength(); k++ ) {
				Node kidNode = list.item(k);
				if ( kidNode instanceof Element ) {
					Element kidTag = (Element)kidNode;
					if ( DocInfo.TAG_NAME.equals( kidTag.getTagName() ) ) {
						infoNode.set( kidTag.getAttribute( DocInfo.ATT_NAME ) , new TextNode( kidTag.getTextContent() ) );
					} else {
						kidsNode.add( super.handleTag(mapper, kidTag, mapper.createObjectNode() ) );
					}
				}
			}
			if ( !infoNode.isEmpty() ) {
				resultNode.set( DocInfo.TAG_NAME , infoNode );
			}
			resultNode.set( this.getPropertyElements(), kidsNode );
		} else {
			resultNode = super.handleTag(mapper, currentTag, currentNode);
		}
		return resultNode;
	}
	
	@Override
	public Element handleNode(Document doc, Element parent, JsonNode current) throws ConfigException {
		Element tag = null;
		String tagName = this.getTagNameOrThrowException(current);
		if ( this.isMetaSection( tagName ) ) {
			tag = doc.createElement( tagName );
			ObjectNode infoNode = (ObjectNode)current.get( DocInfo.TAG_NAME );
			Iterator<String> itFields = infoNode.fieldNames();
			while ( itFields.hasNext() ) {
				String currentInfoKey = itFields.next();
				String currentInfoValue = ((TextNode)infoNode.get( currentInfoKey )).asText();
				Element infoTag = doc.createElement(DocInfo.TAG_NAME);
				infoTag.setAttribute( DocInfo.ATT_NAME , currentInfoKey );
				infoTag.setTextContent( currentInfoValue );
				tag.appendChild( infoTag );
			}
			if ( parent != null ) {
				parent.appendChild( tag );
			}
			this.iterateElement(current, doc, tag);
			this.addAttributes(current, tag);			
		} else {
			tag = super.handleNode(doc, parent, current);
		}
		return tag;
	}
	
	
	
}
