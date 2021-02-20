package org.fugerit.java.doc.base.xml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;

import javax.xml.transform.sax.SAXSource;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.config.XMLSchemaCatalogConfig;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.core.xml.sax.eh.ResultErrorHandler;
import org.slf4j.Logger;
import org.xml.sax.InputSource;

public class DocValidator {

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
			SCHEMA_CATALOG.validateCacheSchema( new ResultErrorHandler( result ) , new SAXSource( new InputSource( xmlData )  ), "current" );	
		} catch (Exception e) {
			throw new XMLException( e );
		}
		return result;
	}
	
	public static void logResult( SAXParseResult result, Logger logger ) throws XMLException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try ( PrintStream ps = new PrintStream( baos ) ) {
			result.printErrorReport( ps );
			logger.info( "Validation issues : \n"+new String( baos.toByteArray() ) );
		}
	}
	
	public static boolean logValidation( Reader xmlData, Logger logger ) throws XMLException {
		SAXParseResult result = new SAXParseResult();
		try {
			SCHEMA_CATALOG.validateCacheSchema( new ResultErrorHandler( result ) , new SAXSource( new InputSource( xmlData )  ), "current" );	
		} catch (Exception e) {
			throw new XMLException( e );
		}
		if ( result.isTotalSuccess() ) {
			logger.info( "Validation completed without errors or warning" );
		} else {
			
		}
		return result.isPartialSuccess();
	}
	
}
