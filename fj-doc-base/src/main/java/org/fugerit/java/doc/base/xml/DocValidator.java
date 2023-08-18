package org.fugerit.java.doc.base.xml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.config.XMLSchemaCatalogConfig;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.core.xml.sax.eh.ResultErrorHandler;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocValidator {

	private DocValidator() {}
	
	private static final String CURRENT = "current";
	
	private static XMLSchemaCatalogConfig init() {
		XMLSchemaCatalogConfig catalog = null;
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "config/schema-validator-config.xml" ) ) {
			catalog = XMLSchemaCatalogConfig.loadConfigSchema( is );
		} catch (Exception e) {
			throw new RuntimeException( e ); 
		}
		return catalog;
	}
	
	private static final XMLSchemaCatalogConfig SCHEMA_CATALOG = init();
	
	public static SAXParseResult validate( Reader xmlData ) throws XMLException {
		SAXParseResult result = new SAXParseResult();
		try {
			SCHEMA_CATALOG.validateCacheSchema( new ResultErrorHandler( result ) , new SAXSource( new InputSource( xmlData )  ), CURRENT );	
		} catch (Exception e) {
			throw new XMLException( e );
		}
		return result;
	}
	
	public static SAXParseResult validateVersion( Reader xmlData ) throws XMLException {
		SAXParseResult result = new SAXParseResult();
		try {
			String buffer = StreamIO.readString( xmlData );	
			String xsdVersion = getXsdVersion( new StringReader( buffer ) );
			log.info( "xsdVersion -> '{}'", xsdVersion );
			String validateVersion = CURRENT;
			if ( StringUtils.isNotEmpty( xsdVersion ) ) {
				validateVersion = "version-"+xsdVersion;
			}
			log.info( "validateVersion -> '{}'", validateVersion );
			SCHEMA_CATALOG.validateCacheSchema( new ResultErrorHandler( result ) , new SAXSource( new InputSource( new StringReader( buffer ) )  ), validateVersion );	
		} catch (Exception e) {
			throw new XMLException( e );
		}
		return result;
	}
	
	public static void logResult( SAXParseResult result, Logger logger ) throws XMLException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try ( PrintStream ps = new PrintStream( baos ) ) {
			result.printErrorReport( ps );
			logger.info( "Validation issues : \n{}", new String( baos.toByteArray() ) );
		}
	}
	
	private static String getXsdVersion( Reader xmlReader ) throws XMLException {
		String version = null;
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware( true );
			final Properties infos = new Properties();
			SAXParser parser = spf.newSAXParser();
			DefaultHandler versionHandler = new DefaultHandler() {
				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if ( DocBase.TAG_NAME.equalsIgnoreCase( qName ) || DocBase.TAG_NAME.equalsIgnoreCase( localName ) ) {
						Properties props = DocContentHandler.attsToProperties(attributes);
						String xsdVersion = DocParserContext.findXsdVersion(props);
						infos.setProperty( "xsdVersion" , xsdVersion );
					}
				}
			};
			parser.parse( new InputSource( xmlReader ) , versionHandler );
			version = infos.getProperty( "xsdVersion" );
		} catch (Exception e) {
			throw new XMLException( e );
		}
		return version;
	}
	
	public static boolean logValidation( Reader xmlData, Logger logger ) throws XMLException {
		SAXParseResult result = new SAXParseResult();
		try {
			SCHEMA_CATALOG.validateCacheSchema( new ResultErrorHandler( result ) , new SAXSource( new InputSource( xmlData )  ), CURRENT );	
		} catch (Exception e) {
			throw new XMLException( e );
		}
		if ( result.isTotalSuccess() ) {
			logger.info( "Validation completed without errors or warning" );
		} else {
			logResult( result , logger );
		}
		return result.isPartialSuccess();
	}
	
}
