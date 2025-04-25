package org.fugerit.java.doc.json.parse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.xml.DocXmlParser;
import org.fugerit.java.xml2json.XmlToJsonConverter;
import org.fugerit.java.xml2json.XmlToJsonHandler;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocObjectMapperHelper {

	private XmlToJsonHandler handler;
	
	public DocObjectMapperHelper(XmlToJsonHandler handler) {
		super();
		this.handler = handler;
	}
	
	public DocObjectMapperHelper(ObjectMapper mapper) {
		this( new XmlToJsonHandler( mapper ) );
	}

	public static final String PROPERTY_TAG = XmlToJsonConverter.DEF_PROPERTY_TAG;
	
	public static final String PROPERTY_TEXT = XmlToJsonConverter.DEF_PROPERTY_TEXT;

	public static final String PROPERTY_ELEMENTS = XmlToJsonConverter.DEF_PROPERTY_ELEMENTS;
	
	public static final String PROPERTY_XSD_VERSION = DocObjectMapperConstants.PROPERTY_XSD_VERSION;
	
	private static final Set<String> SPECIAL_PROPERTY_NAMES = new HashSet<>( Arrays.asList( PROPERTY_TAG, PROPERTY_ELEMENTS, PROPERTY_TAG, PROPERTY_XSD_VERSION ) );

	public static boolean isSpecialProperty( String propertyName ) {
		return SPECIAL_PROPERTY_NAMES.contains(propertyName);
	}

	public DocValidationResult validateWorkerResult(Reader reader, boolean parseVersion) throws DocException {
		return DocException.get( () -> {
			DocValidationResult result = DocValidationResult.newDefaultNotDefinedResult();
			DocJsonToXml convert = new DocJsonToXml( this.handler.getMapper() );
			Element root = convert.convertToElement( reader );
			try ( ByteArrayOutputStream buffer = new ByteArrayOutputStream() )  {
				DOMIO.writeDOMIndent(root, buffer);
				try ( Reader xmlReader = new InputStreamReader( new ByteArrayInputStream( buffer.toByteArray() ) ) ) {
					DocXmlParser parser = new DocXmlParser();
					result = null;
					if ( parseVersion ) {
						result = parser.validateVersionResult(xmlReader);
					} else {
						result = parser.validateResult(xmlReader);
					}
					if ( !result.getErrorList().isEmpty() ) {
						result.getInfoList().add( "This validation is made through conversion to xml, so lines/columns number in errors are to be considered an hint" );
					}
				}
			}
			return result;
		} );
	}
	
	public DocBase parse(Reader reader) throws DocException {
		return DocException.get( () -> {
			DocBase docBase = null;
			try ( StringWriter writer = new StringWriter() ) {
				this.handler.writerAsXml( reader , writer );
				try ( StringReader xml = new StringReader( writer.toString() ) ) {
					DocXmlParser parser = new DocXmlParser();
					docBase = parser.parse( xml );
				}
			}
			log.debug( "Parse done!" );
			return docBase;
		});
	}
	
}
