package org.fugerit.java.doc.json.parse;

import java.io.Reader;
import java.util.Iterator;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DocObjectMapperHelper {
	
	public DocObjectMapperHelper(ObjectMapper mapper) {
		super();
		this.mapper = mapper;
	}

	private ObjectMapper mapper;
	
	public static final String PROPERTY_TAG = "tag";
	
	public static final String PROPERTY_TAG_ALT = "_t";
	
	public static final String PROPERTY_TEXT = "text";
	
	public static final String PROPERTY_TEXT_ALT = "_v";

	public static final String PROPERTY_ELEMENTS = "elements";
	
	public static final String PROPERTY_ELEMENTS_ALT = "_e";
	
	private final static Logger logger = LoggerFactory.getLogger( DocObjectMapperHelper.class );
	
	private void handleElement( JsonNode node, DocParserContext context ) {
		Iterator<String> fieldsNames = node.fieldNames();
		Properties props = new Properties();
		String qName = null;
		String text = null;
		Iterator<JsonNode> elements = null;
		while ( fieldsNames.hasNext() ) {
			String currentName = fieldsNames.next();
			JsonNode currentValue = node.get( currentName );
			if ( PROPERTY_TEXT.equalsIgnoreCase( currentName ) || PROPERTY_TEXT_ALT.equalsIgnoreCase( currentName ) ) {
				text = currentValue.asText();
			} else if ( PROPERTY_TAG.equalsIgnoreCase( currentName ) || PROPERTY_TAG_ALT.equalsIgnoreCase( currentName ) ) {
				qName = currentValue.asText();
			} else if ( PROPERTY_ELEMENTS.equalsIgnoreCase( currentName ) || PROPERTY_ELEMENTS_ALT.equalsIgnoreCase( currentName ) ) {
				elements = currentValue.elements();
			} else {
				props.setProperty( currentName , currentValue.asText() );
			}
		}
		context.handleStartElement(qName, props);
		if ( StringUtils.isNotEmpty( text ) ) {
			context.handleText(text);
		}
		if ( elements != null ) {
			while ( elements.hasNext() ) {
				this.handleElement( elements.next() , context );
			}
		}
		context.handleEndElement(qName);
	}

	
	public DocBase parse(Reader reader) throws Exception {
		DocParserContext context = new DocParserContext();
		context.startDocument();
		JsonNode root = this.mapper.readTree( reader );
		this.handleElement(root, context);
		context.endDocument();
		logger.debug( "Parse done!" );
		return context.getDocBase();
	}
	
}
