package org.fugerit.java.doc.base.xml;

import java.io.Reader;

import javax.xml.parsers.SAXParser;

import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.core.xml.sax.XMLFactorySAX;
import org.fugerit.java.core.xml.sax.dh.DefaultHandlerComp;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.parser.AbstractDocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.xml.sax.InputSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocXmlParser extends AbstractDocParser {
	
	private DocHelper docHelper;

	public DocXmlParser( DocHelper docHelper ) {
		super( DocFacadeSource.SOURCE_TYPE_XML );
		this.docHelper = docHelper;
	}

	public DocXmlParser() {
		this( DocHelper.DEFAULT );
	}

	@Override
	protected DocBase parseWorker(Reader reader) throws DocException {
		DocContentHandler dch =  new DocContentHandler( this.docHelper );
		try {
			SAXParser parser = XMLFactorySAX.makeSAXParser( false ,  true );
			DefaultHandlerComp dh = new DefaultHandlerComp( dch );
			parser.parse( new InputSource(reader), dh);
		} catch (Exception e) {
			throw DocException.convertExMethod( "parseWorker" , e );
		}
		return dch.getDocBase();
	}
	
	@Override
	protected DocValidationResult validateWorker(Reader reader, boolean parseVersion) throws DocException {
		DocValidationResult docResult = DocValidationResult.newDefaultNotDefinedResult();
		SAXParseResult result = null;
		try {
			if ( parseVersion ) {
				result = DocValidator.validateVersion( reader );
			} else {
				result = DocValidator.validate( reader );
			}
		} catch (Exception e) {
			throw DocException.convertExMethod( "validateWorker" , e );
		}
		for ( Exception e : result.fatalsAndErrors() ) {
			docResult.getErrorList().add( e.toString() );
		}
		for ( Exception e : result.warnings() ) {
			docResult.getInfoList().add( e.toString() );
		}
		docResult.evaluateResult();
		log.debug( "Validation result {}", docResult );
		return docResult;
	}

}
