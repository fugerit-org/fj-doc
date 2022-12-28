package org.fugerit.java.doc.base.xml;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.util.Properties;

import javax.xml.parsers.SAXParser;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.xml.XMLValidator;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.core.xml.sax.XMLFactorySAX;
import org.fugerit.java.core.xml.sax.XMLValidatorSAX;
import org.fugerit.java.core.xml.sax.dh.DefaultHandlerComp;
import org.fugerit.java.core.xml.sax.er.ByteArrayEntityResolver;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.parser.AbstractDocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class DocXmlParser extends AbstractDocParser {

	private static final Logger logger = LoggerFactory.getLogger( DocXmlParser.class );
	
	private static final String DTD = "/config/doc-1-0.dtd";
	private static final String XSD = "/config/doc-"+DocFacade.CURRENT_VERSION+".xsd";
	
	private static final Properties DEFAULT_PARAMS = new Properties();
	
	private DocHelper docHelper;
	
	private Properties params;

	public DocXmlParser( DocHelper docHelper, Properties params ) {
		super( DocFacadeSource.SOURCE_TYPE_XML );
		this.docHelper = docHelper;
		this.params = params;
	}

	public DocXmlParser() {
		this( DocHelper.DEFAULT, DEFAULT_PARAMS );
	}

	private static byte[] readData( String res, byte[] alreadRead ) {
		byte[] data = alreadRead;
		if ( data != null ) {
			try {
				data = StreamIO.readBytes( DocFacade.class.getResourceAsStream( res ) );	
			} catch (Exception e) {
				logger.warn( "Read error", e );
			}	
		}
		return data;
	}
	
	private static final byte[] XSD_DATA = readData( XSD, null );
	private static final byte[] DTD_DATA = readData( DTD, null );
	
	private static ByteArrayEntityResolver newEntityResolver( Properties params ) {
		byte[] data = null;
		String validatationMode = params.getProperty( DocFacade.PARAM_DEFINITION_MODE, DocFacade.PARAM_DEFINITION_MODE_DEFAULT ) ;
		if ( DocFacade.PARAM_DEFINITION_MODE_DTD.equalsIgnoreCase( validatationMode ) ) {
			data = DTD_DATA;
		} else {
			data = XSD_DATA;
		}
		logger.debug( DocFacade.PARAM_DEFINITION_MODE+" -> "+validatationMode );
		ByteArrayEntityResolver er = new ByteArrayEntityResolver( data, null, DocFacade.SYSTEM_ID );
		return er;
	}
	
	@Override
	protected DocBase parseWorker(Reader reader) throws Exception {
		SAXParser parser = XMLFactorySAX.makeSAXParser( false ,  false );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamIO.pipeStream( DocFacade.class.getResourceAsStream( DTD  ), baos, StreamIO.MODE_CLOSE_BOTH );
		ByteArrayEntityResolver er = newEntityResolver( this.params );
		DocContentHandler dch =  new DocContentHandler( this.docHelper );
		DefaultHandlerComp dh = new DefaultHandlerComp( dch );
		dh.setWrappedEntityResolver( er );
		parser.parse( new InputSource(reader), dh);
		return dch.getDocBase();
	}
	
	@Override
	protected DocValidationResult validateWorker(Reader reader) throws Exception {
		ByteArrayEntityResolver er = newEntityResolver( params );
		XMLValidator validator = XMLValidatorSAX.newInstance( er );
		SAXParseResult result = validator.validateXML( reader );
		DocValidationResult docResult = DocValidationResult.newDefaultNotDefiniedResult();
		for ( Exception e : result.fatalsAndErrors() )  {
			docResult.getErrorList().add( e.toString() );
		}
		for ( Exception e : result.warnings() )  {
			docResult.getInfoList().add( e.toString() );
		}
		docResult.evaluateResult();
		return docResult;
	}

}
