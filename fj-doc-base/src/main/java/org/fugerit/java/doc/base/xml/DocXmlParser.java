package org.fugerit.java.doc.base.xml;

import java.io.Reader;

import javax.xml.parsers.SAXParser;

import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.core.xml.sax.XMLFactorySAX;
import org.fugerit.java.core.xml.sax.dh.DefaultHandlerComp;
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
	
	private DocHelper docHelper;

	public DocXmlParser( DocHelper docHelper ) {
		super( DocFacadeSource.SOURCE_TYPE_XML );
		this.docHelper = docHelper;
	}

	public DocXmlParser() {
		this( DocHelper.DEFAULT );
	}

	@Override
	protected DocBase parseWorker(Reader reader) throws Exception {
		SAXParser parser = XMLFactorySAX.makeSAXParser( false ,  true );
		DocContentHandler dch =  new DocContentHandler( this.docHelper );
		DefaultHandlerComp dh = new DefaultHandlerComp( dch );
		parser.parse( new InputSource(reader), dh);
		return dch.getDocBase();
	}
	
	@Override
	protected DocValidationResult validateWorker(Reader reader, boolean parseVersion) throws Exception {
		DocValidationResult docResult = DocValidationResult.newDefaultNotDefinedResult();
		SAXParseResult result = null;
		if ( parseVersion ) {
			result = DocValidator.validateVersion( reader );
		} else {
			result = DocValidator.validate( reader );
		}
		for ( Exception e : result.fatalsAndErrors() ) {
			docResult.getErrorList().add( e.toString() );
		}
		for ( Exception e : result.warnings() ) {
			docResult.getInfoList().add( e.toString() );
		}
		docResult.evaluateResult();
		logger.debug( "Validation result {}", docResult );
		return docResult;
	}

}
