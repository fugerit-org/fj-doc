package org.fugerit.java.doc.json.parse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.xml.DocXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DocObjectMapperHelper {
	
	public DocObjectMapperHelper(ObjectMapper mapper) {
		super();
		this.mapper = mapper;
	}

	private ObjectMapper mapper;
	
	public static final String PROPERTY_TAG = "_t";
	
	public static final String PROPERTY_TEXT = "_v";

	public static final String PROPERTY_ELEMENTS = "_e";
	
	public static final String PROPERTY_XSD_VERSION = "xsd-version";
	
	private final static Logger logger = LoggerFactory.getLogger( DocObjectMapperHelper.class );
	
	private final static Set<String> SPECIAL_PROPERTY_NAMES = new HashSet<>();
	static {
		SPECIAL_PROPERTY_NAMES.add( PROPERTY_TAG );
		SPECIAL_PROPERTY_NAMES.add( PROPERTY_TEXT );
		SPECIAL_PROPERTY_NAMES.add( PROPERTY_ELEMENTS );
		SPECIAL_PROPERTY_NAMES.add( PROPERTY_XSD_VERSION );
	}
	
	public static boolean isSpecialProperty( String propertyName ) {
		return SPECIAL_PROPERTY_NAMES.contains(propertyName);
	}
	
	public static String findVersion( JsonNode root, String def ) {
		String res = def;
		JsonNode xsdVersion = root.get( PROPERTY_XSD_VERSION );
		if ( xsdVersion != null ) {
			res = xsdVersion.asText();
		}
		return res;
	}
	
	private void handleElement( JsonNode node, DocParserContext context ) {
		Iterator<String> fieldsNames = node.fieldNames();
		Properties props = new Properties();
		String qName = null;
		String text = null;
		Iterator<JsonNode> elements = null;
		while ( fieldsNames.hasNext() ) {
			String currentName = fieldsNames.next();
			JsonNode currentValue = node.get( currentName );
			if ( PROPERTY_TEXT.equalsIgnoreCase( currentName ) ) {
				text = currentValue.asText();
			} else if ( PROPERTY_TAG.equalsIgnoreCase( currentName ) ) {
				qName = currentValue.asText();
			} else if ( PROPERTY_ELEMENTS.equalsIgnoreCase( currentName ) ) {
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

	public DocValidationResult validateWorkerResult(Reader reader) throws Exception {
		DocValidationResult result = DocValidationResult.newDefaultNotDefiniedResult();
		DocJsonToXml convert = new DocJsonToXml( this.mapper );
		Element root = convert.convertToElement( reader );
		try ( ByteArrayOutputStream buffer = new ByteArrayOutputStream() )  {
			DOMIO.writeDOMIndent(root, buffer);
			try ( Reader xmlReader = new InputStreamReader( new ByteArrayInputStream( buffer.toByteArray() ) ) ) {
				DocXmlParser parser = new DocXmlParser();
				result = parser.validateResult(xmlReader);
				if ( !result.getErrorList().isEmpty() ) {
					result.getInfoList().add( "This validation is made through conversion to xml, so lines/columns number in errors are to be considered an hint" );
				}
			}
		}
		return result;
	}
	
	public DocBase parse(Reader reader) throws Exception {
		DocParserContext context = new DocParserContext();
		context.startDocument();
		JsonNode root = this.mapper.readTree( reader );
		this.handleElement(root, context);
		context.endDocument();
		logger.debug( "Parse done!" );
		DocBase docBase = context.getDocBase();
		docBase.setXsdVersion( findVersion(root, DocFacade.CURRENT_VERSION) );
		return docBase;
	}
	
}
