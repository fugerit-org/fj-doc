package org.fugerit.java.doc.base.xml;

import java.io.Reader;

import javax.xml.parsers.SAXParser;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.core.xml.sax.XMLFactorySAX;
import org.fugerit.java.core.xml.sax.dh.DefaultHandlerComp;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocHelper;
import org.fugerit.java.doc.base.parser.AbstractDocParser;
import org.fugerit.java.doc.base.parser.DocParserContext;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.xml.sax.InputSource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocXmlParser extends AbstractDocParser {
	
	private DocHelper docHelper;
	
	@Getter private boolean failWhenElementNotFound;

	public DocXmlParser( DocHelper docHelper, boolean failWhenElementNotFound ) {
		super( DocFacadeSource.SOURCE_TYPE_XML );
		this.docHelper = docHelper;
		this.failWhenElementNotFound = failWhenElementNotFound;
	}
	
	public DocXmlParser( boolean failOnElementNotFound ) {
		this( DocHelper.DEFAULT, failOnElementNotFound );
	}
	
	public DocXmlParser( DocHelper docHelper ) {
		this( docHelper, DocParserContext.FAIL_WHEN_ELEMENT_NOT_FOUND_DEFAULT );
	}

	public DocXmlParser() {
		this( DocHelper.DEFAULT );
	}

	@Override
	protected DocBase parseWorker(Reader reader) throws DocException {
		return SafeFunction.get( () -> {
			DocContentHandler dch =  new DocContentHandler( this.docHelper, this.isFailWhenElementNotFound() );	
			SAXParser parser = XMLFactorySAX.makeSAXParser( false ,  true );
			DefaultHandlerComp dh = new DefaultHandlerComp( dch );
			parser.parse( new InputSource(reader), dh);
			return dch.getDocBase();
		} );
	}
	
	@Override
	protected DocValidationResult validateWorker(Reader reader, boolean parseVersion) throws DocException {
		return SafeFunction.get( () -> {
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
			log.debug( "Validation result {}", docResult );
			return docResult;
		} );
		
		
	}

}
